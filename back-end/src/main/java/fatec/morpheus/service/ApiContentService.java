package fatec.morpheus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiContentService {
    @Autowired
    private RestTemplate restTemplate;

    public String searchContent(String url, String tag){
        String content = restTemplate.getForObject(url, String.class);
        return content;
    } 

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ApiContentService api = new ApiContentService();
        api.restTemplate = restTemplate; // Injeção manual
        System.out.println(api.searchContent("https://api.dicionario-aberto.net/random", "pele"));
    }
}
