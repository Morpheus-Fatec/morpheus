package fatec.morpheus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fatec.morpheus.DTO.ApiDTO;
import fatec.morpheus.DTO.ErrorResponse;
import fatec.morpheus.entity.Api;
import fatec.morpheus.entity.Tag;
import fatec.morpheus.entity.TagRelFont;
import fatec.morpheus.exception.InvalidFieldException;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.exception.UniqueConstraintViolationException;
import fatec.morpheus.repository.ApiRepository;
import fatec.morpheus.repository.TagRelFontRepository;
import fatec.morpheus.repository.TagRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import jakarta.transaction.Transactional;

@Service
public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagRelFontRepository tagRelFontRepository;

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

    public List<Api> findAllApi() {
        return apiRepository.findAll();
    }

    public Api findApiById(int id) {
        return apiRepository.findById(id).orElseThrow(() -> new NotFoundException(id, "API"));
    }
    
    @Transactional
    public Api deleteApiById(int id) {
        Api api = apiRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(id, "API"));

        tagRelFontRepository.deleteByApiId(api);

        apiRepository.delete(api);

        return api;
    }

    private void validateApi(Api api) {
        Set<ConstraintViolation<Api>> violations = validator.validate(api);
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
            errorResponse.setMessage("Problemas com campos obrigatórios");
            throw new InvalidFieldException(errorResponse);
        }
    }

    private void handleUniqueConstraintViolation(Api api) {
        List<String> duplicateFields = verifyUniqueKeys(api);
        if (!duplicateFields.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.CONFLICT,
                    duplicateFields,
                    "Campos duplicados: " + String.join(", ", duplicateFields)
            );
            throw new UniqueConstraintViolationException(errorResponse);
        }
    }


    private List<String> verifyUniqueKeys(Api api) {
        if (apiRepository.existsByAddress(api.getAddress())) {
            return List.of("address");
        }
        return List.of();
    }

    private void associateTagsWithApi(Api api, List<String> tagCodes) {
        if (tagCodes != null && !tagCodes.isEmpty()) {
            List<Tag> tags = new ArrayList<>();

            for (String tagCode : tagCodes) {
                Tag tag = tagRepository.findByTagName(tagCode);
                if (tag == null) {
                    tag = new Tag();
                    tag.setTagName(tagCode);
                    tagRepository.save(tag);
                }
                tags.add(tag);
            }
    
            for (Tag tag : tags) {
                TagRelFont relation = new TagRelFont();
                relation.setApiId(api);
                relation.setTagId(tag);
                tagRelFontRepository.save(relation);
                System.out.println("Tag associada: " + tag.getTagName());
            }
        } else {
            System.out.println("Nenhuma tag fornecida para associar.");
        }
    }
    
    private void updateTagsForApi(Api api, List<String> tagCodes) {
        tagRelFontRepository.deleteByApiId(api);

        associateTagsWithApi(api, tagCodes);
    }
}