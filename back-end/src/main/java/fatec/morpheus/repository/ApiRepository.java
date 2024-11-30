package fatec.morpheus.repository;

import fatec.morpheus.entity.Api;
import io.micrometer.common.lang.NonNull;

import org.springframework.data.domain.Page;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<Api, Integer>, JpaSpecificationExecutor<Api> {
    boolean existsByName(String name);
    boolean existsByAddress(String address);
    Api findByCode(int code);
    Page<Api> findAll(@NonNull Pageable pageable);
}
