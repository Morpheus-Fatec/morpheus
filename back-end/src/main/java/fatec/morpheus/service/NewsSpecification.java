package fatec.morpheus.service;

import java.time.LocalDate;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;

import fatec.morpheus.entity.News;
import jakarta.persistence.criteria.Predicate;

public class NewsSpecification {

    public static Specification<News> comFiltros(List<String> titles, List<String> contents, List<String> authors, List<String> portals, LocalDate dataStart, LocalDate dataEnd) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
    
            if (titles != null && !titles.isEmpty()) {
                Predicate titlePredicate = criteriaBuilder.disjunction();
                for (String title : titles) {
                    titlePredicate = criteriaBuilder.or(titlePredicate, criteriaBuilder.like(root.get("newsTitle"), "%" + title + "%"));
                }
                predicate = criteriaBuilder.and(predicate, titlePredicate);
            }
    
            if (contents != null && !contents.isEmpty()) {
                Predicate contentPredicate = criteriaBuilder.disjunction();
                for (String content : contents) {
                    contentPredicate = criteriaBuilder.or(contentPredicate, criteriaBuilder.like(root.get("newsContent"), "%" + content + "%"));
                }
                predicate = criteriaBuilder.and(predicate, contentPredicate);
            }
    
            if (authors != null && !authors.isEmpty()) {
                Predicate authorPredicate = criteriaBuilder.disjunction();
                for (String author : authors) {
                    authorPredicate = criteriaBuilder.or(authorPredicate, criteriaBuilder.like(root.join("newsAuthor").get("new_aut_name"), "%" + author + "%"));
                }
                predicate = criteriaBuilder.and(predicate, authorPredicate);
            }
    
            if (portals != null && !portals.isEmpty()) {
                Predicate sourcePredicate = criteriaBuilder.disjunction();
                for (String portal : portals) {
                    sourcePredicate = criteriaBuilder.or(sourcePredicate, criteriaBuilder.like(root.join("sourceNews").get("srcName"), "%" + portal + "%"));
                }
                predicate = criteriaBuilder.and(predicate, sourcePredicate);
            }
    
            if (dataStart != null && dataEnd != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("newsRegistryDate"), dataStart, dataEnd));
            }
    
            return predicate;
        };
    }
    
    

}
