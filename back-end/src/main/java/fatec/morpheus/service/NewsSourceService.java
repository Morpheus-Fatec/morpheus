package fatec.morpheus.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fatec.morpheus.DTO.ErrorResponse;
import fatec.morpheus.DTO.MapSourceDTO;
import fatec.morpheus.DTO.NewsSourceDTO;
import fatec.morpheus.entity.MapSource;
import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.exception.InvalidFieldException;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.exception.UniqueConstraintViolationException;
import fatec.morpheus.repository.NewsSourceRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;


@Service
public class NewsSourceService {

    @Autowired
    private NewsSourceRepository newsSourceRepository;
    @Autowired
    private Validator validator;

    public NewsSource createNewsSource(NewsSourceDTO newsSourceCreatedDTO) {
        NewsSource source = new NewsSource();

        source.setSrcName(newsSourceCreatedDTO.getSrcName());
        source.setType(newsSourceCreatedDTO.getType());
        source.setAddress(newsSourceCreatedDTO.getAddress());
        source.setTagCodes(newsSourceCreatedDTO.getTagCodes());

        MapSourceDTO map = verifyDotMapSource(newsSourceCreatedDTO.getMap());
        MapSource sourceMap = map.toEntity();
        sourceMap.setSource(source);
        source.setMap(sourceMap);

        Set<ConstraintViolation<NewsSource>> sourceViolations = validator.validate(source);    
        if (!sourceViolations.isEmpty()) {
            List<String> errors = sourceViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
            errorResponse.setMessage("Problemas com campos obrigatórios");
            throw new InvalidFieldException(errorResponse);
        }
   

        try {         
            newsSourceRepository.save(source);      
            return source;
                
        } catch (Exception e) {
            List<String> duplicateFields = this.verifyUniqueKeys(source);

            if (duplicateFields.isEmpty()) {
                String errorMessage = e.getCause().getMessage();
                ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.CONFLICT,   
                    duplicateFields,
                    errorMessage       
                );
                throw new UniqueConstraintViolationException(errorResponse);
            }
            
            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,   
                duplicateFields,
                "Campos Duplicados"       
            );

            throw new UniqueConstraintViolationException(errorResponse);
        }
    }

    private MapSourceDTO verifyDotMapSource(MapSourceDTO mapSource) {
        // Verifica e ajusta o campo "author"
        if (mapSource.getAuthor() != null && !mapSource.getAuthor().startsWith(".")) {
            mapSource.setAuthor("." + mapSource.getAuthor());
        }
        
        // Verifica e ajusta o campo "body"
        if (mapSource.getBody() != null && !mapSource.getBody().startsWith(".")) {
            mapSource.setBody("." + mapSource.getBody());
        }
        
        // Verifica e ajusta o campo "title"
        if (mapSource.getTitle() != null && !mapSource.getTitle().startsWith(".")) {
            mapSource.setTitle("." + mapSource.getTitle());
        }
        
        // Verifica e ajusta o campo "date"
        if (mapSource.getDate() != null && !mapSource.getDate().startsWith(".")) {
            mapSource.setDate("." + mapSource.getDate());
        }
    
        return mapSource;
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
        return newsSourceRepository.findById(id).orElseThrow(() -> new NotFoundException(id, "Fonte de Notícia"));  
    }

    public NewsSource updateNewsSourceById(int id, NewsSource newsSourceToUpdate) {
        try {
        return newsSourceRepository.findById(id)
                .map(existingNewsSource -> {
                    existingNewsSource.setSrcName(newsSourceToUpdate.getSrcName());
                    existingNewsSource.setType(newsSourceToUpdate.getType());
                    existingNewsSource.setAddress(newsSourceToUpdate.getAddress());
                    existingNewsSource.getTagCodes().clear();
                    existingNewsSource.getTagCodes().addAll(newsSourceToUpdate.getTagCodes());
                    existingNewsSource.getMap().setAuthor(newsSourceToUpdate.getMap().getAuthor());
                    existingNewsSource.getMap().setBody(newsSourceToUpdate.getMap().getBody());
                    existingNewsSource.getMap().setTitle(newsSourceToUpdate.getMap().getTitle());
                    existingNewsSource.getMap().setDate(newsSourceToUpdate.getMap().getDate());
                    return newsSourceRepository.save(existingNewsSource);
                })
                .orElseThrow(() -> new NotFoundException(id, "Fonte de Notícia"));
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
        NewsSource newsSource = newsSourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "Fonte de Notícia"));
        Hibernate.initialize(newsSource.getTagCodes());
        Hibernate.initialize(newsSource.getMap());

        newsSourceRepository.delete(newsSource);
        return newsSource;
    }

    
}
