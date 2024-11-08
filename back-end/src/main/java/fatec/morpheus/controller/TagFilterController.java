package fatec.morpheus.controller;

import fatec.morpheus.DTO.TagDTO;
import fatec.morpheus.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/morpheus/tag")
public class TagFilterController {

    @Autowired
    private TagService tagService;
    @Operation(summary = "Metodo para filtrar tags e quantidade de notícias por portais", description = "Filtra tags e quantidade de notícias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tags filtradas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao filtrar tags"),
    })
    @PostMapping("/search")
    public ResponseEntity<?> filterTags(@RequestBody List<String> newsSource) {
        if (newsSource == null || newsSource.isEmpty()) {
            return ResponseEntity.badRequest().body("A lista de portais não pode estar vazia.");
        }

        List<Object[]> tags = tagService.findTagWithSource(newsSource);

        List<Map<String, Object>> tagsList = tags.stream()
                .map(tag -> Map.of(
                        "tagCod", tag[0],
                        "tagNome", tag[1],
                        "newsCount", tag[2]
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(tagsList);
    }
}
