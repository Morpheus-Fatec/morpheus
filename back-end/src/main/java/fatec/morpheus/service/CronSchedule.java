package fatec.morpheus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@RefreshScope
@Component
public class CronSchedule {

    @Value("${cron.frequency:0 * * * * ?}") // Valor padrão para a expressão cron
    public String cronExpression;
    @Value("${cron.timeZone:America/Sao_Paulo}") // Valor padrão para a expressão cron
    public String timeZone;

    private final CronManager cronManager;

    public CronSchedule(CronManager cronManager) {
        this.cronManager = cronManager;
    }

    // Método a ser chamado após a injeção de dependências
    @PostConstruct
    public void init() {
        if (cronExpression != null && !cronExpression.isEmpty()) {
            cronManager.addCronTask(cronExpression); // Inicializa a tarefa cron com a expressão
        } else {
            // Lidar com a situação onde a expressão cron não é válida
            throw new IllegalArgumentException("Cron expression cannot be empty");
        }
    }

    // Método que será chamado conforme o cron
    @Scheduled(cron = "${cron.frequency}", zone = "${cron.timeZone}")
    public void scheduledActions() {
        System.out.println("Iniciando o processamento dos dados de vendas: " + System.currentTimeMillis());
    }

    // Atualiza a expressão cron
    public void updateCronExpression(String newCronExpression) {
        cronManager.updateCronExpression(newCronExpression);
    }
}
