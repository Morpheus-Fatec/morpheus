package fatec.morpheus.service;

import fatec.morpheus.DTO.ApiDTO;
import fatec.morpheus.entity.Api;
import fatec.morpheus.entity.ErrorResponse;
import fatec.morpheus.exception.InvalidFieldException;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.exception.UniqueConstraintViolationException;
import fatec.morpheus.repository.ApiRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApiService {

    @Autowired
    private ApiRepository apiRepository;
    @Autowired
    private Validator validator;

    public Api createApi(ApiDTO apiCreatedDTO) {
        Api api = new Api();

        api.setAddress(apiCreatedDTO.getAddress());
        api.setGet(apiCreatedDTO.getGet());
        api.setPost(apiCreatedDTO.getPost());
        api.setTagCodes(apiCreatedDTO.getTagCodes());

        Set<ConstraintViolation<Api>> sourceViolations = validator.validate(api);
        if (!sourceViolations.isEmpty()) {
            List<String> errors = sourceViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
            errorResponse.setMessage("Problemas com campos obrigatórios");
            throw new InvalidFieldException(errorResponse);
        }


        try {
            apiRepository.save(api);
            return api;

        } catch (Exception e) {
            List<String> duplicateFields = this.verifyUniqueKeys(api);

            if (duplicateFields.isEmpty()) {
                String errorMessage = e.getCause().getMessage();
                ErrorResponse errorResponse = new ErrorResponse(
                        HttpStatus.CONFLICT,
                        duplicateFields,
                        errorMessage
                );
                throw new UniqueConstraintViolationException(errorResponse);
            }

            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.CONFLICT,
                    duplicateFields,
                    "Campos Duplicados"
            );

            throw new UniqueConstraintViolationException(errorResponse);
        }
    }

    private List<String> verifyUniqueKeys(Api api) {
        List<String> duplicateFields = new ArrayList<>();
        if (apiRepository.existsByAddress(api.getAddress())) {
            duplicateFields.add("address");
        }
        return duplicateFields;
    }

    public List<Api> findAllApi() {
        return apiRepository.findAll();
    }

    public Api findApiById(int id) {
        return apiRepository.findById(id).orElseThrow(() -> new NotFoundException(id, "API"));
    }

    public Api updateApiById(int id, Api apiToUpdate) {
        try {
            return apiRepository.findById(id)
                    .map(existingApi -> {
                        existingApi.setAddress(apiToUpdate.getAddress());
                        existingApi.setGet(apiToUpdate.getGet());
                        existingApi.setPost(apiToUpdate.getPost());
                        return apiRepository.save(existingApi);
                    })
                    .orElseThrow(() -> new NotFoundException(id, "API"));
        } catch (DataIntegrityViolationException e) {
            List<String> duplicateFields = this.verifyUniqueKeys(apiToUpdate);

            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.CONFLICT,
                    duplicateFields
            );
            throw new UniqueConstraintViolationException(errorResponse);
        }
    }

    public Api deleteApiById(int id) {
        Api api = apiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "Fonte de Notícia"));
        apiRepository.delete(api);
        return api;
    }

}
