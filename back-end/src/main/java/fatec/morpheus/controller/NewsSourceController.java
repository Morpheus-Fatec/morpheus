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
import fatec.morpheus.service.NewsSourceService;

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

    @PostMapping
    public ResponseEntity<NewsSource> createNewsSource(@RequestBody NewsSource newsSource) {
        NewsSource savedNewsSource = newsSourceService.saveNewsSource(newsSource);
        return new ResponseEntity<>(savedNewsSource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NewsSource>> getAllNewsSources() {
        List<NewsSource> sources = newsSourceService.findAllNewsSources();
        return ResponseEntity.ok(sources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsSource> getNewsSourceById(@PathVariable int id) {
        return newsSourceService.findNewsSourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsSource> updateNewsSource(@PathVariable int id, @RequestBody NewsSource newsSource) {
        return newsSourceService.updateNewsSourceById(id, newsSource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NewsSource> deleteNewsSource(@PathVariable int id) {
        return newsSourceService.deleteNewsSourceById(id);
    }
}
