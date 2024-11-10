package fatec.morpheus.service;

import fatec.morpheus.DTO.NewsAuthorDTO;
import fatec.morpheus.repository.NewsAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsAuthorService {

    @Autowired
    private NewsAuthorRepository newsAuthorRepository;

    public List<NewsAuthorDTO> getAllAuthors() {
        return newsAuthorRepository.findAll().stream()
            .map(author -> new NewsAuthorDTO(author.getAutId(), author.getAutName()))
            .collect(Collectors.toList());
    }
}
