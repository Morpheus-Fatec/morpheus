package fatec.morpheus.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiContentService {
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

    public static void main(String[] args) {
        ApiContentService api = new ApiContentService();
        System.out.println(api.searchContentApi("https://pokeapi.co/api/v2/pokemon/", "dragonait"));
    }
}
