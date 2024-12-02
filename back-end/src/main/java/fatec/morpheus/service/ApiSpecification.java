package fatec.morpheus.service;

import org.springframework.data.jpa.domain.Specification;

import fatec.morpheus.DTO.ApiFilterRequestDTO;
import fatec.morpheus.entity.ApiContent;
import jakarta.persistence.criteria.Predicate;

public class ApiSpecification {

    public static Specification<ApiContent> withFilter(ApiFilterRequestDTO request) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.getTags() != null && !request.getTags().isEmpty()) {
                Predicate titlePredicate = criteriaBuilder.disjunction();
                for (String title : request.getTags()) {
                    titlePredicate = criteriaBuilder.or(titlePredicate,
                        criteriaBuilder.like(root.get("apiContent"), "%" + title + "%"));
                }
                predicate = criteriaBuilder.and(predicate, titlePredicate);
            }
            
            if (request.getText() != null && !request.getText().isEmpty()) {
                Predicate textPredicate = criteriaBuilder.disjunction();
                for (String text : request.getText()) {
                    textPredicate = criteriaBuilder.or(textPredicate,
                        criteriaBuilder.like(root.get("apiContent"), "%" + text + "%"));
                }
                predicate = criteriaBuilder.and(predicate, textPredicate);
            }
            

            if (request.getCode() != null && !request.getCode().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("apiId").get("code").in(request.getCode()));
            }

            if (request.getDateStart() != null && request.getDateEnd() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.between(root.get("date"), request.getDateStart(), request.getDateEnd()));
            }

            return predicate;
        };
    }
}
