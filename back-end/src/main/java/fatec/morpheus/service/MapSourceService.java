package fatec.morpheus.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.net.MalformedURLException;

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

import fatec.morpheus.DTO.MappingDTO;

@Service
public class MapSourceService {

    private List<String> errors = new ArrayList<>();
    private MappingDTO mappingDTOResolved = new MappingDTO();

    public MappingDTO validateMap(MappingDTO mapSourceDTO) {
        try {
            MappingDTO mapSourceDTOResolved = findHtmlTags(mapSourceDTO);
            return mapSourceDTOResolved;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar o MapSource", e);
        }
    }


    private MappingDTO findHtmlTags(MappingDTO mappingDTO) {
        try {
            mappingDTOResolved.setUrl(mappingDTO.getUrl());
            if (mappingDTO.getUrl() == null || !mappingDTO.getUrl().startsWith("http://") && !mappingDTO.getUrl().startsWith("https://")) {
                throw new MalformedURLException("URL inválida: " + mappingDTO.getUrl());
            }

            Document doc = Jsoup.connect(mappingDTO.getUrl()).get();

            // Título
            if (!nullOrEmpty(mappingDTO.getTitle())) {
                String titleClass = findElementContainingText(doc, mappingDTO.getTitle());
                if (nullOrEmpty(titleClass)) {
                    String titleClass2 = findElementContainingText2(doc, mappingDTO.getTitle());
                    mappingDTOResolved.setTitle(nullOrEmpty(titleClass2) ? null : "." + titleClass2);
                } else {
                    mappingDTOResolved.setTitle("." + titleClass);
                }
            } else {
                mappingDTOResolved.setTitle(null);
            }
            
            // Data
            if (!nullOrEmpty(mappingDTO.getDate())) {
                String dateClass = findElementContainingText(doc, mappingDTO.getDate());
                String dateClassTime = findFirstDateElement(doc, mappingDTO.getDate());
                if (!nullOrEmpty(dateClass)) {
                    if (dateClass.equals(dateClassTime)) {
                        mappingDTOResolved.setDate("." + dateClass);
                    } else {
                        mappingDTOResolved.setDate("." + dateClassTime);
                    }
                } else {
                    String dateClass2 = findElementContainingText2(doc, mappingDTO.getDate());
                    mappingDTOResolved.setDate(nullOrEmpty(dateClass2) ? null : "." + dateClass2);
                }
            } else {
                mappingDTOResolved.setDate(null);
            }


            // Autor
            if (!nullOrEmpty(mappingDTO.getAuthor())) {
                String authorClass = findElementContainingText(doc, mappingDTO.getAuthor());
                if (nullOrEmpty(authorClass)) {
                    String authorClass2 = findElementContainingText2(doc, mappingDTO.getAuthor());
                    mappingDTOResolved.setAuthor(nullOrEmpty(authorClass2) ? null : "." + authorClass2);
                } else {
                    mappingDTOResolved.setAuthor("." + authorClass);
                }
            } else{
                mappingDTOResolved.setAuthor(null);
            }

            // Corpo
            if (!nullOrEmpty(mappingDTO.getBody())) {
                String bodyClass = findElementContainingText(doc, mappingDTO.getBody());
                if (nullOrEmpty(bodyClass)) {
                    String bodyClass2 = findParentClassOfBody(doc, mappingDTO.getBody());
                    mappingDTOResolved.setBody(nullOrEmpty(bodyClass2) ? null : "." + bodyClass2);
                } else {
                    mappingDTOResolved.setBody("." + bodyClass);
                }
            } else{
                mappingDTOResolved.setBody(null);
            }
    
        } catch (MalformedURLException e) {
            e.printStackTrace();
            errors.add(mappingDTO.getUrl());
            throw new InvalidFieldException(new ErrorResponse(HttpStatus.BAD_REQUEST, errors, "URL inválida."));
        } catch (IOException e) {
            e.printStackTrace();
            errors.add(mappingDTO.getUrl());
            throw new InvalidFieldException(new ErrorResponse(HttpStatus.BAD_REQUEST, errors, "Erro ao acessar a URL."));
        }
    
        return mappingDTOResolved;
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
    
    private String findParentClassOfBody(Document doc, String text) {
        Elements elements = doc.body().select("a, span, div, p");
        for (Element element : elements) {
            String elementText = element.text();
    
            if (elementText.toLowerCase().contains(text.toLowerCase())) {
                Element parentElement = element.parent(); 
                if (parentElement != null) {
                    String parentClassName = parentElement.className();
                    if (parentClassName != null && !parentClassName.isEmpty()) {
                        return parentClassName; 
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

    public String findFirstDateElement(Document doc, String expectedDate) {
        // Procura todos os elementos <time> no documento
        Elements timeElements = doc.select("time");
    
        for (Element timeElement : timeElements) {
            String dateText = timeElement.text().trim();


            if (dateText.contains(expectedDate)) {
                String timeClass = timeElement.className();
                if (!timeClass.isEmpty()) {
                    return timeClass;
                } else {
                    Element parent = timeElement.parent();
                    if (parent != null && !parent.className().isEmpty()) {
                        return parent.className();
                    }
                }
            }
        }
        return null;
    }
    
        
    private boolean nullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

}
