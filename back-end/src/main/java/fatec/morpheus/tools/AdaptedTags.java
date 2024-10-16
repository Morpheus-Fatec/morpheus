package fatec.morpheus.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import fatec.morpheus.entity.Tag;
import fatec.morpheus.entity.Texto;
import fatec.morpheus.service.TagService;
import fatec.morpheus.service.TextoService;

@Component
public class AdaptedTags {
    List<String> tagsList = new ArrayList<>();
    List<String> textoList = new ArrayList<>();

    private final TagService tagService;
    private final TextoService textoService;
    private final RestTemplate restTemplate;

    private final String TAGS_URL = "http://127.0.0.1:8080/morpheus/tag";
    private final String TEXTO_URL = "http://127.0.0.1:8080/textos";

    public AdaptedTags (TagService tagService, TextoService textoService, RestTemplate restTemplate){
        this.tagService = tagService;
        this.textoService = textoService;
        this.restTemplate = restTemplate;
    }

    public void allTags(){
        Tag[] tags = restTemplate.getForObject(TAGS_URL, Tag[].class);
        if (tags != null){
            for(Tag tag : tags){
                tagsList.add(tag.getTagName());
            }
        }
    }

    public void allTexts(){
        Texto[] textos = restTemplate.getForObject(TEXTO_URL, Texto[].class);
        if(textos != null){
            for(Texto texto : textos){
                textoList.add(texto.getTextoDescription());
            }
        }
    }
}
