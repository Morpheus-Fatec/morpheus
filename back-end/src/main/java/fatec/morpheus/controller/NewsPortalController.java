package fatec.morpheus.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.entity.NewsPortal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@CrossOrigin(origins = "*")
@RestController
public class NewsPortalController {
    List<NewsPortal> portals = new ArrayList<>();

    public NewsPortalController(){
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
        portals.add(portal1);
        portals.add(portal2);
        portals.add(portal3);
    }

    @PostMapping("/createNewsPortal")
    public ResponseEntity<NewsPortal> createNewsPortal(@RequestBody NewsPortal newsPortalToCreate) {
        newsPortalToCreate.setId((long) (portals.size() + 1));
        portals.add(newsPortalToCreate);
        
        return new ResponseEntity<>(newsPortalToCreate, HttpStatus.CREATED);
    }
    

    @GetMapping
    List<NewsPortal> getNewsPortal(){
        return portals;
    }

    @PutMapping("editNewsPortal/{id}")
    public ResponseEntity<NewsPortal> editNewsPortal(@PathVariable Long id, @RequestBody NewsPortal newsPortalToEdit) {
        for (NewsPortal newsPortal : portals) {
            if (newsPortal.getId().equals(id)) {
                newsPortal.setName(newsPortalToEdit.getName());
                newsPortal.setLink(newsPortalToEdit.getLink());
                newsPortal.setRegistrationDate(newsPortalToEdit.getRegistrationDate());
                return new ResponseEntity<>(newsPortal, HttpStatus.OK);
            }
        }
        
        return new ResponseEntity<>(newsPortalToEdit, HttpStatus.OK);
    }
    
    @DeleteMapping("deleteNewsPortal/{id}")
    public ResponseEntity<NewsPortal> deleteNewsPortal(@PathVariable Long id) {
        for (NewsPortal newsPortal : portals) {
            if (newsPortal.getId().equals(id)) {
                portals.remove(newsPortal);
                return new ResponseEntity<>(newsPortal, HttpStatus.OK);
            }
        }
        
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
