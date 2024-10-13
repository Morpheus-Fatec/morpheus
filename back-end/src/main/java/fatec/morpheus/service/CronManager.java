package fatec.morpheus.service;

import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

@Service
public class CronManager {

    private final ScheduledTaskRegistrar taskRegistrar;
    private Runnable cronTask;
    private String cronExpression;

    public CronManager(ScheduledTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
    }

    // Adiciona a tarefa cron
    public void addCronTask(String cronExpression) {
        if (cronTask != null) {
            taskRegistrar.getScheduledTasks().forEach(task -> task.cancel());
        }

        cronTask = () -> {
            // A lógica a ser executada
            System.out.println("Task executed at: " + new java.util.Date());
        };

        taskRegistrar.addCronTask(cronTask, cronExpression);
    }

    // Atualiza a expressão cron
    public void updateCronExpression(String newCronExpression) {
        this.cronExpression = newCronExpression;
        addCronTask(newCronExpression);
    }
}
