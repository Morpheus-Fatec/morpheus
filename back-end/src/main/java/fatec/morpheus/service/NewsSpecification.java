package fatec.morpheus.service;

import org.springframework.data.jpa.domain.Specification;

import fatec.morpheus.DTO.NewsSearchRequest;
import fatec.morpheus.entity.News;
import jakarta.persistence.criteria.Predicate;

public class NewsSpecification {

    public static Specification<News> withFilter(NewsSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Main AND
    
            // Title Search
            if (request.getTitleSearch() != null && !request.getTitleSearch().isEmpty()) {
                Predicate titlePredicate = criteriaBuilder.disjunction(); // OR for multiple titles
                for (String title : request.getTitleSearch()) {
                    titlePredicate = criteriaBuilder.or(titlePredicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("newsTitle")), "%" + title.toLowerCase() + "%"));
                }
                predicate = criteriaBuilder.and(predicate, titlePredicate);
            }
    
            Predicate textPredicate = criteriaBuilder.conjunction();
            if (request.getTextSearch() != null && !request.getTextSearch().isEmpty()) {
                for (String text : request.getTextSearch()) {
                    textPredicate = criteriaBuilder.or(textPredicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("newsContent")), "%" + text.toLowerCase() + "%"));
                }
            }
    
            // Author Filter
            if (request.getAuthor() != null && !request.getAuthor().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("newsAuthor").get("autId").in(request.getAuthor()));
            }
    
            // Sources Filter
            if (request.getSourcesOrigin() != null && !request.getSourcesOrigin().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("sourceNews").get("code").in(request.getSourcesOrigin()));
            }
    
            // Date Range Filter
            if (request.getDateStart() != null && request.getDateEnd() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.between(root.get("newsRegistryDate"), request.getDateStart(), request.getDateEnd()));
            }
    
            return predicate;
        };
    }
        
}

