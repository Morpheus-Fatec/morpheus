package fatec.morpheus.service;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

@Service
public class AdaptedTagsService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<String> findVariacao() {
        String sql = "SELECT " +
                    "REPLACE(tg.tag_name, txA.texto_description, txB.texto_description) AS variacao " +
                    "FROM " +
                    "Tag tg " +
                    "JOIN " +
                    "synonymous tr " +
                    "ON LOCATE((SELECT texto_description FROM Texto WHERE texto_cod = tr.texto_cod), tg.tag_name) > 0 " +
                    "JOIN " +
                    "Texto txA ON txA.texto_cod = tr.texto_cod " +
                    "JOIN " +
                    "Texto txB ON txB.texto_cod = tr.syn_group " +
                    "UNION " +
                    "SELECT tag_name AS variacao " +
                    "FROM Tag";

        Query query = entityManager.createNativeQuery(sql);
        List<String> resultList = query.getResultList();

        return resultList;
    }
}
