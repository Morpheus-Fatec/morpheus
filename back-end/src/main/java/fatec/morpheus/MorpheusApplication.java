package fatec.morpheus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import fatec.morpheus.service.ApiContentService;

@SpringBootApplication
@EnableScheduling
public class MorpheusApplication implements CommandLineRunner {

    @Autowired
    private ApiContentService apiContentService;

    public static void main(String[] args) {
        SpringApplication.run(MorpheusApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        apiContentService.searchContentApi();
    }
}