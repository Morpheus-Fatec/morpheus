package fatec.morpheus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.Synonymous;

@Repository
public interface SynonymousRepository extends JpaRepository<Synonymous, fatec.morpheus.entity.SynonymousId> {
    void deleteByText_TextCode(Integer textCode);
    void deleteBySynGroup(Integer synGroup);
    List<Synonymous> findByTextCodeOrSynGroup(Integer textCode, Integer synGroup);
}