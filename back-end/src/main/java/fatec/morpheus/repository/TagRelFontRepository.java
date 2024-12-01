package fatec.morpheus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.Api;
import fatec.morpheus.entity.TagRelFont;
@Repository
public interface TagRelFontRepository extends JpaRepository<TagRelFont, Integer>{

    void deleteByApiId(Api api);

}
