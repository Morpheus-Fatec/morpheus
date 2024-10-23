package fatec.morpheus.filters;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class FilterBySource {
    @PersistenceContext
    private EntityManager entityManager;

    public List<String> getNoticesBySourceCode(List<Integer> newSourcesCodes){
        String sources = newSourcesCodes.stream()
                                     .map(String::valueOf)
                                     .collect(Collectors.joining(", "));
        String sql = "SELECT n.new_content " +
                     "FROM news n " +
                     "WHERE n.new_src_cod IN (" + sources + ")";

        Query query = entityManager.createNativeQuery(sql);

        List<String> notices = query.getResultList();

        return notices;
    }
}
