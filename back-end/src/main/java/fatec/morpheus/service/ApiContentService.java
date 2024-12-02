package fatec.morpheus.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fatec.morpheus.DTO.ApiEndpointDTO;
import fatec.morpheus.DTO.ApiFilterRequestDTO;
import fatec.morpheus.DTO.PaginatedApi;
import fatec.morpheus.DTO.PaginatedNewsResponse;
import fatec.morpheus.entity.Api;
import fatec.morpheus.entity.ApiContent;
import fatec.morpheus.entity.TagRelFont;
import fatec.morpheus.repository.ApiContentRepository;
import fatec.morpheus.repository.ApiRepository;
import fatec.morpheus.repository.TagRelFontRepository;


import org.springframework.data.domain.Pageable;

@Service
public class ApiContentService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private TagRelFontRepository tagRelFontRepository;

    @Autowired
    private ApiContentRepository apiContentRepository;

    public void searchContentApi() {
        RestTemplate restTemplateObj = new RestTemplate();

        List<Api> apis = apiRepository.findAll();
        List<TagRelFont> tagRelFonts = tagRelFontRepository.findAll();

        for (Api api : apis) {
            System.out.println("API encontrada: " + api.getAddress());

            List<String> relatedTags = findTagsForApi(api, tagRelFonts);

            if (relatedTags.isEmpty()) {
                System.out.println("Nenhuma tag encontrada para a API: " + api.getAddress() + ". Pulando para a próxima.");
                continue;
            }

            System.out.println("Tags relacionadas: " + relatedTags);

            String content = restTemplateObj.getForObject(api.getAddress(), String.class);

            boolean tagFound = false;

            for (String tag : relatedTags) {
                if (content != null && content.contains(tag)) {
                    tagFound = true;
                    break;
                }
            }

            if (tagFound) {
                System.out.println("API: " + api.getAddress() + " - Ok");

                if (api.getGet() == 1) {
                    saveApiContent(api, content, "GET");
                }

                if (api.getPost() == 1) {
                    saveApiContent(api, content, "POST");
                }
            } else {
                System.out.println("API: " + api.getAddress() + " - Não tem");
            }
        }
    }

    private void saveApiContent(Api api, String content, String method) {
        LocalDate currentDate = LocalDate.now();
    
        Optional<ApiContent> existingContent = apiContentRepository
                .findByApiIdAndApiAddressAndMethodAndDate(api, api.getAddress(), method, currentDate);
    
        if (existingContent.isPresent()) {
            System.out.println("Conteúdo já existe para a API: " + api.getAddress() +
                    ", método: " + method + " no dia " + currentDate + ". Não será salvo.");
            return;
        }
    
        ApiContent apiContent = new ApiContent();
        apiContent.setApiId(api);
        apiContent.setApiContent(content);
        apiContent.setApiAddress(api.getAddress());
        apiContent.setDate(currentDate);
        apiContent.setMethod(method);
    
        apiContentRepository.save(apiContent);
    
        System.out.println("Conteúdo salvo para a API: " + api.getAddress() + ", método: " + method);
    }

    private List<String> findTagsForApi(Api api, List<TagRelFont> tagRelFonts) {
        List<String> tagsForApi = new ArrayList<>();

        for (TagRelFont tagRelFont : tagRelFonts) {
            if (tagRelFont.getApiId().getCode() == api.getCode()) {
                tagsForApi.add(tagRelFont.getTagId().getTagName());
            }
        }

        return tagsForApi;
    }

    public PaginatedApi<ApiEndpointDTO> getFilteredEndpoints(ApiFilterRequestDTO filterRequest, Pageable pageable) {

        List<Api> apis = apiRepository.findAll();

        List<ApiEndpointDTO> result = new ArrayList<>();

        for (Api api : apis) {
            if (filterRequest.getCode() == null || filterRequest.getCode().contains(api.getCode())) {
                ApiContent data = apiContentRepository.findFirstByApiId(api);

                if (data != null) {
                    ApiEndpointDTO dto = new ApiEndpointDTO();
                    dto.setCode(api.getCode());
                    dto.setAddress(api.getAddress());
                    dto.setSource(data.getApiContent());
                    dto.setMethod(api.getPost(), api.getGet());
                    result.add(dto);
                }
            }
        }

        int totalElements = result.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
        int start = Math.min((int) pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<ApiEndpointDTO> currentPageContent = result.subList(start, end);

        return new PaginatedApi<>(currentPageContent, pageable.getPageSize(), totalPages);
    }


}
