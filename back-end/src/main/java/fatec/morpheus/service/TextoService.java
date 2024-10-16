package fatec.morpheus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.Texto;
import fatec.morpheus.repository.TextoRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TextoService {

    @Autowired
    TextoRepository textoRepository;

    @Autowired
    private SynonymousService synonymousService;

    public Texto saveTexto(Texto texto) {
        return textoRepository.save(texto);
    }

    public Optional<Texto> findTextoById(Integer id) {
        return textoRepository.findById(id);
    }

    public List<Texto> findAllTextos() {
        return textoRepository.findAll();
    }

    public void deleteTexto(Integer id) {
        textoRepository.deleteById(id);
    }

    @Transactional
    public void deleteTextoAndRelations(Integer id) {
        synonymousService.deleteRelationById(id);
        textoRepository.deleteById(id);
    }
}