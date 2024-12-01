package fatec.morpheus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import fatec.morpheus.DTO.ApiDTO;
import fatec.morpheus.entity.Api;
import fatec.morpheus.service.ApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/morpheus/api")
public class ApiController {

    @Autowired
    ApiService apiService;

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
    public ResponseEntity<List<ApiDTO>> getAllApi() {
        List<ApiDTO> apis = apiService.findAllApiWithTags();
        return ResponseEntity.ok(apis);
    }

    @Operation(summary= "", description = "Busca uma API pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao retornar API"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiDTO> getApiById(@PathVariable int id) {
        ApiDTO apiDTO = apiService.findApiById(id);
        return new ResponseEntity<>(apiDTO, HttpStatus.OK);
    }

    @Operation(summary= "", description = "Atualiza uma API pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar API"),
    })

    @PutMapping("/{id}")
    public ResponseEntity<ApiDTO> updateApi(@PathVariable int id,@RequestBody ApiDTO apiDTO) {

        Api updatedApi = apiService.updateApiById(id, mapToApiEntity(apiDTO), apiDTO.getTags());
        ApiDTO response = mapToApiDTO(updatedApi, apiDTO.getTags());

        return ResponseEntity.ok(response);
    }

    private Api mapToApiEntity(ApiDTO apiDTO) {
        Api api = new Api();
        api.setAddress(apiDTO.getAddress());
        api.setName(apiDTO.getName());
        api.setGet(apiDTO.getGet());
        api.setPost(apiDTO.getPost());
        return api;
    }

    private ApiDTO mapToApiDTO(Api api, List<String> tags) {
        return new ApiDTO(api.getAddress(), api.getName(), api.getGet(), api.getPost(), tags);
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

}
