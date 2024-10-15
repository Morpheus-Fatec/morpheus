package fatec.morpheus.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fatec.morpheus.DTO.MapSourceDTO;
import fatec.morpheus.DTO.NewsSourceDTO;
import fatec.morpheus.entity.ErrorResponse;
import fatec.morpheus.entity.MapSource;
import fatec.morpheus.exception.InvalidFieldException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class MapSourceService {

    @Autowired
    private Validator validator;

    public MapSourceDTO validateMap(NewsSourceDTO newsSourceDTO) {

        MapSourceDTO mapSourceDTO = newsSourceDTO.getMap();
        Set<ConstraintViolation<MapSourceDTO>> mapViolations = validator.validate(mapSourceDTO);

        // Verifica se há violações
        if (!mapViolations.isEmpty()) {
            List<String> errors = mapViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
            errorResponse.setMessage("Campos Vazios");
            throw new InvalidFieldException(errorResponse);
        }

        try {
            MapSource mapSourceResolved = findHtmlTags(mapSourceDTO);
            return mapSourceResolved.toDTO();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar o MapSource", e);
        }
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


            if (elementText.equalsIgnoreCase(text)) {
                String className = element.className();
                if (className != null && !className.isEmpty()) {
                    return className;
                } else {
                    Element parentElement = element.parent(); 
                    if (parentElement != null) {
                        String parentClassName = parentElement.className();
                        if (parentClassName != null && !parentClassName.isEmpty()) {
                            return parentClassName;
                        }
                    }
                }
            }
        }
        return null; 
    }    
       
    private String findDateElement(Document doc) {
        Elements timeElements = doc.select("time");
        for (Element timeElement : timeElements) {
            String datetime = timeElement.attr("datetime");
            String text = timeElement.text();
            
            if (!datetime.isEmpty()) {
                if (!timeElement.className().isEmpty()) {
                    return timeElement.className(); 
                } else {
                    return timeElement.parent().className();
                }
            }

            if (checkDateText(text)) {
                return timeElement.className(); 
            }
        }
    
        Elements elements = doc.select("span, div, p");
        for (Element element : elements) {
            String text = element.text();

            if (checkDateText(text)) {
                return element.className(); 
            }
        }
    
        return null; 
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
