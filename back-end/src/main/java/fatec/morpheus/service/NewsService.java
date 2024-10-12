package fatec.morpheus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.News;
import fatec.morpheus.repository.NewsRepository;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;
    

    public List<News> findAllNews(int page, int itens) {
        Page<News> newsPage = newsRepository.findAll(PageRequest.of(page, itens, Sort.by(Sort.Direction.ASC, "newsRegistryDate")));
        
        return newsPage.getContent();
    }



    
}
