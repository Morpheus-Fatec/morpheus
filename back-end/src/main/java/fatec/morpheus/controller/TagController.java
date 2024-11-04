package fatec.morpheus.controller;

import java.util.List;
import java.util.Optional;

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

import fatec.morpheus.entity.Tag;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@CrossOrigin("*")
@RestController
@RequestMapping("/morpheus/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    
    @Operation(summary= "", description = "Cria uma nova tag")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tag criada com sucesso"),
    @ApiResponse(responseCode = "400", description = "Erro ao criar tag"),
    })
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag){
        Tag createTag = tagService.createTag(tag);
        return ResponseEntity.ok(createTag);
    }

    @Operation(summary= "", description = "Retorna todas as tags cadastradas")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tags retornadas com sucesso"),
    @ApiResponse(responseCode = "400", description = "Erro ao retornar tags"),
    })
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags(){
        return ResponseEntity.ok(tagService.tagFindAll());
    }

    @Operation(summary= "", description = "Busca uma tag pelo nome")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tag retornada com sucesso"),
    @ApiResponse(responseCode = "400", description = "Erro ao retornar tag"),
    })
    @GetMapping("/tagName/{name}")
    public ResponseEntity<Tag> getTagByName(@PathVariable String name){
        Optional<Tag> tag = tagService.tagFindByName(name);
        return tag.map(ResponseEntity::ok)
                  .orElseThrow(() -> new NotFoundException(name, "tag"));
    }

    @Operation(summary= "", description = "Busca uma tag pelo ID")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tag retornada com sucesso"),
    @ApiResponse(responseCode = "400", description = "Erro ao retornar tag"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Integer id) {
        Optional<Tag> tag = tagService.tagFindById(id);
        return tag.map(ResponseEntity::ok).orElseThrow(() -> new NotFoundException(id, "Tag"));
    }
    
    @Operation(summary= "", description = "Atualiza uma tag")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tag atualizada com sucesso"),
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar tag"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTagEntity(@PathVariable Integer id, @RequestBody Tag tag){
        Optional<Tag> updateTag = tagService.updateTag(id, tag);
        return updateTag.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary= "", description = "Deleta uma tag pelo ID")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Tag deletada com sucesso"),
    @ApiResponse(responseCode = "400", description = "Erro ao deletar tag"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id){
        if (tagService.deleTag(id)) {
            return ResponseEntity.noContent().build();

        }else{
            return ResponseEntity.notFound().build();

        }
    }

}
