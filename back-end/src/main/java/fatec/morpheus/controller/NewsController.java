package fatec.morpheus.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.entity.News;
import fatec.morpheus.entity.NewsReponse;
import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.entity.PaginatedNewsResponse;
import fatec.morpheus.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<PaginatedNewsResponse> getAllNews(@RequestParam int page, @RequestParam(defaultValue = "50") int itens) {
        PaginatedNewsResponse news = newsService.getNewsWithDetails(page, itens);
        return ResponseEntity.ok(news);
    }


}
