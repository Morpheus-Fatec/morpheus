package fatec.morpheus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.repository.NewsSourceRepository;

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
    private NewsSourceRepository newsSourceRepository;

    @PostMapping("/createNewsPortal")
    public ResponseEntity<NewsSource> createNewsSource(@RequestBody NewsSource NewsSourceToCreate) {
        NewsSource savedNewsSource = newsSourceRepository.save(NewsSourceToCreate);
        
        return new ResponseEntity<>(savedNewsSource, HttpStatus.CREATED);
    }
    

    @GetMapping("/")
    ResponseEntity<List<NewsSource>> getNewsSource(){
        List<NewsSource> sources = newsSourceRepository.findAll();
        return new ResponseEntity<>(sources, HttpStatus.OK);
    }

    @PutMapping("editNewsSource/{id}")
    public ResponseEntity<NewsSource> editNewsSource(@PathVariable int id, @RequestBody NewsSource NewsSourceToEdit) {
        return newsSourceRepository.findById(id)
            .map(newsSource -> {
                newsSource.setSrcName(NewsSourceToEdit.getSrcName());
                newsSource.setType(NewsSourceToEdit.getType());
                newsSource.setAddress(NewsSourceToEdit.getAddress());
                newsSourceRepository.save(newsSource);
                return new ResponseEntity<>(newsSource, HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("deleteNewsSource/{id}")
    public ResponseEntity<NewsSource> deleteNewsSource(@PathVariable int id) {
        return newsSourceRepository.findById(id)
            .map(newsSource -> {
                newsSourceRepository.deleteById(id);
                return new ResponseEntity<>(newsSource, HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
