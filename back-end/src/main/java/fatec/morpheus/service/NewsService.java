package fatec.morpheus.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fatec.morpheus.DTO.NewsSearchRequest;
import fatec.morpheus.entity.News;
import fatec.morpheus.entity.NewsResponse;
import fatec.morpheus.entity.PaginatedNewsResponse;
import fatec.morpheus.repository.NewsRepository;
import fatec.morpheus.repository.NewsSourceRepository;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;
    

    @Autowired
    private NewsSourceRepository newsSourceRepository;

    public PaginatedNewsResponse<NewsResponse> getNewsWithDetails(int page, int itens) {
        PageRequest pageable = PageRequest.of(page - 1, itens, Sort.by(Sort.Direction.ASC, "newsRegistryDate"));
        Page<News> newsPage = newsRepository.findAll(pageable);
        
        List<NewsResponse> responseDTOs = new ArrayList<>();
        
        for (News news : newsPage) {
            String newsTitle = news.getNewsTitle();
            String newsContent = news.getNewsContent();
            Date newsRegistryDate = news.getNewsRegistryDate();
            String autName = getAuthorName(news);
            String srcName = news.getSourceNews().getSrcName();
            String srcAddress = news.getSourceNews().getAddress();
            String srcURL = news.getNewAddress();
            
            NewsResponse dto = new NewsResponse(newsTitle, newsContent, newsRegistryDate, autName, srcName, srcAddress, srcURL);
            responseDTOs.add(dto);
        }
        
        return new PaginatedNewsResponse<>(
            responseDTOs,
            newsPage.getTotalPages(),
            newsPage.getTotalElements()
        );
    }

    private String getAuthorName(News news) {
        if (news.getNewsAuthor() != null) {
            return news.getNewsAuthor().getAutName();
        }
        return null;
    }

    public void saveNews(News newNew) {
        newsRepository.save(newNew);
    }

    public boolean existsByNewAddress(String address) {
        return newsSourceRepository.existsByAddress(address);
    }

    public Map<String, Object> findNewsWithFilter(NewsSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("newsRegistryDate").descending());
        
        Page<News> pageResult = newsRepository.findAll(NewsSpecification.withFilter(request), pageable);
        
        List<NewsResponse> newsResponses = pageResult.stream().map(news -> {
            String srcName = news.getSourceNews().getSrcName();
            String srcAddress = news.getSourceNews().getAddress();
            
            return new NewsResponse(
                news.getNewsTitle(),
                news.getNewsContent(),
                news.getNewsRegistryDate(),
                news.getNewsAuthor().getAutName(),
                srcName,
                srcAddress,
                news.getNewAddress()
            );
        }).collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("news", newsResponses);   
        response.put("totalPages", pageResult.getTotalPages());   
        response.put("totalElements", pageResult.getTotalElements());
        
        return response;
    }
}