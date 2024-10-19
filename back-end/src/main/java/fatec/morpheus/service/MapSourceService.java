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
    private List<String> errors = new ArrayList<>();
    private MapSourceDTO mapedSourceDto = new MapSourceDTO();
    private final String notFoundMessage = "Não encontrado elemento HTML correspondente.";

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
        try {
            Document doc = Jsoup.connect(mapSourceDTO.getUrl()).get();
            mapedSourceDto.setUrl(mapSourceDTO.getUrl());
    
            // Título
            String titleClass = findElementContainingText(doc, mapSourceDTO.getTitle());
            String titleClass2 = findElementContainingText2(doc, mapSourceDTO.getTitle());
            if (nullOrEmpty(titleClass) && nullOrEmpty(titleClass2)) {
                mapedSourceDto.setTitle(notFoundMessage);
            } else {
                mapedSourceDto.setTitle("." + (!nullOrEmpty(titleClass) ? titleClass : titleClass2));
            }
    
            // Data
            String dateClass = findElementContainingText(doc, mapSourceDTO.getDate());
            String dateClass2 = findElementContainingText2(doc, mapSourceDTO.getDate());
            if (nullOrEmpty(dateClass) && nullOrEmpty(dateClass2)) {
                mapedSourceDto.setDate(notFoundMessage);
            } else {
                mapedSourceDto.setDate("." + (!nullOrEmpty(dateClass) ? dateClass : dateClass2));
            }
            
            String authorClass = findElementContainingText(doc, mapSourceDTO.getAuthor());
            String authorCLass2 = findElementContainingText2(doc, mapSourceDTO.getAuthor());
            if (nullOrEmpty(authorClass) && nullOrEmpty(authorCLass2)) {
                mapedSourceDto.setAuthor(notFoundMessage);
            } else {
                mapedSourceDto.setAuthor("." + (!nullOrEmpty(authorClass) ? authorClass : authorCLass2));
            }
    
            // Corpo
            String bodyClass = findElementContainingText(doc, mapSourceDTO.getBody());
            String bodyClass2 = findElementContainingText2(doc, mapSourceDTO.getBody());
            if (nullOrEmpty(bodyClass) && nullOrEmpty(bodyClass2)) {
                mapedSourceDto.setBody(notFoundMessage);
            } else {
                mapedSourceDto.setBody("." + (!nullOrEmpty(bodyClass) ? bodyClass : bodyClass2));
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

    private String findElementContainingText2(Document doc, String text) {
        Elements elements = doc.select("a, p, span, div, h1, h2, h3, time");
        for (Element element : elements) {
            String elementText = element.text().toLowerCase();
            String searchText = text.toLowerCase();
            if (elementText.contains(searchText) && isReasonableMatch(elementText, searchText)) {
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
    
    private boolean isReasonableMatch(String elementText, String searchText) {
        // Define um tamanho mínimo de texto para considerar a correspondência
        int minTextLength = 5;
    
        // Calcula a proporção entre o tamanho do texto buscado e o texto do elemento
        double ratio = (double) searchText.length() / elementText.length();
    
        // Considera razoável se o texto do elemento for maior que o mínimo e a proporção for alta o suficiente
        return elementText.length() >= minTextLength && ratio >= 0.5;
    }
    
        
    private boolean nullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

}
