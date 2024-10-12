package fatec.morpheus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.entity.News;
import fatec.morpheus.repository.NewsRepository;
import fatec.morpheus.service.NewsService;
import fatec.morpheus.service.NewsSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/morpheus/News")
public class NewsController {
    
    @Autowired
    private NewsService newsService;


    @Operation(summary = "Busca", description = "Retorna todas as notícias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notícias retornadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao retornar notícias"),
    })
    @GetMapping
    public ResponseEntity<List<News>> getAllNews(@RequestParam int page, @RequestParam int itens){
        List<News> news = newsService.findAllNews(page, itens);
        return ResponseEntity.ok(news);
    }

}
