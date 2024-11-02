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
    public ResponseEntity<Map<String, Object>> getNoticias(
        @RequestBody NewsSearchRequest request,
        @RequestParam(defaultValue = "2") int page,
        @RequestParam(defaultValue = "10") int items) {

        Page<NewsReponse> newsPage = newsService.findNewsWithFilter(request, page -1, items);
        
        Map<String, Object> response = new HashMap<>();
        response.put("news", newsPage.getContent());
        response.put("totalItems", newsPage.getTotalElements());
        response.put("totalPages", newsPage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
}
