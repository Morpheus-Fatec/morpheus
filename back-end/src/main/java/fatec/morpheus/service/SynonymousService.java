package fatec.morpheus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.Synonymous;
import fatec.morpheus.repository.SynonymousRepository;
import jakarta.transaction.Transactional;

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
    public void deleteRelationById(Integer textCod) {
        synonymousRepository.deleteByTextCod(textCod);
    }
}