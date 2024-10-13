package fatec.morpheus.service;

import fatec.morpheus.entity.Synonymous;
import fatec.morpheus.repository.SynonymousRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SynonymousService {

    @Autowired
    private SynonymousRepository synonymousRepository;

    public Synonymous saveSynonymous(Synonymous synonymous) {
        return synonymousRepository.save(synonymous);
    }

    public List<Synonymous> findAllSynonymous() {
        return synonymousRepository.findAll();
    }

    @Transactional
    public void deleteRelationById(Integer textoCod) {
        synonymousRepository.deleteByTextoCod(textoCod);
    }
}