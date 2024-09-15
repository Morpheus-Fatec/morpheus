package fatec.morpheus.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

import fatec.morpheus.dao.ITag;
import fatec.morpheus.entity.CreateTag;

@CrossOrigin("*")
@RestController
@RequestMapping("/morpheus/tag")
public class CreateTagController {

    @Autowired
    private ITag tagDao;

    @GetMapping
    public List<CreateTag> listTag(){
        return (List<CreateTag>) tagDao.findAll();
    }
    @PostMapping
    public ResponseEntity<List<CreateTag>> createTags(@RequestBody List<CreateTag> tags) {
       
        List<CreateTag> savedTags = (List<CreateTag>) tagDao.saveAll(tags);
        return ResponseEntity.ok(savedTags);
    }

    @PutMapping
    public ResponseEntity<List<CreateTag>> updateTags(@RequestBody List<CreateTag> tags) {
        
        List<CreateTag> updatedTags = (List<CreateTag>) tagDao.saveAll(tags);
        return ResponseEntity.ok(updatedTags);
    }

    @DeleteMapping("/{tagCod}")
    public Optional<CreateTag> deleteTag(@PathVariable Integer tagCod){
        Optional<CreateTag> tagName = tagDao.findById(tagCod);
        tagDao.deleteById(tagCod);
        return tagName;

    }

}
