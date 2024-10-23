package fatec.morpheus.filters;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class FilterByAuthor {
    @PersistenceContext
    private EntityManager entityManager;

    public List<String> getNoticesByAuthor(List<Integer> authorCode){
        String author = authorCode.stream()
                                  .map(String::valueOf)
                                  .collect(Collectors.joining(", "));

        String sql = "SELECT " + 
                     "a.new_aut_name "+
                     "FROM news n " + 
                     "LEFT JOIN news_author a "+
                     "ON a.new_aut_id = n.new_aut_cod "+
                     "WHERE new_aut_id IN ("+ author +")";

        Query query = entityManager.createNativeQuery(sql);
        List<String> notices = query.getResultList();

        return notices;

    }

}
