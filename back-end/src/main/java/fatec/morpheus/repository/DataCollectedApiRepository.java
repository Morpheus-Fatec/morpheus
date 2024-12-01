package fatec.morpheus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fatec.morpheus.entity.DataCollectedApi;

@Repository
public interface DataCollectedApiRepository extends JpaRepository<DataCollectedApi, Integer> {

    DataCollectedApi findFirstByApi_Code(Integer apiCode);

}