package fatec.morpheus;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fatec.morpheus.service.ScrapingService;

@SpringBootApplication
public class MorpheusApplication {

	public static void main(String[] args) {
		SpringApplication.run(MorpheusApplication.class, args);
	}

	@Bean
    public CommandLineRunner run(ScrapingService scraperService) {
			return args -> {
				System.out.println("Executando o Scraper...");
				scraperService.getSearch();
			};
    }
}
