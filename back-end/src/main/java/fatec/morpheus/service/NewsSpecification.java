package fatec.morpheus.service;

import org.springframework.data.jpa.domain.Specification;

import fatec.morpheus.DTO.NewsSearchRequest;
import fatec.morpheus.entity.News;
import jakarta.persistence.criteria.Predicate;

public class NewsSpecification {

    public static Specification<News> withFilter(NewsSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
    
            if (request.getTitleSearch() != null && !request.getTitleSearch().isEmpty()) {
                Predicate titlePredicate = criteriaBuilder.disjunction();
                for (String title : request.getTitleSearch()) {
                    titlePredicate = criteriaBuilder.or(titlePredicate, criteriaBuilder.like(root.get("newsTitle"), "%" + title + "%"));
                }
                predicate = criteriaBuilder.and(predicate, titlePredicate);
            }
    
            if (request.getTextSearch() != null && !request.getTextSearch().isEmpty()) {
                Predicate contentPredicate = criteriaBuilder.disjunction();
                for (String content : request.getTextSearch()) {
                    contentPredicate = criteriaBuilder.or(contentPredicate, criteriaBuilder.like(root.get("newsContent"), "%" + content + "%"));
                }
                predicate = criteriaBuilder.and(predicate, contentPredicate);
            }
    
            if (request.getAuthor() != null && !request.getAuthor().isEmpty()) {
                Predicate authorPredicate = criteriaBuilder.disjunction();
                for (String authorName : request.getAuthor()) {
                    authorPredicate = criteriaBuilder.or(authorPredicate, criteriaBuilder.like(root.join("newsAuthor").get("new_aut_name"), "%" + authorName + "%")); // Ajuste para o campo correto
                }
                predicate = criteriaBuilder.and(predicate, authorPredicate);
            }
    
            if (request.getSourcesOrigin() != null && !request.getSourcesOrigin().isEmpty()) {
                Predicate sourcePredicate = criteriaBuilder.disjunction();
                for (String sourceName : request.getSourcesOrigin()) {
                    sourcePredicate = criteriaBuilder.or(sourcePredicate, criteriaBuilder.like(root.join("sourceNews").get("srcName"), "%" + sourceName + "%")); // Ajuste para o campo correto
                }
                predicate = criteriaBuilder.and(predicate, sourcePredicate);
            }
    
            if (request.getDateStart() != null && request.getDateEnd() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("newsRegistryDate"), request.getDateStart(), request.getDateEnd()));
            }
    
            return predicate;
        };
    }
    
}

