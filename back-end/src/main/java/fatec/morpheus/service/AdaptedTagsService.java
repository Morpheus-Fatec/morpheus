package fatec.morpheus.service;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional    
    public List<String> findVariationByText(List<String> listOfText) {
        // Criar tabela temporária
        String createTableSql = "CREATE TEMPORARY TABLE IF NOT EXISTS temp_strings (valor VARCHAR(255))";
        entityManager.createNativeQuery(createTableSql).executeUpdate();
    
        // Inserir valores na tabela temporária
        String insertSql = "INSERT INTO temp_strings (valor) VALUES (:valor)";
        for (String text : listOfText) {
            Query insertQuery = entityManager.createNativeQuery(insertSql);
            insertQuery.setParameter("valor", text);
            insertQuery.executeUpdate();
        }
    
        // Executar consulta para obter as variações
        String sqlQuery = "SELECT "
                + "COALESCE(REPLACE(ts.valor, t.text_description, t2.text_description), ts.valor) AS valor_com_variacao "
                + "FROM temp_strings ts "
                + "LEFT JOIN text t "
                + "  ON LOCATE(t.text_description, ts.valor) > 0 "
                + "LEFT JOIN synonymous s "
                + "  ON t.text_cod = s.text_cod "
                + "LEFT JOIN synonymous s1 "
                + "  ON s1.syn_group = s.syn_group "
                + "LEFT JOIN text t2 "
                + "  ON s1.text_cod = t2.text_cod";
    
        Query query = entityManager.createNativeQuery(sqlQuery);
        List<String> resultList = query.getResultList();
    
        // Remover tabela temporária
        String dropTableSql = "DROP TEMPORARY TABLE IF EXISTS temp_strings";
        entityManager.createNativeQuery(dropTableSql).executeUpdate();
    
        return resultList;
    }    
}
