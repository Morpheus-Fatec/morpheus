package fatec.morpheus.service;

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

    /**
     * Cria uma nova API e associa as tags fornecidas.
     *
     * @param apiDTO Dados para criação da API.
     * @return A instância da API criada.
     */
    public Api createApi(ApiDTO apiDTO) {
        Api api = new Api();
        api.setAddress(apiDTO.getAddress());
        api.setGet(apiDTO.getGet());
        api.setPost(apiDTO.getPost());

        validateApi(api);

        try {
            Api savedApi = apiRepository.save(api);

            associateTagsWithApi(savedApi, apiDTO.getTagCodes());
            return savedApi;

        } catch (DataIntegrityViolationException e) {
            handleUniqueConstraintViolation(api);
            return null;
        }
    }

    /**
     * Atualiza uma API existente e suas associações de tags.
     *
     * @param id         Identificador único da API.
     * @param apiToUpdate Dados para atualização da API.
     * @param tagCodes    Lista de códigos das tags para associação.
     * @return A instância da API atualizada.
     */
    public Api updateApiById(int id, Api apiToUpdate, List<Integer> tagCodes) {
        validateApi(apiToUpdate);

        return apiRepository.findById(id)
                .map(existingApi -> {
                    existingApi.setAddress(apiToUpdate.getAddress());
                    existingApi.setGet(apiToUpdate.getGet());
                    existingApi.setPost(apiToUpdate.getPost());

                    Api updatedApi = apiRepository.save(existingApi);

                    updateTagsForApi(updatedApi, tagCodes);

                    return updatedApi;
                })
                .orElseThrow(() -> new NotFoundException(id, "API"));
    }

    /**
     * Retorna todas as APIs cadastradas.
     *
     * @return Lista de APIs.
     */
    public List<Api> findAllApi() {
        return apiRepository.findAll();
    }

    /**
     * Busca uma API pelo ID.
     *
     * @param id Identificador da API.
     * @return Instância da API encontrada.
     */
    public Api findApiById(int id) {
        return apiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "API"));
    }

    /**
     * Remove uma API e suas associações de tags.
     *
     * @param id Identificador da API.
     * @return A API removida.
     */
    public Api deleteApiById(int id) {
        Api api = apiRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(id, "API"));

        tagRelFontRepository.deleteByApiId(api);

        apiRepository.delete(api);

        return api;
    }

    /**
     * Valida os campos de uma API usando o Validator.
     *
     * @param api Instância da API para validação.
     */
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

    /**
     * Trata violações de unicidade.
     *
     * @param api Instância da API para verificação.
     */
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

    /**
     * Verifica se existem campos únicos já cadastrados no banco.
     *
     * @param api Instância da API para verificação.
     * @return Lista de campos duplicados.
     */
    private List<String> verifyUniqueKeys(Api api) {
        if (apiRepository.existsByAddress(api.getAddress())) {
            return List.of("address");
        }
        return List.of();
    }

    /**
     * Associa as tags à API usando TagRelFont.
     *
     * @param api      Instância da API.
     * @param tagCodes Lista de IDs das tags.
     */
    private void associateTagsWithApi(Api api, List<Integer> tagCodes) {
        if (tagCodes != null) {
            List<Tag> tags = tagRepository.findAllById(tagCodes);
            for (Tag tag : tags) {
                TagRelFont relation = new TagRelFont();
                relation.setApiId(api);
                relation.setTagId(tag);
                tagRelFontRepository.save(relation);
            }
        }
    }

    /**
     * Atualiza as associações de tags para uma API.
     *
     * @param api      Instância da API.
     * @param tagCodes Lista de IDs das tags.
     */
    private void updateTagsForApi(Api api, List<Integer> tagCodes) {
        tagRelFontRepository.deleteByApiId(api);

        associateTagsWithApi(api, tagCodes);
    }
}
