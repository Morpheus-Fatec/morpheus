package fatec.morpheus.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import fatec.morpheus.DTO.ApiSearchRequest;
import fatec.morpheus.entity.Api;
import fatec.morpheus.entity.ApiResponse;
import fatec.morpheus.repository.ApiRepository;

public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    public Map<String, Object> findNewsWithFilter(ApiSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("apiRegistryDate").descending());
        
        Page<Api> pageResult = apiRepository.findAll(ApiSpecification.withFilter(request), pageable);
        
        List<ApiResponse> apiResponse = pageResult.stream().map(api -> {            
            return new ApiResponse(
                api.getCode(),
                api.getName(),
                api.getAddress(),
                api.getContent(),
                api.getMethod()
            );
        }).collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("api", apiResponse);   
        response.put("totalPages", pageResult.getTotalPages());   
        response.put("totalElements", pageResult.getTotalElements());
        
        return response;
    }
}
