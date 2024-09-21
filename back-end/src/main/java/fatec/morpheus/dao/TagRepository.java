package fatec.morpheus.dao;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PutMapping;

import fatec.morpheus.entity.Tag;
import java.util.List;
import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag, Integer>{

    Optional<Tag> findByTagName(String name);


}
