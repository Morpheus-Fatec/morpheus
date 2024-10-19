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

    public List<String> findVariation(int idNewSource) {
        String sql = "SELECT " +
            "REPLACE(tg.tag_name, txA.texto_description, txB.texto_description) AS variacao " +
            "FROM Tag tg " +
            "JOIN synonymous tr " +
            "ON LOCATE((SELECT texto_description FROM Texto WHERE texto_cod = tr.texto_cod), tg.tag_name) > 0 " +
            "JOIN Texto txA ON txA.texto_cod = tr.texto_cod " +
            "JOIN Texto txB ON txB.texto_cod = tr.syn_group " +
            "JOIN source_tag ON source_tag.tag_cod = tg.tag_cod " +
            "WHERE source_tag.src_cod = '" + idNewSource + "' " +  // Correção de espaço aqui
            "UNION " +
            "SELECT tg.tag_name AS variacao " +
            "FROM tag tg " +
            "INNER JOIN source_tag src_tg ON tg.tag_cod = src_tg.tag_cod " +
            "WHERE src_tg.src_cod = '" + idNewSource + "';";

            // System.out.println(sql);
            // return null;
            Query query = entityManager.createNativeQuery(sql);
            List<String> resultList = query.getResultList();
    
            return resultList;
    }
}
