package fatec.morpheus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.Api;
import fatec.morpheus.entity.ApiContent;

@Repository
public interface ApiContentRepository extends JpaRepository<ApiContent, Long> {

    Optional<ApiContent> findByApiIdAndApiAddressAndDate(Api api, String apiAddress, String date);
}
