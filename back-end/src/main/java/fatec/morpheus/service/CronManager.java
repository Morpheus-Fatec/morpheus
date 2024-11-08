package fatec.morpheus.service;

import fatec.morpheus.controller.CronController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
public class CronManager {
    private static final Logger logger = LoggerFactory.getLogger(CronController.class);
    private TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;
    @Autowired
    private ScrapingService scrapingService;    

    @Value("${cron.expression:0 * * * * *}")
    private String cronExpression;

    @Value("${cron.timeZone:America/Sao_Paulo}")
    private String cronTimeZone;

    public CronManager() {
        taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) taskScheduler).initialize();
    }

    @PostConstruct
    public void startCronTask() {
        scheduledTask = taskScheduler.schedule(this::runTask, getTrigger());
        logger.info("Tarefa cron iniciada com a expressão: " + cronExpression);
    }

    public void updateCron(String newCron) {
        this.cronExpression = newCron;
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
    }

    public void stopCronTask() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
            logger.info("Tarefa cron parada.");
        } else {
            logger.info("Nenhuma tarefa cron ativa para parar.");
        }
    }

    private void runTask() {
        scrapingService.getSearch();
        logger.info("Executando a tarefa com o cron: " + cronExpression);
    }

    private Trigger getTrigger() {
        return triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(cronExpression,TimeZone.getTimeZone(cronTimeZone));

            Date nextExecution = Date.from(Objects.requireNonNull(cronTrigger.nextExecution(triggerContext)));

            return nextExecution.toInstant();
        };
    }

}
