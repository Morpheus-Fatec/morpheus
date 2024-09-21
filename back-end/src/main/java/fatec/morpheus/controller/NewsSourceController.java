package fatec.morpheus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.dao.NewsSource;
import fatec.morpheus.repository.NewsSourceRepository;
import main.java.fatec.morpheus.service.NewsSourceService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/morpheus/source")
public class NewsSourceController {
 

    @Autowired
    private NewsSourceService newsSourceService;

    @PostMapping("/createNewsPortal")
    public ResponseEntity<NewsSource> createNewsSource(@RequestBody NewsSource newsSourceToCreate) {
        NewsSource savedNewsSource =  newsSourceService.saveNewsSource(newsSourceToCreate);

        return new ResponseEntity<>(savedNewsSource, HttpStatus.CREATED);
    }
    

    @GetMapping("/")
    ResponseEntity<List<NewsSource>> getNewsSource(){
        List<NewsSource> sources = newsSourceService.findAllNewsSource();
        return new ResponseEntity<>(sources, HttpStatus.OK);
    }

    @GetMapping("getNewsSourceById/{id}")
    ResponseEntity<NewsSource> getNewsSourceById(@PathVariable int id){
        return newsSourceService.findNewsSourceById(id);
    }

    @PutMapping("editNewsSource/{id}")
    public ResponseEntity<NewsSource> editNewsSource(@PathVariable int id, @RequestBody NewsSource NewsSourceToEdit) {
        return newsSourceService.editNewsSourceById(id, NewsSourceToEdit);
    }
    
    @DeleteMapping("deleteNewsSource/{id}")
    public ResponseEntity<NewsSource> deleteNewsSource(@PathVariable int id) {
       return newsSourceService.deleteNewsSourceById(id);
    }

}
