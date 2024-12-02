package fatec.morpheus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.News;
import io.micrometer.common.lang.NonNull;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer>, JpaSpecificationExecutor<News> {

    Page<News> findAll(@NonNull Pageable pageable);
    boolean existsByNewAddress(String newAddress);
}
