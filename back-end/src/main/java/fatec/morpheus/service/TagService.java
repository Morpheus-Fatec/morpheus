package fatec.morpheus.service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.Tag;
import fatec.morpheus.repository.TagRepository;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @PersistenceContext
    private EntityManager entityManager;

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
        } else {
            return false;
        }
    }


    public List findTagWithSource(List<String> newsSource) {
        String inClause = "("+String.join(",", newsSource.stream()
                .map(source -> "'" + source + "'")
                .toArray(String[]::new))+")";

        String sql = "select t.tag_cod, tag_name, (select count(nt.new_cod) " +
                "from news_tag nt " +
                "inner join source_tag st on st.src_tag_cod = nt.src_tag_cod " +
                "where st.src_cod in "+inClause+" ) as quant " +
                "from source_tag st " +
                "inner join tag t on t.tag_cod = st.tag_cod " +
                "where st.src_cod in "+inClause;
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

}
