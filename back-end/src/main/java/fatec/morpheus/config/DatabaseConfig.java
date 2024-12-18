package fatec.morpheus.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
public class DatabaseConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void init() {
        transactionTemplate.execute(status -> {

            @SuppressWarnings("unchecked")
            List<String> foreignKeysSourceTag = (List<String>) entityManager.createNativeQuery(
                "SELECT CONSTRAINT_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE TABLE_NAME = 'source_tag' AND COLUMN_NAME = 'tag_cod' AND CONSTRAINT_SCHEMA = DATABASE()"
            ).getResultList();

            for (String foreignKey : foreignKeysSourceTag) {
                entityManager.createNativeQuery("ALTER TABLE source_tag DROP FOREIGN KEY " + foreignKey).executeUpdate();
            }

            entityManager.createNativeQuery(
                "ALTER TABLE source_tag ADD CONSTRAINT FK_tag_cod_source_tag FOREIGN KEY (tag_cod) REFERENCES tag(tag_cod) ON DELETE CASCADE"
            ).executeUpdate();

            @SuppressWarnings("unchecked")
            List<String> foreignKeysNews = (List<String>) entityManager.createNativeQuery(
                "SELECT CONSTRAINT_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE TABLE_NAME = 'news' AND COLUMN_NAME = 'new_src_cod' AND CONSTRAINT_SCHEMA = DATABASE()"
            ).getResultList();

            for (String foreignKey : foreignKeysNews) {
                entityManager.createNativeQuery("ALTER TABLE news DROP FOREIGN KEY " + foreignKey).executeUpdate();
            }

            entityManager.createNativeQuery(
                "ALTER TABLE news ADD CONSTRAINT FK_new_src_cod_news FOREIGN KEY (new_src_cod) REFERENCES source(src_cod) ON DELETE SET NULL"
            ).executeUpdate();

            @SuppressWarnings("unchecked")
            List<String> foreignKeysDataCollectedApi = (List<String>) entityManager.createNativeQuery(
                "SELECT CONSTRAINT_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE TABLE_NAME = 'data_collected_api' AND COLUMN_NAME = 'api_cod' AND CONSTRAINT_SCHEMA = DATABASE()"
            ).getResultList();

            for (String foreignKey : foreignKeysDataCollectedApi) {
                entityManager.createNativeQuery("ALTER TABLE data_collected_api DROP FOREIGN KEY " + foreignKey).executeUpdate();
            }

            entityManager.createNativeQuery(
                "ALTER TABLE data_collected_api ADD CONSTRAINT FK_api_cod_data_collected_api FOREIGN KEY (api_cod) REFERENCES api(api_cod) ON DELETE CASCADE"
            ).executeUpdate();

            return null;
        });
    }
}