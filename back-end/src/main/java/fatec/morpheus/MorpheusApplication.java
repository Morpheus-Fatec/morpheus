package fatec.morpheus;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext; // Correct import
import org.springframework.scheduling.annotation.EnableScheduling;

import fatec.morpheus.service.AdaptedTagsService;

@SpringBootApplication
@EnableScheduling
public class MorpheusApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MorpheusApplication.class, args);

        // Retrieve the service bean from Spring context
        AdaptedTagsService service = context.getBean(AdaptedTagsService.class);

        ArrayList<String> list = new ArrayList<>();
        list.add("AI today");
        list.add("health in old age");
        list.add("Mini vini gay");

        System.out.println(service.findVariationByText(list));
    }
}
