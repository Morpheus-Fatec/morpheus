package fatec.morpheus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.NewsSource;

@Repository
public interface NewsSourceRepository extends JpaRepository<NewsSource, Integer> {

    boolean existsBySrcName(String srcName);
    boolean existsByAddress(String address);

}
