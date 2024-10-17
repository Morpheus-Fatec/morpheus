package fatec.morpheus.repository;

import fatec.morpheus.entity.Synonymous;
import fatec.morpheus.entity.SynonymousId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SynonymousRepository extends JpaRepository<Synonymous, SynonymousId> {
    void deleteByTextoCod(Integer textoCod);
}