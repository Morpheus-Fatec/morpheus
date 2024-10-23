package fatec.morpheus.filters;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class FilterByTitle {
    @PersistenceContext
    private EntityManager entityManager;

    public List<String> getNoticesByTitle(List<String> newTitle){
        String titles = newTitle.stream()
                                .map(code -> "'" + code + "'")
                                .collect(Collectors.joining(", "));
        String sql = "SELECT " + 
        "n.new_content " +
        "FROM news n " +
        "WHERE new_title IN ("+ titles +")";

        Query query = entityManager.createNativeQuery(sql);

        List<String> notices = query.getResultList();

        return notices;
    }
}
