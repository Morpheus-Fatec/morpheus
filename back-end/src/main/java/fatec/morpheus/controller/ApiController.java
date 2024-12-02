package fatec.morpheus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.DTO.ApiDTO;
import fatec.morpheus.DTO.ApiEndpointDTO;
import fatec.morpheus.DTO.ApiFilterRequestDTO;
import fatec.morpheus.DTO.PaginatedApi;
import fatec.morpheus.DTO.PaginatedNewsResponse;
import fatec.morpheus.entity.Api;
import fatec.morpheus.service.ApiContentService;
import fatec.morpheus.service.ApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/morpheus/api")
public class ApiController {

    @Autowired
    ApiService apiService;

    @Autowired
    ApiContentService apiContentService;

    @Operation(summary= "", description = "Cria um novo registro de API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao criar API"),
    })
    @PostMapping
    public ResponseEntity<Void> createApi(@RequestBody ApiDTO apiDTO) {
        apiService.createApi(apiDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary= "Busca", description = "Retorna todos as APIs cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "APIs retornadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao retornar APIs"),
    })
    @GetMapping
    public ResponseEntity<List<Api>> getAllApi() {
        List<Api> apis = apiService.findAllApi();
        return ResponseEntity.ok(apis);
    }

    @Operation(summary= "", description = "Busca uma API pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao retornar API"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Api> getApiById(@PathVariable int id) {
        Api api = apiService.findApiById(id);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }
    @Operation(summary= "", description = "Atualiza uma API pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar API"),
    })

    @PutMapping("/{id}")
    public ResponseEntity<Api> updateApi(@PathVariable int id, @RequestBody Api api) {
        Api updatedApi = apiService.updateApiById(id, api);
        return ResponseEntity.ok(updatedApi);
    }


    @Operation(summary= "", description = "Deleta uma API pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API deletada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao deletar API"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Api> deleteApi(@PathVariable int id) {
        Api api = apiService.deleteApiById(id);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }

    
    @Operation(summary= "Filtro", description = "Filtro da API")
    @PostMapping("/filter")
    public PaginatedApi<ApiEndpointDTO> filterEndpoints(
            @RequestBody ApiFilterRequestDTO filterRequest,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return apiContentService.getFilteredEndpoints(filterRequest, pageable);
    }

}
