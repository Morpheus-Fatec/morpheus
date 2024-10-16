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

    // Injetando a expressão cron inicial a partir do arquivo de configuração
    @Value("${cron.frequency:0 * * * * *}") // Valor padrão: a cada minuto
    private String cronExpression;

    public CronManager() {
        taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) taskScheduler).initialize();
    }

    // Método para iniciar a tarefa cron
    @PostConstruct
    public void startCronTask() {
        scheduledTask = taskScheduler.schedule(this::runTask, getTrigger());
        System.out.println("Tarefa cron iniciada com a expressão: " + cronExpression);
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
            System.out.println("Tarefa cron parada.");
        } else {
            System.out.println("Nenhuma tarefa cron ativa para parar.");
        }
    }

    // Tarefa executada pelo cron
    private void runTask() {
        System.out.println("Executando a tarefa com o cron: " + cronExpression);
    }

    // Obtém o Trigger com base na expressão cron
    private Trigger getTrigger() {
        return triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(cronExpression);
            return cronTrigger.nextExecutionTime(triggerContext).toInstant();
        };
    }
}
