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
import fatec.morpheus.entity.Api;
import fatec.morpheus.entity.ErrorResponse;
import fatec.morpheus.entity.Tag;
import fatec.morpheus.entity.TagRelFont;
import fatec.morpheus.exception.InvalidFieldException;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.exception.UniqueConstraintViolationException;
import fatec.morpheus.repository.ApiRepository;
import fatec.morpheus.repository.TagRelFontRepository;
import fatec.morpheus.repository.TagRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

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

    public Api createApi(ApiDTO apiDTO) {
        Api api = new Api();
        api.setAddress(apiDTO.getAddress());
        api.setName(apiDTO.getName());
        api.setGet(apiDTO.getGet());
        api.setPost(apiDTO.getPost());
    
        validateApi(api);
    
        try {
            Api savedApi = apiRepository.save(api);
    
            associateTagsWithApi(savedApi, apiDTO.getTags());
            return savedApi;
    
        } catch (DataIntegrityViolationException e) {
            handleUniqueConstraintViolation(api);
            return null;
        }
    }
    
    @Transactional
    public Api updateApiById(int id, Api apiToUpdate, List<String> tags) {
        return apiRepository.findById(id)
                .map(existingApi -> {
                    existingApi.setAddress(apiToUpdate.getAddress());
                    existingApi.setGet(apiToUpdate.getGet());
                    existingApi.setPost(apiToUpdate.getPost());
    
                    Api updatedApi = apiRepository.save(existingApi);
    
                    updateTagsForApi(updatedApi, tags);
    
                    return updatedApi;
                })
                .orElseThrow(() -> new NotFoundException(id, "API"));
    }

    public List<Api> findAllApi() {
        return apiRepository.findAll();
    }

    public List<ApiDTO> findAllApiWithTags() {
        List<Api> apis = apiRepository.findAll();
        List<ApiDTO> apiDTOs = new ArrayList<>();

        for (Api api : apis) {
            List<String> tags = tagRepository.findTagsByApiId(api.getCode());
            
            ApiDTO apiDTO = new ApiDTO(
                api.getAddress(),
                api.getName(),
                api.getGet(),
                api.getPost(),
                tags
            );
            
            apiDTOs.add(apiDTO);
        }
        
        return apiDTOs;
    }

    public ApiDTO findApiById(int id) {
        Api api = apiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "API"));

        List<String> tags = tagRepository.findTagsByApi(id);

        return new ApiDTO(
                api.getAddress(),
                api.getName(),
                api.getGet(),
                api.getPost(),
                tags
        );
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
