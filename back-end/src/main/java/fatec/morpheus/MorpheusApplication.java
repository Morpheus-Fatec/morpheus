package fatec.morpheus;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import fatec.morpheus.service.AdaptedTagsService;

@SpringBootApplication
@EnableScheduling
public class MorpheusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MorpheusApplication.class, args);
		
    }

    @Bean
    public CommandLineRunner run(AdaptedTagsService adaptedTags) {
        return args -> {
            System.out.println(adaptedTags.findVariation("safra abacaxi"));
			
        };
    }
}