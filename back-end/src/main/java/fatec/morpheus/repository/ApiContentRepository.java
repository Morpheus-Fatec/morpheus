package fatec.morpheus.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.Api;
import fatec.morpheus.entity.ApiContent;
import io.micrometer.common.lang.NonNull;

@Repository
public interface ApiContentRepository extends JpaRepository<ApiContent, Integer>, JpaSpecificationExecutor<ApiContent> {
   Optional<ApiContent> findByApiIdAndApiAddressAndMethodAndDate(Api api, String apiAddress, String method, LocalDate date);
   ApiContent findFirstByApiId(Api api);
   Page<ApiContent> findAll(@NonNull Pageable pageable);
}

