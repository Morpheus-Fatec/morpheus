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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary= "", description = "Cria um novo portal de notícias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Portal de notícias criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar portal de notícias"),
    })
    @PostMapping
    public ResponseEntity<NewsSource> createNewsSource(@RequestBody NewsSource newsSource) {
        NewsSource savedNewsSource = newsSourceService.saveNewsSource(newsSource);
        return new ResponseEntity<>(savedNewsSource, HttpStatus.CREATED);
    }

    @Operation(summary= "Busca", description = "Retorna todos os portais de notícias cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Portais de notícias retornados com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao retornar portais de notícias"),
    })
    @GetMapping
    public ResponseEntity<List<NewsSource>> getAllNewsSources() {
        List<NewsSource> sources = newsSourceService.findAllNewsSources();
        return ResponseEntity.ok(sources);
    }

    @Operation(summary= "", description = "Busca um portal de notícias pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Portal de notícias retornado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao retornar portal de notícias"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsSource> getNewsSourceById(@PathVariable int id) {
        return newsSourceService.findNewsSourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary= "", description = "Atualiza um portal de notícias pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Portal de notícias atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao atualizar portal de notícias"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<NewsSource> updateNewsSource(@PathVariable int id, @RequestBody NewsSource newsSource) {
        return newsSourceService.updateNewsSourceById(id, newsSource);
    }

    @Operation(summary= "", description = "Deleta um portal de notícias pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Portal de notícias deletado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao deletar portal de notícias"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<NewsSource> deleteNewsSource(@PathVariable int id) {
        return newsSourceService.deleteNewsSourceById(id);
    }
}
