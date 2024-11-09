package fatec.morpheus.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.Synonymous;
import fatec.morpheus.entity.Text;
import fatec.morpheus.repository.TextRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

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

    public List<Integer> findSynonymsByTextId(Integer textId) {
        List<Synonymous> synonyms = synonymousService.findByTextCodOrSynGroup(textId);
        return synonyms.stream()
                .map(s -> s.getTextCode() == textId ? s.getSynGroup() : s.getTextCode())
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class TextUpdateRequest {
        private String textoDescription;
        private List<Integer> synonymIds;
    }
}