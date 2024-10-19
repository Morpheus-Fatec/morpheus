package fatec.morpheus.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fatec.morpheus.DTO.MapSourceDTO;
import fatec.morpheus.entity.ErrorResponse;
import fatec.morpheus.exception.InvalidFieldException;


@Service
public class MapSourceService {

    private List<String> errors = new ArrayList<>();
    private MapSourceDTO mapedSourceDto = new MapSourceDTO();
    private final String notFoundMessage = "Não encontrado elemento HTML correspondente.";

    public MapSourceDTO validateMap(MapSourceDTO mapSourceDTO) {
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
            mapedSourceDto.setUrl(mapSourceDTO.getUrl());
            if (mapSourceDTO.getUrl() == null || !mapSourceDTO.getUrl().startsWith("http://") && !mapSourceDTO.getUrl().startsWith("https://")) {
                throw new MalformedURLException("URL inválida: " + mapSourceDTO.getUrl());
            }

            Document doc = Jsoup.connect(mapSourceDTO.getUrl()).get();

            // Título
            if (!nullOrEmpty(mapSourceDTO.getTitle())) {
                String titleClass = findElementContainingText(doc, mapSourceDTO.getTitle());
                if (nullOrEmpty(titleClass)) {
                    String titleClass2 = findElementContainingText2(doc, mapSourceDTO.getTitle());
                    mapedSourceDto.setTitle(nullOrEmpty(titleClass2) ? notFoundMessage : "." + titleClass2);
                } else {
                    mapedSourceDto.setTitle("." + titleClass);
                }
            }
            
            // Data
            String dateClass = findElementContainingText(doc, mapSourceDTO.getDate());
            String dateClassTime = findFirstDateElement(doc, mapSourceDTO.getDate());

            if (!nullOrEmpty(dateClass)) {
                if (dateClass.equals(dateClassTime)) {
                    mapedSourceDto.setDate("." + dateClass);
                } else {
                    mapedSourceDto.setDate("." + dateClassTime);
                }
            } else {
                String dateClass2 = findElementContainingText2(doc, mapSourceDTO.getDate());
                mapedSourceDto.setDate(nullOrEmpty(dateClass2) ? notFoundMessage : "." + dateClass2);
            }

            // Autor
            String authorClass = findElementContainingText(doc, mapSourceDTO.getAuthor());
            if (nullOrEmpty(authorClass)) {
                String authorClass2 = findElementContainingText2(doc, mapSourceDTO.getAuthor());
                mapedSourceDto.setAuthor(nullOrEmpty(authorClass2) ? notFoundMessage : "." + authorClass2);
            } else {
                mapedSourceDto.setAuthor("." + authorClass);
            }

            // Corpo
            String bodyClass = findElementContainingText(doc, mapSourceDTO.getBody());
            if (nullOrEmpty(bodyClass)) {
                String bodyClass2 = findParentClassOfBody(doc, mapSourceDTO.getBody());
                mapedSourceDto.setBody(nullOrEmpty(bodyClass2) ? notFoundMessage : "." + bodyClass2);
            } else {
                mapedSourceDto.setBody("." + bodyClass);
            }
    
        } catch (MalformedURLException e) {
            e.printStackTrace();
            errors.add(mapSourceDTO.getUrl());
            throw new InvalidFieldException(new ErrorResponse(HttpStatus.BAD_REQUEST, errors, "URL inválida."));
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
