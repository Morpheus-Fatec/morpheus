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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import fatec.morpheus.entity.News;
import fatec.morpheus.entity.NewsAuthor;
import fatec.morpheus.entity.NewsReponse;
import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.entity.PaginatedNewsResponse;
import fatec.morpheus.repository.NewsAuthorRepository;
import fatec.morpheus.repository.NewsRepository;
import fatec.morpheus.repository.NewsSourceRepository;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired 
    NewsAuthorRepository newsAuthorRepository;

    @Autowired
    NewsSourceRepository newsSourceRepository;
    
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

    // @Transactional
    // public News saveNews(String newsTitle, String newsContent, String authorName, Date registryDate, Integer sourceId) {
    //     if (!StringUtils.hasText(newsTitle)) {
    //         throw new IllegalArgumentException("O título da notícia é obrigatório.");
    //     }
    
    //     NewsSource sourceNews = newsSourceRepository.findById(sourceId).orElseThrow(() -> new IllegalArgumentException("Fonte de notícia não encontrada."));
        
    //     News news = new News();
    //     news.setNewsTitle(newsTitle);
    //     news.setNewsContent(newsContent);
    //     news.setNewsRegistryDate(registryDate);
    //     news.setSourceNews(sourceNews);
    
    //     if (StringUtils.hasText(authorName)) {
    //         NewsAuthor author = new NewsAuthor();
    //         author.setAutName(authorName);
    //         news.setNewsAuthor(author);
    //     } else {
    //         news.setNewsAuthor(null); 
    //     }
    
    //     return newsRepository.save(news);
    // }
    public News saveNews(String newsTitle, String newsContent, String authorName, Date registryDate, Integer sourceId) {
        // Validação do título
        if (newsTitle == null || newsTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("O título da notícia é obrigatório.");
        }

        // Busca a fonte de notícias pelo ID
        NewsSource sourceNews = newsSourceRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Fonte de notícia não encontrada."));

        // Criação da instância de News
        News news = new News();
        news.setNewsTitle(newsTitle);
        news.setNewsContent(newsContent);
        news.setNewsRegistryDate(registryDate);
        news.setSourceNews(sourceNews);

        // Caso o autor seja fornecido, ele será associado à notícia
        if (authorName != null && !authorName.trim().isEmpty()) {
            NewsAuthor author = new NewsAuthor();
            author.setAutName(authorName);

            // Salva o autor no banco de dados, se for necessário
            newsAuthorRepository.save(author);

            news.setNewsAuthor(author);
        }

        // Salva a notícia no banco de dados
        return newsRepository.save(news);
    }

}
