package fatec.morpheus.controller;

import fatec.morpheus.entity.Texto;
import fatec.morpheus.service.TextoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
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


    @Operation(summary = "Metodo para encontrar um texto pelo ID", description = "Encontra um texto pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Texto encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Texto não encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Texto> getTextoById(@PathVariable Integer id) {
        return textoService.findTextoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Metodo para listar todos os textos", description = "Lista todos os textos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Textos encontrados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum texto encontrado"),
    })
    @GetMapping
    public ResponseEntity<List<Texto>> getAllTextos() {
        List<Texto> textos = textoService.findAllTextos();
        if (textos.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(textos);
        }
    }


    @Operation(summary = "Metodo para atualizar um texto pelo ID", description = "Atualiza um texto pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Texto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Texto não encontrado"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Texto> updateTextoById(@PathVariable Integer id, @RequestBody Texto texto) {
        return textoService.findTextoById(id)
                .map(existingTexto -> {
                    existingTexto.setTextoDescription(texto.getTextoDescription());
                    Texto updatedTexto = textoService.saveTexto(existingTexto);
                    return ResponseEntity.ok(updatedTexto);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Metodo para deletar um texto pelo ID", description = "Deleta um texto pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Texto deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Texto não encontrado"),
})
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteTexto(@PathVariable Integer id) {
    return textoService.findTextoById(id)
            .map(existingTexto -> {
                textoService.deleteTexto(id);
                return ResponseEntity.ok().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
}

}



