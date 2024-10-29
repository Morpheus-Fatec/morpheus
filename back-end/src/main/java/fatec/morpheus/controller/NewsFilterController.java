package fatec.morpheus.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fatec.morpheus.entity.NewsReponse;
import fatec.morpheus.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> buscarNoticiasComFiltros(
            @RequestParam(required = false) List<String> titles,
            @RequestParam(required = false) List<String> contents,
            @RequestParam(required = false) List<String> authors,
            @RequestParam(required = false) List<String> portals,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEnd,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int items) {


        PageRequest pageRequest = PageRequest.of(page - 1, items);
        Page<NewsReponse> newsPage = newsService.buscarNoticiasComFiltros(titles, contents, authors, portals, dataStart, dataEnd, pageRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("news", newsPage.getContent());
        response.put("totalItems", newsPage.getTotalElements());
        response.put("totalPages", newsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }
}
