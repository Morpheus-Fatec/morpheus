package fatec.morpheus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.News;
import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.repository.NewsRepository;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;
    

    public List<News> findAllNews() {
        return newsRepository.findAll(Sort.by(Sort.Direction.ASC, "newsRegistryDate"));
    }

    
}
