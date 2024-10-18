package fatec.morpheus.service;

import fatec.morpheus.controller.CronController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledFuture;

@Service
public class CronManager {
    private static final Logger logger = LoggerFactory.getLogger(CronController.class);
    private TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    // Injetando a expressão cron inicial a partir do arquivo de configuração
    @Value("${cron.expression:0 * * * * *}") // Valor padrão: a cada minuto
    private String cronExpression;

    public CronManager() {
        taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) taskScheduler).initialize();
    }

    // Método para iniciar a tarefa cron
    @PostConstruct
    public void startCronTask() {
        scheduledTask = taskScheduler.schedule(this::runTask, getTrigger());
        logger.info("Tarefa cron iniciada com a expressão: " + cronExpression);
    }

    // Método para atualizar a expressão cron e reiniciar a tarefa
    public void updateCron(String newCron) {
        this.cronExpression = newCron;
        if (scheduledTask != null) {
            scheduledTask.cancel(false);  // Cancela a tarefa anterior
        }
    }

    // Método para parar o cron
    public void stopCronTask() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);  // Cancela a tarefa cron se estiver rodando
            logger.info("Tarefa cron parada.");
        } else {
            logger.info("Nenhuma tarefa cron ativa para parar.");
        }
    }

    // Tarefa executada pelo cron
    private void runTask() {
        logger.info("Executando a tarefa com o cron: " + cronExpression);
    }

    // Obtém o Trigger com base na expressão cron
    private Trigger getTrigger() {
        return triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(cronExpression);
            return cronTrigger.nextExecutionTime(triggerContext).toInstant();
        };
    }
}
