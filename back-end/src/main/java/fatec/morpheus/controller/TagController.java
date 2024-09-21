package fatec.morpheus.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.text.html.Option;

import org.apache.catalina.connector.Request;
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

import fatec.morpheus.dao.TagRepository;
import fatec.morpheus.entity.Tag;
import fatec.morpheus.service.TagService;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("*")
@RestController
@RequestMapping("/morpheus/tag")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag){
        Tag createTag = tagService.createTag(tag);
        return ResponseEntity.ok(createTag);
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags(){  
        return ResponseEntity.ok(tagService.tagFindAll());
    }

    @GetMapping("/tagName/{name}")
    public ResponseEntity<Tag> getTagByName(@PathVariable String name){
        Optional<Tag> tag = tagService.tagFindByName(name);
        return tag.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Integer id) {
        Optional<Tag> tag = tagService.tagFindById(id);
        return tag.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTagEntity(@PathVariable Integer id, @RequestBody Tag tag){
        Optional<Tag> updateTag = tagService.updateTag(id, tag);
        return updateTag.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id){
        if (tagService.deleTag(id)) {
            return ResponseEntity.noContent().build();

        }else{
            return ResponseEntity.notFound().build();

        }
    }

}
