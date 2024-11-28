package fatec.morpheus.service;

import org.springframework.data.jpa.domain.Specification;

import fatec.morpheus.DTO.ApiSearchRequest;
import fatec.morpheus.entity.Api;
import jakarta.persistence.criteria.Predicate;

public class ApiSpecification {

    public static Specification<Api> withFilter(ApiSearchRequest request) {
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
                System.out.println("Entrou no textSearch" + request.getTextSearch().size());
                Predicate textPredicate = criteriaBuilder.disjunction();
                for (String text : request.getTextSearch()) {
                    textPredicate = criteriaBuilder.or(textPredicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("newsContent")), "%" + text.toLowerCase() + "%"));
                }
                predicate = criteriaBuilder.and(predicate, textPredicate);
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
