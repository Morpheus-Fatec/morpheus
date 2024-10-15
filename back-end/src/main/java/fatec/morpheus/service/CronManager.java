package fatec.morpheus.service;


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

    private TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;
    @Value("${cron.frequency:0 * * * * *}")
    private String cronExpression = "${cron.frequency}";  // Configuração inicial (a cada minuto)

    public CronManager() {
        taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) taskScheduler).initialize();
    }

    // Método que será executado automaticamente após a inicialização do bean
    @PostConstruct
    public void init() {
        startCronTask();
        System.out.println("Tarefa cron inicializada na inicialização da aplicação.");
    }

    public void startCronTask() {
        scheduledTask = taskScheduler.schedule(this::runTask, getTrigger());
    }

    public void updateCron(String newCron) {
        this.cronExpression = newCron;
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        startCronTask();
    }

    private void runTask() {
        System.out.println("Executando a tarefa com o cron: " + cronExpression);
    }

    private Trigger getTrigger() {
        return triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(cronExpression);
            return cronTrigger.nextExecutionTime(triggerContext).toInstant();
        };
    }
}
