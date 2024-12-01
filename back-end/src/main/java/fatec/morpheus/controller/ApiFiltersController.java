package fatec.morpheus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.DTO.ApiDTO;
import fatec.morpheus.DTO.ApiEndpointDTO;
import fatec.morpheus.DTO.ApiFiltersDTO;
import fatec.morpheus.service.ApiService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/morpheus/api")
public class ApiFiltersController {

    @Autowired
    private ApiService apiService;

    @PostMapping("/filter")
    public List<ApiEndpointDTO> getApiResponsesByCodes(@RequestBody List<ApiEndpointDTO> filterRequests) {
        return apiService.getApiResponsesByCodes(filterRequests);
    }
}

