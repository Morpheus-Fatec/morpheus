package fatec.morpheus.service;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import fatec.morpheus.DTO.NewsSearchRequest;
import fatec.morpheus.entity.Api;
import fatec.morpheus.repository.ApiRepository;

public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    public Map<String, Object> findNewsWithFilter(NewsSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("newsRegistryDate").descending());
        
        Page<Api> pageResult = apiRepository.findAll(ApiSpecification.withFilter(request), pageable);
        
        // List<NewsReponse> newsResponses = pageResult.stream().map(news -> {
            // String srcName = news.getSourceNews().getSrcName();
            // String srcAddress = news.getSourceNews().getAddress();
            
        //     return new NewsReponse(
        //         news.getNewsTitle(),
        //         news.getNewsContent(),
        //         news.getNewsRegistryDate(),
        //         news.getNewsAuthor().getAutName(),
        //         srcName,
        //         srcAddress,
        //         news.getNewAddress()
        //     );
        // }).collect(Collectors.toList());
        
        // Map<String, Object> response = new HashMap<>();
        // response.put("news", newsResponses);   
        // response.put("totalPages", pageResult.getTotalPages());   
        // response.put("totalElements", pageResult.getTotalElements());
        
        return null;
    }
}
