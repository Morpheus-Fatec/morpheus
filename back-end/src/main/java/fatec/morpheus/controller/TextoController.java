package fatec.morpheus.controller;

import fatec.morpheus.entity.Texto;
import fatec.morpheus.service.TextoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/textos")
public class TextoController {

    @Autowired
    private TextoService textoService;


    @Operation(summary= "Metodo para criar um novo texto", description = "Cria um novo texto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Texto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar texto"),
    })
    @PostMapping("/createTextoWithSynonyms")
    public ResponseEntity<Texto> createTextoWithSynonyms(@RequestBody TextoRequest textoRequest) {
        Texto textoPrincipal = new Texto();
        textoPrincipal.setTextoDescription(textoRequest.getTextoDescription());
        textoPrincipal = textoService.saveTexto(textoPrincipal);

        List<Texto> synonyms = textoRequest.getSynonyms().stream()
            .map(synonymDescription -> {
                Texto synonym = new Texto();
                synonym.setTextoDescription(synonymDescription);
                return textoService.saveTexto(synonym);
            })
            .collect(Collectors.toList());

        textoPrincipal.setSynonyms(synonyms);
        textoPrincipal = textoService.saveTexto(textoPrincipal);

        return ResponseEntity.ok(textoPrincipal);
    }

    @Data
    public static class TextoRequest {
        private String textoDescription;
        private List<String> synonyms;
    }
}



