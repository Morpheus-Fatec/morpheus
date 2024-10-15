package fatec.morpheus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer>{

    Page<News> findAll(Pageable Pegeable);
}
