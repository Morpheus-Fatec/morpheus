package fatec.morpheus.controller;

import fatec.morpheus.entity.CronProperties;
import fatec.morpheus.service.CronManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.TimeZone;

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

    // Método GET para retornar as propriedades atuais
    @GetMapping("/properties")
    public ResponseEntity<CronProperties> getProperties() {
        try {
            Path propertiesFile = Paths.get("src/main/resources/application.properties");
            Properties properties = new Properties();

            // Carrega as propriedades do arquivo
            try (InputStream inputStream = Files.newInputStream(propertiesFile)) {
                properties.load(inputStream);
            }

            // Preenche o objeto CronProperties com os valores das propriedades
            CronProperties cronProperties = new CronProperties();
            cronProperties.setTimeZone(properties.getProperty("cron.timeZone"));
            cronProperties.setActive(Boolean.parseBoolean(properties.getProperty("cron.active")));
            cronProperties.setFrequency(properties.getProperty("cron.frequency"));
            cronProperties.setTime(properties.getProperty("cron.time"));

            // Retorna o objeto com as propriedades para o front-end
            return ResponseEntity.ok(cronProperties);

        } catch (IOException e) {
            logger.error("Error loading properties: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProperties(@RequestBody CronProperties configRequest) {
        logger.info("Received request: {}", configRequest);
        try {
            Path propertiesFile = Paths.get("src/main/resources/application.properties");
            Properties properties = new Properties();

            // Carrega as propriedades atuais
            try (InputStream inputStream = Files.newInputStream(propertiesFile)) {
                properties.load(inputStream);
            }

            // Valida o time zone
            if (!isValidTimeZone(configRequest.getTimeZone())) {
                logger.error("Invalid time zone: {}", configRequest.getTimeZone());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid time zone: " + configRequest.getTimeZone());
            }

            // Converte a frequência e o horário para expressão cron
            String cronExpression = convertToCronExpression(configRequest.getFrequency(), configRequest.getTime());
            if (cronExpression == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid frequency value: " + configRequest.getFrequency());
            }

            // Atualiza as propriedades
            properties.setProperty("cron.expression", cronExpression);
            properties.setProperty("cron.timeZone", configRequest.getTimeZone());
            properties.setProperty("cron.active", String.valueOf(configRequest.isActive()));
            properties.setProperty("cron.frequency", configRequest.getFrequency());
            properties.setProperty("cron.time", configRequest.getTime());

            // Salva as novas propriedades no arquivo
            try (OutputStream outputStream = Files.newOutputStream(propertiesFile)) {
                properties.store(outputStream, "Updated by Spring Boot");
            }

            // Atualiza a expressão cron no CronSchedule
            cronManager.updateCron(cronExpression);
            logger.info("Updated cron task with expression: {}", cronExpression);

            // Ativa ou desativa o cron com base no status 'active' da solicitação
            if (configRequest.isActive()) {
                cronManager.startCronTask();
                logger.info("Cron task started.");
            } else {
                cronManager.stopCronTask();
                logger.info("Cron task stopped.");
            }

            return ResponseEntity.ok("Properties updated successfully!");

        } catch (IOException e) {
            logger.error("Error updating properties: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating properties: " + e.getMessage());
        }
    }

    // Método que converte a frequência e o horário para expressão cron
    private String convertToCronExpression(String frequency, String time) {
        String[] timeParts = time.split(":");
        if (timeParts.length != 2) {
            return null; // Retorna nulo se o formato do horário não estiver correto
        }
        String hour = timeParts[0];
        String minute = timeParts[1];

        return switch (frequency.toLowerCase()) {
            case "daily" -> String.format("0 %s %s * * *", minute, hour);
            case "hourly" -> "0 0 * * * ?";
            case "weekly" -> String.format("0 %s %s ? * MON", minute, hour);
            case "monthly" -> String.format("0 %s %s 1 * ?", minute, hour);
            default -> null; // Retorna nulo para frequência inválida
        };
    }

    // Método que valida se o time zone é válido
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
