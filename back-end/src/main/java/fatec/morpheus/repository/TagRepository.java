package fatec.morpheus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fatec.morpheus.entity.Tag;

import java.util.List;
import java.util.Optional;


public interface TagRepository extends JpaRepository<Tag, Integer>{

    Optional<Tag> findByTagName(String name);

    @Query("SELECT t.tagName FROM Tag t " +
    "JOIN TagRelFont trf ON t.tagCode = trf.tagId.tagCode " +
    "WHERE trf.apiId.code = :apiCode")
    List<String> findTagsByApiId(int apiCode);

    @Query("SELECT t FROM Tag t WHERE t.tagName IN :tagNames")
    List<Tag> findByTagNameIn(List<String> tagNames);

}
