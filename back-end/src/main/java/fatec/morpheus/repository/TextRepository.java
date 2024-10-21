package fatec.morpheus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.Text;

@Repository
public interface TextRepository extends JpaRepository<Text, Integer> {
}