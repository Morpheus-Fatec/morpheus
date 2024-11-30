package fatec.morpheus.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.DTO.CronProperties;
import fatec.morpheus.service.CronManager;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/morpheus/config")
public class CronController {
    private static final Logger logger = LoggerFactory.getLogger(CronController.class);

    @Autowired
    private final CronManager cronManager;

    public CronController(CronManager cronManager) {
        this.cronManager = cronManager;
    }

    @GetMapping("/properties")
    public ResponseEntity<CronProperties> getProperties() {
        try {
            Properties properties = new Properties();

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
                if (inputStream == null) {
                    logger.error("Arquivo application.properties não encontrado.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null);
                }
                properties.load(inputStream);
            }

            CronProperties cronProperties = new CronProperties();
            cronProperties.setTimeZone(properties.getProperty("cron.timeZone"));
            cronProperties.setActive(Boolean.parseBoolean(properties.getProperty("cron.active")));
            cronProperties.setFrequency(properties.getProperty("cron.frequency"));
            cronProperties.setTime(properties.getProperty("cron.time"));
            cronProperties.setTimeout(properties.getProperty("cron.timeout"));

            return ResponseEntity.ok(cronProperties);

        } catch (IOException e) {
            logger.error("Erro ao carregar as propriedades: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProperties(@RequestBody CronProperties configRequest) {
        logger.info("Solicitação recebida: {}", configRequest);
        try {
            Path externalPropertiesFile = Paths.get("back-end/src/main/resources/application.properties");

            if (Files.notExists(externalPropertiesFile)) {
                logger.info("Arquivo externo de propriedades não encontrado. Criando uma cópia.");
                Files.createDirectories(externalPropertiesFile.getParent());
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
                    if (inputStream != null) {
                        Files.copy(inputStream, externalPropertiesFile);
                    }
                }
            }

            Properties properties = new Properties();

            try (InputStream inputStream = Files.newInputStream(externalPropertiesFile)) {
                properties.load(inputStream);
            }

            if (!isValidTimeZone(configRequest.getTimeZone())) {
                logger.error("Fuso horário inválido: {}", configRequest.getTimeZone());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Fuso horário inválido: " + configRequest.getTimeZone());
            }

            String cronExpression = convertToCronExpression(configRequest.getFrequency(), configRequest.getTime());
            if (cronExpression == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Valor de frequência inválido: " + configRequest.getFrequency());
            }

            properties.setProperty("cron.expression", cronExpression);
            properties.setProperty("cron.timeZone", configRequest.getTimeZone());
            properties.setProperty("cron.active", String.valueOf(configRequest.isActive()));
            properties.setProperty("cron.frequency", configRequest.getFrequency());
            properties.setProperty("cron.time", configRequest.getTime());
            properties.setProperty("cron.timeout", configRequest.getTimeout());
            

            logger.info("Propriedades antes da gravação: {}", properties);

            try (OutputStream outputStream = Files.newOutputStream(externalPropertiesFile)) {
                properties.store(outputStream, "Atualizado pelo Spring Boot");
            }

            cronManager.updateCron(cronExpression);
            logger.info("Tarefa cron atualizada com a expressão: {}", cronExpression);

            if (configRequest.isActive()) {
                cronManager.startCronTask();
                logger.info("Tarefa cron iniciada.");
            } else {
                cronManager.stopCronTask();
                logger.info("Tarefa cron parada.");
            }

            return ResponseEntity.ok("Propriedades atualizadas com sucesso!");

        } catch (IOException e) {
            logger.error("Erro ao atualizar as propriedades: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar as propriedades: " + e.getMessage());
        }
    }

    private String convertToCronExpression(String frequency, String time) {
        String[] timeParts = time.split(":");
        if (timeParts.length != 2) {
            return null;
        }
        String hour = timeParts[0];
        String minute = timeParts[1];

        return switch (frequency.toLowerCase()) {
            case "daily" -> String.format("0 %s %s * * *", minute, hour);
            case "hourly" -> "0 0 * * * ?";
            case "weekly" -> String.format("0 %s %s ? * MON", minute, hour);
            case "monthly" -> String.format("0 %s %s 1 * ?", minute, hour);
            default -> null;
        };
    }

    private boolean isValidTimeZone(String timeZone) {
        String[] availableZoneIds = TimeZone.getAvailableIDs();
        for (String zoneId : availableZoneIds) {
            if (zoneId.equals(timeZone)) {
                return true;
            }
        }
        return false;
    }
}
