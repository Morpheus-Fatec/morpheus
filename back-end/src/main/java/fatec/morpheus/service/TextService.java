package fatec.morpheus.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.Text;
import fatec.morpheus.repository.TextRepository;
import jakarta.transaction.Transactional;

@Service
public class TextService {

    @Autowired
    TextRepository textRepository;

    @Autowired
    private SynonymousService synonymousService;

    public Text saveText(Text text) {
        return textRepository.save(text);
    }

    public Optional<Text> findTextById(Integer id) {
        return textRepository.findById(id);
    }

    public List<Text> findAllTexts() {
        return textRepository.findAll();
    }

    public void deleteText(Integer id) {
        textRepository.deleteById(id);
    }

    @Transactional
    public void deleteTextAndRelations(Integer id) {
        synonymousService.deleteRelationById(id);
        textRepository.deleteById(id);
    }
}