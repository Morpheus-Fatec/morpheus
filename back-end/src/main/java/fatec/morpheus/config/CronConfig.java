package fatec.morpheus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling // Habilita o agendamento
public class CronConfig {

    @Bean
    public ScheduledTaskRegistrar taskRegistrar() {
        return new ScheduledTaskRegistrar();
    }
}
