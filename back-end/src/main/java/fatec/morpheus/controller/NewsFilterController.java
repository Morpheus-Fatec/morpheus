package fatec.morpheus.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fatec.morpheus.DTO.NewsSearchRequest;
import fatec.morpheus.entity.NewsReponse;
import fatec.morpheus.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/morpheus/NewsFilter")
public class NewsFilterController {

    @Autowired
    private NewsService newsService;


        @Operation(summary = "Filtro Notícias", description = "Retorna notícias relacionadas aos parâmetros a serem inseridos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notícias retornadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Termo especificado não encontrado"),
    })
    
    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> getNews(
        @RequestBody @Valid NewsSearchRequest request,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "50") int items) {
        
        Map<String, Object> response = newsService.findNewsWithFilter(request, page - 1, items);
        
        return ResponseEntity.ok(response);
    }
}
