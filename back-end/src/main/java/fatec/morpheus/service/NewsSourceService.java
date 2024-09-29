package fatec.morpheus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.exception.ErrorResponse;
import fatec.morpheus.exception.NewsSourceException.NotFoundException;
import fatec.morpheus.exception.NewsSourceException.UniqueConstraintViolationException;
import fatec.morpheus.repository.NewsSourceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsSourceService {

    @Autowired
    private NewsSourceRepository newsSourceRepository;

    public NewsSource createNewsSource(NewsSource newsSource) {
        try {
            return newsSourceRepository.save(newsSource);
                
        } catch (Exception e) {
            List<String> duplicateFields = this.verifyUniqueKeys(newsSource);
            
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
}
