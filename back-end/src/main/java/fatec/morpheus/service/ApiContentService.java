package fatec.morpheus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fatec.morpheus.entity.TagRelFont;
import fatec.morpheus.repository.TagRelFontRepository;

@Service
public class ApiContentService {
    @Autowired
    private TagRelFontRepository tagRelFontRepository;

    public String searchContentApi(String url, String tag){
        RestTemplate restTemplateObj = new RestTemplate();

        String content = restTemplateObj.getForObject(url, String.class);
        String contentToReturn = "Não há nenhum conteúdo com a tag "+tag;

        try{
            if (url != null && tag != null){
                boolean busca = content.contains(tag);
    
                if (busca){
                    contentToReturn = content;
                }
            } else {
                return "Faltando dados nos parametros";
            }

            return contentToReturn;
        } catch (Exception e){
            e.printStackTrace();
            return "Erro na requisição";
        }
    }

    public String testeUrl(){
        List<TagRelFont> relsList = tagRelFontRepository.findAll();

        for(TagRelFont tag : relsList){
            return tag.getTagId().toString();
        }

        return "";

    }

    public static void main(String[] args) {
        ApiContentService api = new ApiContentService();
        // System.out.println(api.searchContentApi("https://pokeapi.co/api/v2/pokemon/", "dragonait"));
        System.out.println(api.testeUrl());
    }
}
