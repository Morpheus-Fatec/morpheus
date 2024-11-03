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
                    titlePredicate = criteriaBuilder.or(titlePredicate, criteriaBuilder.like(root.get("new_title"), "%" + title + "%"));
                }
                predicate = criteriaBuilder.and(predicate, titlePredicate);
            }
    
            if (request.getTextSearch() != null && !request.getTextSearch().isEmpty()) {
                Predicate contentPredicate = criteriaBuilder.disjunction();
                for (String content : request.getTextSearch()) {
                    contentPredicate = criteriaBuilder.or(contentPredicate, criteriaBuilder.like(root.get("new_content"), "%" + content + "%"));
                }
                predicate = criteriaBuilder.and(predicate, contentPredicate);
            }
    
            if (request.getAuthor().length != 0) {
                Predicate authorPredicate = criteriaBuilder.disjunction();
                for (int authorId : request.getAuthor()) {
                    authorPredicate = criteriaBuilder.or(authorPredicate, criteriaBuilder.equal(root.join("newsAuthor").get("new_aut_id"), authorId));
                }
                predicate = criteriaBuilder.and(predicate, authorPredicate);
            }
    
            if (request.getSourcesOrigin().length !=0) {
                Predicate sourcePredicate = criteriaBuilder.disjunction();
                for (int sourceId : request.getSourcesOrigin()) {
                    sourcePredicate = criteriaBuilder.or(sourcePredicate, criteriaBuilder.equal(root.join("sourceNews").get("code"), sourceId));
                }
                predicate = criteriaBuilder.and(predicate, sourcePredicate);
            }
    
            if (request.getDateStart() != null && request.getDateEnd() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("src_registry_date"), request.getDateStart(), request.getDateEnd()));
            }
    
            return predicate;
        };
    }
    
}

