package fatec.morpheus.service;

import java.io.IOException;
import java.util.ArrayList;
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
import fatec.morpheus.entity.ErrorResponse;
import fatec.morpheus.exception.InvalidFieldException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class MapSourceService {

    @Autowired
    private Validator validator;

    public MapSourceDTO validateMap(MapSourceDTO mapSourceDTO) {
        Set<ConstraintViolation<MapSourceDTO>> mapViolations = validator.validate(mapSourceDTO);

        if (!mapViolations.isEmpty()) {
            List<String> errors = mapViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
            errorResponse.setMessage("Campos Vazios");
            throw new InvalidFieldException(errorResponse);
        }

        try {
            MapSourceDTO mapSourceDTOResolved = findHtmlTags(mapSourceDTO);
            return mapSourceDTOResolved;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar o MapSource", e);
        }
    }


    private MapSourceDTO findHtmlTags(MapSourceDTO mapSourceDTO) {
        MapSourceDTO mapedSourceDto = new MapSourceDTO();
        mapedSourceDto.setUrl(mapSourceDTO.getUrl());
        List<String> errors = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(mapSourceDTO.getUrl()).get();

            String titleClass = findElementContainingText(doc, mapSourceDTO.getTitle());
            if (titleClass == null) {
                errors.add("O título não foi encontrado no HTML.");
            } else {
                mapedSourceDto.setTitle(titleClass);
            }

            String dateClass = findDateElement(doc);
            if (dateClass == null) {
                errors.add("A data não foi encontrada no HTML.");
            } else {
                mapedSourceDto.setDate(dateClass);
            }

            if (mapSourceDTO.getAuthor().isBlank()) {
                mapSourceDTO.setAuthor(null);
            }else{
                String authorClass = findElementContainingText(doc, mapSourceDTO.getAuthor());
                mapedSourceDto.setAuthor(authorClass);
            }
            

            String bodyClass = findElementContainingText(doc, mapSourceDTO.getBody());
            if (bodyClass == null) {
                errors.add("O corpo não foi encontrado no HTML.");
            } else {
                mapedSourceDto.setBody(bodyClass);
            }

            if (!errors.isEmpty()) {
                throw new InvalidFieldException(new ErrorResponse(HttpStatus.BAD_REQUEST, errors, "Não encontrado elemento HTML correspondente."));
            }

        } catch (IOException e) {
            e.printStackTrace();
            errors.add(mapSourceDTO.getUrl());
            throw new InvalidFieldException(new ErrorResponse(HttpStatus.BAD_REQUEST, errors, "Erro ao acessar a URL."));
        }

        return mapedSourceDto;
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
