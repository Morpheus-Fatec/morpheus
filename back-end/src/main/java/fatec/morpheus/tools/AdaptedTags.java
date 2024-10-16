package fatec.morpheus.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import fatec.morpheus.entity.Synonymous;
import fatec.morpheus.entity.Tag;
import fatec.morpheus.entity.Texto;
import fatec.morpheus.service.SynonymousService;
import fatec.morpheus.service.TagService;
import fatec.morpheus.service.TextoService;

@Component
public class AdaptedTags {
    List<String> tagsList = new ArrayList<>();
    List<String> textoList = new ArrayList<>();
    Map<Integer, Integer> synonymMap = new HashMap<>();

    private final TagService tagService;
    private final TextoService textoService;
    private final SynonymousService synonymousService;
    private final RestTemplate restTemplate;

    private final String TAGS_URL = "http://127.0.0.1:8080/morpheus/tag";
    private final String TEXTO_URL = "http://127.0.0.1:8080/textos";
    private final String SYNONYMOUS_URL = "http://localhost:8080/textos/synonyms";

    public AdaptedTags (TagService tagService, TextoService textoService, SynonymousService synonymousService, RestTemplate restTemplate){
        this.tagService = tagService;
        this.textoService = textoService;
        this.restTemplate = restTemplate;
        this.synonymousService = synonymousService;
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

    public void allSynonymous() {
        Synonymous[] synonymousArray = restTemplate.getForObject(SYNONYMOUS_URL, Synonymous[].class);

        if (synonymousArray != null) {
            for (Synonymous syn : synonymousArray) {
                synonymMap.put(syn.getTextoCod(), syn.getSynGroup());
            }
        }
    }

    public List<String> chamaFio(){
        allTags();
        allTexts();
        allSynonymous();
        
        System.out.println(tagsList);
        System.out.println(textoList);
        System.out.println(synonymMap);
        return tagsList;
    }
}
