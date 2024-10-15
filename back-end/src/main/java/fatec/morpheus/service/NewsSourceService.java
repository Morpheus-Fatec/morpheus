package fatec.morpheus.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fatec.morpheus.DTO.NewsSourceDTO;
import fatec.morpheus.entity.ErrorResponse;
import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.exception.InvalidFieldException;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.exception.UniqueConstraintViolationException;
import fatec.morpheus.repository.NewsSourceRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

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
    private NewsSource source = new NewsSource();
    private NewsSourceDTO newsSourceDTO = new NewsSourceDTO();


    public NewsSourceDTO createNewsSource(NewsSourceDTO newsSourceCreatedDTO) {

        source.setSrcName(newsSourceCreatedDTO.getSrcName());
        source.setAddress(newsSourceCreatedDTO.getAddress());

        
        Set<ConstraintViolation<NewsSource>> sourceViolations = validator.validate(source);
       
    
        if (!sourceViolations.isEmpty()) {
            List<String> errors = sourceViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
            errorResponse.setMessage("Campos Vazios");
            throw new InvalidFieldException(errorResponse);
        }
   

        try {         
            newsSourceRepository.save(source);      
            newsSourceDTO.setSrcName(source.getSrcName());
            newsSourceDTO.setAddress(source.getAddress());
            newsSourceDTO.setMap(newsSourceCreatedDTO.getMap());
        
            return newsSourceDTO;
                
        } catch (Exception e) {
            List<String> duplicateFields = this.verifyUniqueKeys(source);
            
            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,   
                duplicateFields       
            );

            throw new UniqueConstraintViolationException(errorResponse);
        }
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
                .orElseThrow(() -> new NotFoundException(id, "Fonte de Notícia"));  
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
        return newsSourceRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(id, "Fonte de Notícia"));
    }
    
}
