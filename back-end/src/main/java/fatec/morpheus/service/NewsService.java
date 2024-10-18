package fatec.morpheus.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.News;
import fatec.morpheus.entity.NewsAuthor;
import fatec.morpheus.entity.NewsReponse;
import fatec.morpheus.entity.PaginatedNewsResponse;
import fatec.morpheus.repository.NewsAuthorRepository;
import fatec.morpheus.repository.NewsRepository;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired 
    NewsAuthorRepository newsAuthorRepository;
    
    public PaginatedNewsResponse getNewsWithDetails(int page, int itens) {
        PageRequest pageable = PageRequest.of(page, itens, Sort.by(Sort.Direction.ASC, "newsRegistryDate"));
        
        Page<News> newsPage = newsRepository.findAll(pageable); 
        
        List<NewsReponse> responseDTOs = new ArrayList<>();
        

        for (News news : newsPage) {
            String newsTitle = news.getNewsTitle();
            String newsContent = news.getNewsContent();
            Date newsRegistryDate = news.getNewsRegistryDate();
            String autName = (news.getNewsAuthor() != null) ? news.getNewsAuthor().getAutName() : "No author";
            String srcName = news.getSourceNews().getSrcName();
            String srcAddress = news.getSourceNews().getAddress();
            
            NewsReponse dto = new NewsReponse(newsTitle, newsContent, newsRegistryDate, autName, srcName, srcAddress);
            responseDTOs.add(dto);
        }
        
        return new PaginatedNewsResponse(
            responseDTOs,
            newsPage.getTotalPages(), 
            newsPage.getTotalElements()
        );
    }

    public News saveNews(String title, String content, String autName, Date registryDate) {

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
    
        if (registryDate == null) {
            throw new IllegalArgumentException("Publication date is required");
        }
    
        News news = new News();
        
        news.setNewsTitle(title);
        news.setNewsContent(content);
        news.setNewsRegistryDate(registryDate);
        
        if (autName != null && !autName.isEmpty()) {
            Optional<NewsAuthor> newsAuthorOpt = newsAuthorRepository.findByAutName(autName);
            if (newsAuthorOpt.isPresent()) {
                news.setNewsAuthor(newsAuthorOpt.get());
            } else {
                throw new IllegalArgumentException("Author not found");
            }
        } else {
            news.setNewsAuthor(null); 
        }
    
        return newsRepository.save(news);
    }

    
    


    
}
