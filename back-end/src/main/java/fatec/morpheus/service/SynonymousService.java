package fatec.morpheus.service;

import fatec.morpheus.entity.Synonymous;
import fatec.morpheus.repository.SynonymousRepository;
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
}