package fatec.morpheus.filters;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class FilterByText {
    @PersistenceContext
    private EntityManager entityManager;

    public List<String> getNoticesByText(String text){
        String sql = "SELECT "+
                     "new_content " +
                     "FROM news " +
                     "WHERE new_cont LIKE '%"+ text +"%'";

        Query query = entityManager.createNativeQuery(sql);

        List<String> notices = query.getResultList();

        return notices;    }
}
