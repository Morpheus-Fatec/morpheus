package fatec.morpheus.controller;


import fatec.morpheus.DTO.NewsAuthorDTO;
import fatec.morpheus.exception.NoAuthorsFoundException;
import fatec.morpheus.service.NewsAuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/morpheus/authors")
public class NewsAuthorController {

    @Autowired
    private NewsAuthorService newsAuthorService;

    @Operation(summary = "Listagem de Autores", description = "Retorna todos os Autores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autores retornadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Nenhum Autor Encontrado"),
    })
    @GetMapping
    public List<NewsAuthorDTO> getAuthors() {
        List<NewsAuthorDTO> authors = newsAuthorService.getAllAuthors();
        if (authors.isEmpty()) {
            throw new NoAuthorsFoundException("Nenhum Autor Encontrado");
        }
        return authors;
    }
}