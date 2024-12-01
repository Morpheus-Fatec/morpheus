package fatec.morpheus.service;

import fatec.morpheus.DTO.ApiDTO;
import fatec.morpheus.DTO.ApiEndpointDTO;
import fatec.morpheus.DTO.ApiFiltersDTO;
import fatec.morpheus.entity.Api;
import fatec.morpheus.entity.DataCollectedApi;
import fatec.morpheus.entity.ErrorResponse;
import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.entity.Synonymous;
import fatec.morpheus.entity.Tag;
import fatec.morpheus.exception.InvalidFieldException;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.exception.UniqueConstraintViolationException;
import fatec.morpheus.repository.ApiRepository;
import fatec.morpheus.repository.DataCollectedApiRepository;
import fatec.morpheus.repository.SynonymousRepository;
import fatec.morpheus.repository.TagRepository;
import fatec.morpheus.repository.TextRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                        existingApi.setTagCodes(apiToUpdate.getTagCodes());
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
                .orElseThrow(() -> new NotFoundException(id, "API"));    
        Hibernate.initialize(api.getTagCodes());

        apiRepository.delete(api);
        return api;
    }


    @Autowired
    private DataCollectedApiRepository dataCollectedApiRepository;

    public List<ApiEndpointDTO> getApiResponsesByCodes(List<ApiEndpointDTO> filterRequests) {
        // Processa a lista de filtros e gera a resposta com a busca do source
        return filterRequests.stream().map(request -> {
            // Criar um DTO de resposta com base no filtro recebido
            ApiEndpointDTO dto = new ApiEndpointDTO();
            dto.setCode(request.getCode());
            dto.setAddress(request.getAddress());
            dto.setMethod(request.getMethod());

            // Buscar o source relacionado à API usando o código da API
            DataCollectedApi dataCollectedApi = dataCollectedApiRepository.findFirstByApi_Code(request.getCode());
            if (dataCollectedApi != null) {
                dto.setSource(dataCollectedApi.getContent()); // Ou qualquer outro campo que deseja usar como "source"
            } else {
                dto.setSource("No source found");
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
}
