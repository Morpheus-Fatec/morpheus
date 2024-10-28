package fatec.morpheus.service;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import fatec.morpheus.entity.News;
import jakarta.persistence.criteria.Predicate;

public class NewsSpecification {

    public static Specification<News> comFiltros(String titulo, String conteudo, String autor, String portal, LocalDate dataInicio, LocalDate dataFim) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (titulo != null && !titulo.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("newsTitle"), "%" + titulo + "%"));
            }

            if (conteudo != null && !conteudo.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("newsContent"), "%" + conteudo + "%"));
            }

            if (autor != null && !autor.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("newsAuthor").get("autName"), "%" + autor + "%"));
            }

            if (portal != null && !portal.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("sourceNews").get("srcName"), "%" + portal + "%"));
            }

            if (dataInicio != null && dataFim != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("newsRegistryDate"), dataInicio, dataFim));
            }

            return predicate;
        };
    }
}
