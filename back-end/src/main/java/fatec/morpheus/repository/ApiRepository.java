package fatec.morpheus.repository;

import fatec.morpheus.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<Api, Integer> {
    boolean existsByAddress(String address);
    Api findByCode(int code);
}
