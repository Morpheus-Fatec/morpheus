package fatec.morpheus.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.DTO.TextDTO;
import fatec.morpheus.entity.Synonymous;
import fatec.morpheus.entity.Text;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.service.SynonymousService;
import fatec.morpheus.service.TextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/texts")
public class TextController {

    @Autowired
    private TextService textService;

    @Autowired
    private SynonymousService synonymousService;


    @Operation(summary = "Metodo para criar um novo texto", description = "Cria um novo texto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Texto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao criar texto"),
    })
    @PostMapping
    public ResponseEntity<Text> saveTexo(@RequestBody Text texto) {
        return ResponseEntity.ok(textService.saveText(texto));
    }


    @Operation(summary = "Metodo para encontrar um texto pelo ID", description = "Encontra um texto pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Texto encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Texto não encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Text> getTextById(@PathVariable Integer id) {
        return textService.findTextById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException(id, "Texto"));
    }

    @Operation(summary = "Metodo para listar todos os textos com seus sinonimos", description = "Lista todos os textos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Textos encontrados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum texto encontrado"),
    })
    @GetMapping
    public ResponseEntity<List<TextDTO>> getAllTexts() {
        List<Text> texts = textService.findAllTexts();
        if (texts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<TextDTO> textDTOs = texts.stream().map(text -> {
                List<Integer> synonyms = textService.findSynonymsByTextId(text.getTextCode());
                return new TextDTO(text.getTextDescription(), text.getTextCode(), synonyms);
            }).collect(Collectors.toList());
            return ResponseEntity.ok(textDTOs);
        }
    }


    @Operation(summary = "Metodo para atualizar um texto pelo ID e ligar os sinonimos", description = "Atualiza um texto pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Texto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Texto não encontrado"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Text> updateTextById(@PathVariable Integer id, @RequestBody TextService.TextUpdateRequest request) {
        return textService.findTextById(id)
                .map(existingText -> {
                    existingText.setTextDescription(request.getTextoDescription());
                    Text updatedText = textService.saveText(existingText);

                    synonymousService.deleteRelationById(id);
    
                    for (Integer synonymId : request.getSynonymIds()) {
                        Synonymous synonymous = new Synonymous();
                        synonymous.setTextCode(id);
                        synonymous.setSynGroup(synonymId);
                        synonymousService.saveSynonymous(synonymous);
                    }
    
                    return ResponseEntity.ok(updatedText);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Metodo para deletar um texto pelo ID", description = "Deleta um texto pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Texto deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Texto não encontrado"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteText(@PathVariable Integer id) {
        return textService.findTextById(id)
                .map(existingText -> {
                    textService.deleteTextAndRelations(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Metodo para criar um sinônimo entre dois textos", description = "Cria um sinônimo entre dois textos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sinônimo criado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Texto não encontrado"),
    })
    @PostMapping("/{id1}/synonym/{id2}")
    public ResponseEntity<Synonymous> createSynonym(@PathVariable Integer id1, @PathVariable Integer id2) {
        Text text1 = textService.findTextById(id1).orElse(null);
        Text text2 = textService.findTextById(id2).orElse(null);
    
        if (text1 == null || text2 == null) {
            return ResponseEntity.notFound().build();
        }
    
        Synonymous synonymous = new Synonymous();
        synonymous.setTextCode(text1.getTextCode());
        synonymous.setSynGroup(text2.getTextCode());
    
        synonymousService.saveSynonymous(synonymous);
    
        return ResponseEntity.ok(synonymous);
    }


    @Operation(summary = "Metodo para listar todos os sinônimos", description = "Lista todos os sinônimos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sinônimos encontrados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum sinônimo encontrado"),
    })
    @GetMapping("/synonyms")
    public ResponseEntity<List<Synonymous>> getAllSynonyms() {
        List<Synonymous> synonyms = synonymousService.findAllSynonymous();
        if (synonyms.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(synonyms);
        }
    }
}