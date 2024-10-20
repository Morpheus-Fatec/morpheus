package fatec.morpheus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.NewsAuthor;

@Repository
public interface NewsAuthorRepository extends JpaRepository<NewsAuthor, Integer>{
  NewsAuthor findByAutName(String autName);
}
