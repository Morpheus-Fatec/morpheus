package fatec.morpheus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fatec.morpheus.entity.Tag;
import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag, Integer>{

    Optional<Tag> findByTagName(String name);


}
