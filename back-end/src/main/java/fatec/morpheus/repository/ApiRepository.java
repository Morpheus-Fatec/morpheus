package fatec.morpheus.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.Api;
import io.micrometer.common.lang.NonNull;

@Repository
public interface ApiRepository extends JpaRepository<Api, Integer>, JpaSpecificationExecutor<Api> {
    Page<Api> findAll(@NonNull Pageable pageable);
}
