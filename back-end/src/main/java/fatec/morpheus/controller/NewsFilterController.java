import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fatec.morpheus.entity.NewsReponse;
import fatec.morpheus.service.NewsService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/morpheus/NewsFilter")
public class NewsFilterController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/search")
    public ResponseEntity<List<NewsReponse>> searchNews(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String conteudo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String portal,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim) {
        
        List<NewsReponse> noticias = newsService.buscarNoticiasComFiltros(titulo, conteudo, autor, portal, dataInicio, dataFim);
        
        if (noticias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        }
        
        return new ResponseEntity<>(noticias, HttpStatus.OK); // 
    }
}
