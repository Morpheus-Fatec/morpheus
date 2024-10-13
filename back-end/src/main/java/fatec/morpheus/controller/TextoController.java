package fatec.morpheus.controller;

import fatec.morpheus.entity.Texto;
import fatec.morpheus.service.TextoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
    @PostMapping
    public ResponseEntity<Texto> saveTexto(@RequestBody Texto texto) {
        return ResponseEntity.ok(textoService.saveTexto(texto));
    }
}



