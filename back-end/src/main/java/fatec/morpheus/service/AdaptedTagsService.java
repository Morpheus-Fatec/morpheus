package fatec.morpheus.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class AdaptedTagsService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<String> findVariation(int idNewSource) {
        String sql = "SELECT " +
            "REPLACE(tg.tag_name, txA.text_description, txB.text_description) AS variacao " +
            "FROM Tag tg " +
            "JOIN synonymous tr " +
            "ON LOCATE((SELECT text_description FROM Text WHERE text_cod = tr.text_cod), tg.tag_name) > 0 " +
            "JOIN Text txA ON txA.text_cod = tr.text_cod " +
            "JOIN Text txB ON txB.text_cod = tr.syn_group " +
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
