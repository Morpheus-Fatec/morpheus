package main.java.fatec.morpheus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fatec.morpheus.dao.NewsSource;
import fatec.morpheus.repository.NewsSourceRepository;

import java.util.List;

@Service
public class NewsSourceService {

    @Autowired
    NewsSourceRepository newsSourceRepository;


    public void saveNewsSource(NewsSource NewsSourceToCreate){
        return newsSourceRepository.save(NewsSourceToCreate);
    }

    public List<NewsSource> findAllNewsSource(){
        List<NewsSource> sources = newsSourceRepository.findAll();
        return sources;
    }

    public List<NewsSource> findNewsSourceById(int id){
        return newsSourceRepository.findById(id);
        
    }

    public ResponseEntity<NewsSource> editNewsSourceById(int id, NewsSource NewsSourceToEdit) {
        return newsSourceRepository.findById(id)
            .map(newsSource -> {
                newsSource.setSrcName(NewsSourceToEdit.getSrcName());
                newsSource.setType(NewsSourceToEdit.getType());
                newsSource.setAddress(NewsSourceToEdit.getAddress());
                newsSourceRepository.save(newsSource);
                return new ResponseEntity<>(newsSource, HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<NewsSource> deleteNewsSourceById(int id) {
        return newsSourceRepository.findById(id)
            .map(newsSource -> {
                newsSourceRepository.deleteById(id);
                return new ResponseEntity<>(newsSource, HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
