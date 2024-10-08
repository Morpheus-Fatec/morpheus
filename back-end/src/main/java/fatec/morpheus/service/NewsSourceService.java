package fatec.morpheus.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fatec.morpheus.DTO.NewsSourceDTO;
import fatec.morpheus.entity.ErrorResponse;
import fatec.morpheus.entity.MapSource;
import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.exception.InvalidFieldException;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.exception.UniqueConstraintViolationException;
import fatec.morpheus.repository.NewsSourceRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;



@Service
public class NewsSourceService {

    @Autowired
    private NewsSourceRepository newsSourceRepository;

    @Autowired
    private Validator validator;

    public NewsSource createNewsSource(NewsSourceDTO newsSourceDTO) {

        NewsSource source = new NewsSource();
        source.setSrcName(newsSourceDTO.getName());
        source.setAddress(newsSourceDTO.getAddress());
        
        MapSource mapSource = new MapSource();
        mapSource.setSource(source);
        mapSource.setAuthor(newsSourceDTO.getMap().getAuthor());
        mapSource.setBody(newsSourceDTO.getMap().getBody());
        mapSource.setTitle(newsSourceDTO.getMap().getTitle());
        mapSource.setUrl(newsSourceDTO.getMap().getUrl());
        MapSource mapSourceResolved = findHtmlTags(mapSource);

        // Set<ConstraintViolation<NewsSource>> violations = validator.validate(source);

        // if (!violations.isEmpty()) {
        //     List<String> errors = violations.stream()
        //         .map(ConstraintViolation::getMessage)
        //         .collect(Collectors.toList());

        //     ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        //     throw new InvalidFieldException(errorResponse);
        // }

        // try {
        //     return newsSourceRepository.save(source);
                
        // } catch (Exception e) {
        //     List<String> duplicateFields = this.verifyUniqueKeys(source);
            
        //     ErrorResponse errorResponse = new ErrorResponse(
        //         HttpStatus.CONFLICT,   
        //         duplicateFields       
        //     );

        //     throw new UniqueConstraintViolationException(errorResponse);
        // }
        return null;
    }

    private List<String> verifyUniqueKeys(NewsSource newsSource) {
        List<String> duplicateFields = new ArrayList<>();
        if (newsSourceRepository.existsBySrcName(newsSource.getSrcName())) {
            duplicateFields.add("srcName");
        }
        if (newsSourceRepository.existsByAddress(newsSource.getAddress())) {
            duplicateFields.add("address");
        }
        return duplicateFields;
    }

    public List<NewsSource> findAllNewsSources() {
        return newsSourceRepository.findAll();
    }

    public NewsSource findNewsSourceById(int id) {
        return newsSourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));  
    }

    public NewsSource updateNewsSourceById(int id, NewsSource newsSourceToUpdate) {
        try {
        return newsSourceRepository.findById(id)
                .map(existingNewsSource -> {
                    existingNewsSource.setSrcName(newsSourceToUpdate.getSrcName());
                    existingNewsSource.setType(newsSourceToUpdate.getType());
                    existingNewsSource.setAddress(newsSourceToUpdate.getAddress());
                    existingNewsSource.getTags().clear();
                    existingNewsSource.getTags().addAll(newsSourceToUpdate.getTags());

                    return newsSourceRepository.save(existingNewsSource);
                })
                .orElseThrow(() -> new NotFoundException(id));
        } catch (DataIntegrityViolationException e) {
            List<String> duplicateFields = this.verifyUniqueKeys(newsSourceToUpdate);

            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,  
                duplicateFields
            );
            throw new UniqueConstraintViolationException(errorResponse);
        }
    }
    

    public NewsSource deleteNewsSourceById(int id) {
        return newsSourceRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(id));
    }

    private MapSource findHtmlTags(MapSource mapSource) {
        try {
            // Conecta e obtém o documento HTML da URL
            Document doc = Jsoup.connect(mapSource.getUrl()).get();
    
            // Procura pela tag que contém o título
            String title = mapSource.getTitle();
            System.out.println("Título: " + title);
    
            if (title != null) {
                // Usa a função auxiliar para encontrar o elemento que contém o texto específico
                Element titleElement = findElementContainingText(doc, title);
                if (titleElement != null) {
                    String titleClass = titleElement.className();
                    System.out.println("Classe do título: " + titleClass);
                } else {
                    System.out.println("Título não encontrado.");
                }
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return mapSource;
    }
    
    // Função auxiliar para encontrar o elemento que contém o texto específico
    private Element findElementContainingText(Document doc, String text) {
        Elements elements = doc.getAllElements();
        for (Element element : elements) {
            if (element.text().equals(text)) {  // Verifica se o texto é igual ao título
                return element; // Retorna o elemento se o texto for encontrado
            }
        }
        return null; // Retorna null se não encontrar
    }
}
