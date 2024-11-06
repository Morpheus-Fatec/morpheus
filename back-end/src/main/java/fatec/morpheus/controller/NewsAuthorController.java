package fatec.morpheus.controller;


import fatec.morpheus.DTO.NewsAuthorDTO;
import fatec.morpheus.service.NewsAuthorService;
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

    @GetMapping
    public List<NewsAuthorDTO> getAuthors() {
        return newsAuthorService.getAllAuthors();
    }
}