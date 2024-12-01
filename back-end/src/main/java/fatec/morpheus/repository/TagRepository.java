package fatec.morpheus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fatec.morpheus.entity.Tag;


public interface TagRepository extends JpaRepository<Tag, Integer>{
    
    @Query("SELECT t.tagName FROM Tag t " +
    "JOIN TagRelFont trf ON t.tagCode = trf.tagId.tagCode " +
    "WHERE trf.apiId.code = :apiCode")
    List<String> findTagsByApiId(int apiCode);

    List<Tag> findByTagNameIn(List<String> tagCodes);

    @Query("SELECT t.tagName FROM Tag t JOIN TagRelFont trf ON trf.tagId = t WHERE trf.apiId.code = :apiId")
    List<String> findTagsByApi(@Param("apiId") int apiId);

    Tag findByTagName(String tagName);

}
