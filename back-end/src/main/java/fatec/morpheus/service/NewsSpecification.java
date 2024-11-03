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
                    titlePredicate = criteriaBuilder.or(titlePredicate, 
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("newsTitle")), "%" + title.toLowerCase() + "%"));
                }
                predicate = criteriaBuilder.and(predicate, titlePredicate);
            }

            if (request.getTextSearch() != null && !request.getTextSearch().isEmpty()) {
                Predicate contentPredicate = criteriaBuilder.disjunction();
                for (String content : request.getTextSearch()) {
                    contentPredicate = criteriaBuilder.or(contentPredicate, 
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("newsContent")), "%" + content.toLowerCase() + "%"));
                }
                predicate = criteriaBuilder.and(predicate, contentPredicate);
            }

            if (request.getAuthor() != null && !request.getAuthor().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("newsAuthor").get("autId").in(request.getAuthor()));
            }

            if (request.getSourcesOrigin() != null && !request.getSourcesOrigin().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("sourceNews").get("code").in(request.getSourcesOrigin()));
            }

            if (request.getDateStart() != null && request.getDateEnd() != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.between(root.get("newsRegistryDate"), request.getDateStart(), request.getDateEnd()));
            }

            return predicate;
        };
    }
    
}

