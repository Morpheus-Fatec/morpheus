package fatec.morpheus.dao;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PutMapping;

import fatec.morpheus.entity.CreateTag;
import java.util.List;


public interface ITag extends CrudRepository<CreateTag, Integer>{

    List<CreateTag> findByTagName(String tagName);


    
}
