package fatec.morpheus.tools;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fatec.morpheus.entity.Tag;
import fatec.morpheus.service.TagService;

@Component
public class AdaptedTags {
    private List<Tag> listaRetorno = new ArrayList<>();
    private  final TagService tagService;

    public AdaptedTags (TagService tagService){
        this.tagService = tagService;
    }

    public void allTags(){
        List<Tag> listaUsavel = new ArrayList<>();

        List<Tag> todasTags = tagService.tagFindAll();
        for (Tag tag : todasTags){
            listaUsavel.add(tag);
        }

        listaRetorno = listaUsavel;
    }

    public List<Tag> retornoDaLista(){
        return listaRetorno;
    }
}
