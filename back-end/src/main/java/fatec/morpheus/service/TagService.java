package fatec.morpheus.service;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.hibernate.boot.model.source.spi.PluralAttributeIndexNature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fatec.morpheus.dao.TagRepository;
import fatec.morpheus.entity.Tag;

@Service
public class TagService {

    @Autowired
    TagRepository tagRepository;


    public void saveTag(Tag tag){
        tagRepository.save(tag);
    }

    public Optional<Tag> tagFindById(Integer id) {
        return tagRepository.findById(id);
    }

    public Optional<Tag> tagFindByName(String name){
        return tagRepository.findByTagName(name);
    }   

    public List<Tag> tagFindAll() {
        return tagRepository.findAll();
    }

    public Optional<Tag> updateTag(Integer id, Tag updatedTag) {
        Optional<Tag> existingTag = tagRepository.findById(id);
        
        if (existingTag.isPresent()) {
            Tag tagToUpdate = existingTag.get();
            tagToUpdate.setTagName(updatedTag.getTagName());

            tagRepository.save(tagToUpdate);
            return Optional.of(tagToUpdate);
        } else {
            return Optional.empty();
        }
    }

    public Tag createTag(Tag tag){
        Tag savedTag = tagRepository.save(tag);
        return savedTag;

    }

    public boolean deleTag(Integer id){

        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    


}
