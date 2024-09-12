package fatec.morpheus.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.entity.NewsPortal;

@CrossOrigin(origins = "*")
@RestController
public class NewsPortalController {

    @GetMapping
    List<NewsPortal> getNewsPortal(){
        NewsPortal portal1 = new NewsPortal();
        portal1.setId(1L);
        portal1.setName("Portal 1");
        portal1.setLink("http://portal1.com");
        portal1.setRegistrationDate(new Date(0));

        NewsPortal portal2 = new NewsPortal();
        portal2.setId(2L);
        portal2.setName("Portal 2");
        portal2.setLink("http://portal2.com");
        portal2.setRegistrationDate(new Date(0));

        NewsPortal portal3 = new NewsPortal();
        portal3.setId(3L);
        portal3.setName("Portal 3");
        portal3.setLink("http://portal3.com");
        portal3.setRegistrationDate(new Date(0));

        List<NewsPortal> portals = new ArrayList<>();
        portals.add(portal1);
        portals.add(portal2);
        portals.add(portal3);
        return portals;
    }

}
