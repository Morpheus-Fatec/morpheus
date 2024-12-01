package fatec.morpheus.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

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
            "WHERE source_tag.src_cod = '" + idNewSource + "' " +
            "UNION " +
            "SELECT tg.tag_name AS variacao " +
            "FROM tag tg " +
            "INNER JOIN source_tag src_tg ON tg.tag_cod = src_tg.tag_cod " +
            "WHERE src_tg.src_cod = '" + idNewSource + "';";

            Query query = entityManager.createNativeQuery(sql);
            List<String> resultList = query.getResultList();
    
            return resultList;
    }

    @Transactional    
    public List<String> findVariationByText(List<String> filters) {
        String createTableSql = "CREATE TEMPORARY TABLE IF NOT EXISTS temp_filters (filter VARCHAR(255))";
        entityManager.createNativeQuery(createTableSql).executeUpdate();

        String insertSql = "INSERT INTO temp_filters (filter) VALUES (:filter)";
        for (String filterText : filters) {
            Query insertQuery = entityManager.createNativeQuery(insertSql);
            insertQuery.setParameter("filter", filterText);
            insertQuery.executeUpdate();
        }

        String sqlQuery = "SELECT "
            + "COALESCE(REPLACE(tf.filter, t.text_description, t2.text_description), tf.filter) AS filter_with_variation "
            + "FROM temp_filters tf "
            + "LEFT JOIN text t "
            + "  ON LOCATE(t.text_description, tf.filter) > 0 "
            + "LEFT JOIN synonymous s "
            + "  ON t.text_cod = s.text_cod "
            + "LEFT JOIN text t2 "
            + "  ON s.syn_group = t2.text_cod ";

        Query query = entityManager.createNativeQuery(sqlQuery);
        List<String> queryResults = query.getResultList();

        String dropTableSql = "DROP TEMPORARY TABLE IF EXISTS temp_filters";
        entityManager.createNativeQuery(dropTableSql).executeUpdate();

        Set<String> resultSet = new LinkedHashSet<>(queryResults);
        resultSet.addAll(filters);

        return new ArrayList<>(resultSet);
    }

}
