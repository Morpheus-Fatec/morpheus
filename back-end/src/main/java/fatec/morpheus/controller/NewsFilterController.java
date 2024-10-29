package fatec.morpheus.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fatec.morpheus.entity.NewsReponse;
import fatec.morpheus.service.NewsService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/morpheus/NewsFilter")
public class NewsFilterController {

    @Autowired
    private NewsService newsService;


    @GetMapping("/search")
    public ResponseEntity<List<NewsReponse>> searchNews(
            @RequestParam(required = false) List<String> titles,
            @RequestParam(required = false) List<String> contents,
            @RequestParam(required = false) List<String> authors,
            @RequestParam(required = false) List<String> portals,
            @RequestParam(required = false) LocalDate dataStart,
            @RequestParam(required = false) LocalDate dataEnd) {
        
        List<NewsReponse> noticias = newsService.buscarNoticiasComFiltros(titles, contents, authors, portals, dataStart, dataEnd);
        
        if (noticias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        }
        
        return new ResponseEntity<>(noticias, HttpStatus.OK);
    }
}
