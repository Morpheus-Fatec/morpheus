package fatec.morpheus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.Texto;

@Repository
public interface TextoRepository extends JpaRepository<Texto, Integer> {
}