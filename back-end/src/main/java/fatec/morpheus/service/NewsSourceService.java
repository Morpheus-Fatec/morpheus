package fatec.morpheus.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fatec.morpheus.DTO.MapSourceDTO;
import fatec.morpheus.DTO.NewsSourceDTO;
import fatec.morpheus.entity.ErrorResponse;
import fatec.morpheus.entity.MapSource;
import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.exception.InvalidFieldException;
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.exception.UniqueConstraintViolationException;
import fatec.morpheus.repository.MapSourceRepository;
import fatec.morpheus.repository.NewsSourceRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class NewsSourceService {

    @Autowired
    private NewsSourceRepository newsSourceRepository;

    @Autowired
    private MapSourceRepository mapSourceRepository;

    @Autowired
    private Validator validator;

    private NewsSource source = new NewsSource();
    private NewsSourceDTO newsSourceDTO = new NewsSourceDTO();
    private MapSourceDTO MapSourceDTO = new MapSourceDTO();

    public NewsSourceDTO createNewsSource(NewsSourceDTO newsSourceCreatedDTO) {

        source.setSrcName(newsSourceCreatedDTO.getSrcName());
        source.setAddress(newsSourceCreatedDTO.getAddress());

        MapSourceDTO.setAuthor(newsSourceCreatedDTO.getMap().getAuthor());
        MapSourceDTO.setBody(newsSourceCreatedDTO.getMap().getBody());
        MapSourceDTO.setTitle(newsSourceCreatedDTO.getMap().getTitle());
        MapSourceDTO.setUrl(newsSourceCreatedDTO.getMap().getUrl());
        MapSourceDTO.setDate(newsSourceCreatedDTO.getMap().getDate());
        Set<ConstraintViolation<NewsSource>> sourceViolations = validator.validate(source);
        Set<ConstraintViolation<MapSourceDTO>> mapviolations = validator.validate(MapSourceDTO);
    
        if (!sourceViolations.isEmpty()) {
            List<String> errors = sourceViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
            throw new InvalidFieldException(errorResponse);
        }

        if (!mapviolations.isEmpty()) {
            List<String> errors = mapviolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
            throw new InvalidFieldException(errorResponse);
        }

        try {
            // Processa as tags HTML e cria MapSource a partir delas
            MapSource mapSourceResolved = findHtmlTags(MapSourceDTO);
            mapSourceResolved.setSource(source); // Associa o source ao MapSource
        
            // Salva o source no banco de dados primeiro
            newsSourceRepository.save(source);
            
            // Agora que o source foi salvo, ele possui um ID, então podemos salvar o MapSource
            mapSourceRepository.save(mapSourceResolved);
        
            // Cria um DTO a partir do mapSourceResolved para retornar
            MapSourceDTO mapedSourceDTO = new MapSourceDTO();
            mapedSourceDTO.setAuthor(mapSourceResolved.getAuthor());
            mapedSourceDTO.setBody(mapSourceResolved.getBody());
            mapedSourceDTO.setTitle(mapSourceResolved.getTitle());
            mapedSourceDTO.setUrl(mapSourceResolved.getUrl());
            mapedSourceDTO.setDate(mapSourceResolved.getDate().toString());
            
            // Atualiza os dados do DTO para retorno
            newsSourceDTO.setSrcName(source.getSrcName());
            newsSourceDTO.setAddress(source.getAddress());
            newsSourceDTO.setMap(mapedSourceDTO);
        
            System.out.println(mapSourceResolved.toString());
        
            return newsSourceDTO;
                
        } catch (Exception e) {
            List<String> duplicateFields = this.verifyUniqueKeys(source);
            
            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,   
                duplicateFields       
            );

            throw new UniqueConstraintViolationException(errorResponse);
        }
    }

    private List<String> verifyUniqueKeys(NewsSource newsSource) {
        List<String> duplicateFields = new ArrayList<>();
        if (newsSourceRepository.existsBySrcName(newsSource.getSrcName())) {
            duplicateFields.add("srcName");
        }
        if (newsSourceRepository.existsByAddress(newsSource.getAddress())) {
            duplicateFields.add("address");
        }
        return duplicateFields;
    }

    public List<NewsSource> findAllNewsSources() {
        return newsSourceRepository.findAll();
    }

    public NewsSource findNewsSourceById(int id) {
        return newsSourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));  
    }

    public NewsSource updateNewsSourceById(int id, NewsSource newsSourceToUpdate) {
        try {
        return newsSourceRepository.findById(id)
                .map(existingNewsSource -> {
                    existingNewsSource.setSrcName(newsSourceToUpdate.getSrcName());
                    existingNewsSource.setType(newsSourceToUpdate.getType());
                    existingNewsSource.setAddress(newsSourceToUpdate.getAddress());
                    existingNewsSource.getTags().clear();
                    existingNewsSource.getTags().addAll(newsSourceToUpdate.getTags());

                    return newsSourceRepository.save(existingNewsSource);
                })
                .orElseThrow(() -> new NotFoundException(id));
        } catch (DataIntegrityViolationException e) {
            List<String> duplicateFields = this.verifyUniqueKeys(newsSourceToUpdate);

            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,  
                duplicateFields
            );
            throw new UniqueConstraintViolationException(errorResponse);
        }
    }
    

    public NewsSource deleteNewsSourceById(int id) {
        return newsSourceRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(id));
    }

    private MapSource findHtmlTags(MapSourceDTO mapSourceDTO) {
        MapSource mapedSource = new MapSource();
        mapedSource.setUrl(mapSourceDTO.getUrl());
        try {
            Document doc = Jsoup.connect(mapSourceDTO.getUrl()).get();
            System.out.println("URL: " + mapSourceDTO.getUrl());
            String title = mapSourceDTO.getTitle();
            String titleClass = findElementContainingText(doc, title);
            mapedSource.setTitle(titleClass);

            String dateClass = findDateElement(doc);
            mapedSource.setDate(dateClass);
           
            String author = mapSourceDTO.getAuthor();
            String authorClass = findElementContainingText(doc, author);
            mapedSource.setAuthor(authorClass);
             

            String body = mapSourceDTO.getBody();
            String bodyClass = findElementContainingText(doc, body);
            mapedSource.setBody(bodyClass);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        return mapedSource;
    }    
    
    private String findElementContainingText(Document doc, String text) {
        Elements elements = doc.select("a, span, div, p, h1, h2, h3, time");
        for (Element element : elements) {
            String elementText = element.text();
    
            // Compara o texto completo do elemento com o texto esperado
            if (elementText.equalsIgnoreCase(text)) {
                String className = element.className();
    
                // Verifica se o elemento possui uma classe não vazia
                if (className != null && !className.isEmpty()) {
                    return className;
                } else {
                    // Tenta pegar a classe do elemento pai
                    Element parentElement = element.parent(); // Pega o elemento pai
                    if (parentElement != null) {
                        String parentClassName = parentElement.className();
                        if (parentClassName != null && !parentClassName.isEmpty()) {
                            return parentClassName;
                        }
                    }
                }
            }
        }
        return null; // Retorna null se não encontrar
    }    
       
    private String findDateElement(Document doc) {
        // Primeiro, tenta encontrar todas as tags <time> no documento
        Elements timeElements = doc.select("time");
        for (Element timeElement : timeElements) {
            String datetime = timeElement.attr("datetime");
            String text = timeElement.text();
            
            // Verifica se o atributo datetime está presente e não está vazio
            if (!datetime.isEmpty()) {
                return timeElement.tagName(); // Retorna a classe do elemento <time> se o atributo datetime estiver presente
            }
            
            // Verifica o texto do elemento <time> se ele contém um formato de data conhecido
            if (checkDateText(text)) {
                return timeElement.className(); // Retorna a classe se o texto contiver uma data
            }
        }
    
        // Se nenhum elemento <time> válido for encontrado, tenta outras tags
        Elements elements = doc.select("span, div, p");
        for (Element element : elements) {
            String text = element.text();
    
            // Verificações para diversos formatos de data
            if (checkDateText(text)) {
                return element.className(); // Retorna a classe do elemento se o texto contiver uma data potencial
            }
        }
    
        return null; // Retorna null se nenhum elemento corresponder
    }  

    private boolean checkDateText(String text){
        if (text.matches(".*\\d{2}/\\d{2}/\\d{4}.*") || // DD/MM/AAAA
            text.matches(".*\\d{4}-\\d{2}-\\d{2}.*") || // YYYY-MM-DD
            text.matches(".*\\d{4}/\\d{2}/\\d{2}.*") || // YYYY/MM/DD
            text.matches(".*\\d{2}\\.\\d{2}\\.\\d{4}.*") || // DD.MM.AAAA
            text.matches(".*\\d{2} \\w+ \\d{4}.*") // DD mês AAAA
        ) {
            return true;
        }
        return false;
    }
    
}
