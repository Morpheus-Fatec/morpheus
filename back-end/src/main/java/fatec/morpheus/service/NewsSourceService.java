package fatec.morpheus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.repository.NewsSourceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NewsSourceService {

    @Autowired
    private NewsSourceRepository newsSourceRepository;

    public NewsSource saveNewsSource(NewsSource newsSource) {
        return newsSourceRepository.save(newsSource);
    }

    public List<NewsSource> findAllNewsSources() {
        return newsSourceRepository.findAll();
    }

    public Optional<NewsSource> findNewsSourceById(int id) {
        return newsSourceRepository.findById(id);
    }

    public ResponseEntity<NewsSource> updateNewsSourceById(int id, NewsSource newsSourceToUpdate) {
        return newsSourceRepository.findById(id)
                .map(existingNewsSource -> {
                    existingNewsSource.setSrcName(newsSourceToUpdate.getSrcName());
                    existingNewsSource.setType(newsSourceToUpdate.getType());
                    existingNewsSource.setAddress(newsSourceToUpdate.getAddress());

                    //atualiza tags
                    existingNewsSource.getTags().clear();
                    existingNewsSource.getTags().addAll(newsSourceToUpdate.getTags());
                    newsSourceRepository.save(existingNewsSource);
                    return new ResponseEntity<>(existingNewsSource, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<NewsSource> deleteNewsSourceById(int id) {
        return newsSourceRepository.findById(id)
                .map(newsSource -> {
                    newsSourceRepository.deleteById(id);
                    return new ResponseEntity<NewsSource>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
