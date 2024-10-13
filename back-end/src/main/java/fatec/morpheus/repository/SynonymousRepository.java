package fatec.morpheus.repository;

import fatec.morpheus.entity.Synonymous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SynonymousRepository extends JpaRepository<Synonymous, Integer> {
}