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
import fatec.morpheus.exception.NotFoundException;
import fatec.morpheus.exception.UniqueConstraintViolationException;
import fatec.morpheus.repository.NewsSourceRepository;
import jakarta.validation.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class NewsSourceService {

    @Autowired
    private NewsSourceRepository newsSourceRepository;

    @Autowired
    private Validator validator;

    public NewsSource createNewsSource(NewsSourceDTO newsSourceDTO) {

        NewsSource source = new NewsSource();
        source.setSrcName(newsSourceDTO.getName());
        source.setAddress(newsSourceDTO.getAddress());
        
        MapSourceDTO MapSourceDTO = new MapSourceDTO();
        MapSourceDTO.setSource(source);
        MapSourceDTO.setAuthor(newsSourceDTO.getMap().getAuthor());
        MapSourceDTO.setBody(newsSourceDTO.getMap().getBody());
        MapSourceDTO.setTitle(newsSourceDTO.getMap().getTitle());
        MapSourceDTO.setUrl(newsSourceDTO.getMap().getUrl());
        MapSourceDTO.setDate(newsSourceDTO.getMap().getDate());  
        MapSource mapSourceResolved = findHtmlTags(MapSourceDTO);

        // Set<ConstraintViolation<NewsSource>> violations = validator.validate(source);

        // if (!violations.isEmpty()) {
        //     List<String> errors = violations.stream()
        //         .map(ConstraintViolation::getMessage)
        //         .collect(Collectors.toList());

        //     ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        //     throw new InvalidFieldException(errorResponse);
        // }

        // try {
        //     return newsSourceRepository.save(source);
                
        // } catch (Exception e) {
        //     List<String> duplicateFields = this.verifyUniqueKeys(source);
            
        //     ErrorResponse errorResponse = new ErrorResponse(
        //         HttpStatus.CONFLICT,   
        //         duplicateFields       
        //     );

        //     throw new UniqueConstraintViolationException(errorResponse);
        // }
        return null;
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
        try {
            Document doc = Jsoup.connect(mapSourceDTO.getUrl()).get();

            String title = mapSourceDTO.getTitle();
            String titleClass = findElementContainingText(doc, title);
            if (titleClass != null) {
                System.out.println("Classe do título: " + titleClass);
            }else{
                System.out.println("Título não encontrado.");
            }
    
            String dateClass = findDateElement(doc);
            if (dateClass != null) {
                System.out.println("class da data: " + dateClass);
                                   
            }else{
                System.out.println("data nao encontrado");  
            } 

            String author = mapSourceDTO.getAuthor();
            if (author != null) {
                String authorClass = findElementContainingText(doc, author);
                System.out.println("class Autor: " + authorClass);
            } else {
                System.out.println("Autor não encontrado.");
            }

            String body = mapSourceDTO.getBody();
            if (body != null) {
                String bodyClass = findBodyElement(doc, body);
                String bodyteste = findElementContainingText(doc, bodyClass);
                System.out.println("class do corpo: " + bodyClass);
            } else {
                System.out.println("Corpo não encontrado.");
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }
    
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
                    System.out.println("\n" +"Texto encontrado: " + elementText + "\n");
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

    // Deve procurar um elemento que tem o texto do body, e deve retornar a classe do pai
    private String findBodyElement(Document doc, String body) {
        // Seleciona todos os elementos relevantes
        Elements elements = doc.select("a, span, div, p, h1, h2, h3");
    
        for (Element element : elements) {
            String elementText = element.text();
            
            // Compara o texto do elemento com o texto esperado
            if (elementText.equalsIgnoreCase(body)) {
                System.out.println("Texto encontrado no corpo: " + elementText);
                // Pega o elemento pai mais distante com uma classe não vazia
                Element parentElement = element.parent(); // Começa com o pai
                while (parentElement != null) {
                    String parentClassName = parentElement.className();
                    // Se encontrar uma classe não vazia, retorna
                    if (parentClassName != null && !parentClassName.isEmpty()) {
                        System.out.println("Texto encontrado no elemento pai: " + elementText);
                        return parentClassName; // Retorna a classe do pai mais distante
                    }
                    parentElement = parentElement.parent(); // Sobe para o próximo pai
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
