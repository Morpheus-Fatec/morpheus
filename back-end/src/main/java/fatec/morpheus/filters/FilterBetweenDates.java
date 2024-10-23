package fatec.morpheus.filters;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class FilterBetweenDates {
    @PersistenceContext
    private EntityManager entityManager;

    public List<String> getNoticesBetweenDates(String inicialDate, String endDate){
        String sql = "SELECT " +
                     "new_content "+
                     "FROM news " +
                     "WHERE new_cont BETWEEN '"+ inicialDate +"' AND '"+ endDate +"'";

        Query query = entityManager.createNativeQuery(sql);

        List<String> notices = query.getResultList();

        return notices;
    }


}
