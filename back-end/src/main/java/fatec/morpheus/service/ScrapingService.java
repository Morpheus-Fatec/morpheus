package fatec.morpheus.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import fatec.morpheus.entity.ResponseDTO;

import java.io.IOException;
import java.util.*;

@Service
public class ScrapingService {

  private final List<Map<String, Object>> portals = Arrays.asList(
      Map.of(
          "portal", "https://g1.globo.com/",
          "tags", new HashMap<>(Map.of(
              "content", ".content-text__container",
              "title", ".content-head__title",
              "date", "[itemprop=datePublished]",
              "urlNoticiaSalva", ""))),
      Map.of(
          "portal", "https://www.cnnbrasil.com.br/",
          "tags", new HashMap<>(Map.of(
              "content", ".single-content p",
              "title", ".single-header__title",
              "date", ".single-header__time",
              "urlNoticiaSalva", ""))));

  private Set<String> processedUrls = new HashSet<>();

  public Set<ResponseDTO> getSearch() {
    Set<ResponseDTO> responseDTOS = new HashSet<>();
    System.out.println("Buscando notícias...");

    for (Map<String, Object> portalData : portals) {
      try {
        String portalUrl = (String) portalData.get("portal");
        @SuppressWarnings("unchecked")
        Map<String, String> tags = (Map<String, String>) portalData.get("tags");

        System.out.println("Processando portal: " + portalUrl);
        extractNews(responseDTOS, portalUrl, tags);

      } catch (Exception e) {
        System.out.println("Erro ao processar o portal: " + portalData.get("portal"));
        e.printStackTrace();
      }
    }

    return responseDTOS;
  }

  private void extractNews(Set<ResponseDTO> responseDTOS, String url, Map<String, String> tags) {
    try {
      Document document = Jsoup.connect(url).get();
      Elements newsCards = document.select("a[href]");

      for (Element card : newsCards) {
        String link = card.attr("href");

        if (!link.startsWith("http")) {
          link = "https:" + link;
        }

        if (link.startsWith(url) && !processedUrls.contains(link)) {
          processedUrls.add(link);
          scrapeNewsDetailsG1(responseDTOS, link, tags);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void scrapeNewsDetailsG1(Set<ResponseDTO> responseDTOS, String newsUrl, Map<String, String> tags) {
    try {
      Document newsPage = Jsoup.connect(newsUrl).get();

      Elements contentElements = newsPage.select(tags.get("content"));
      if (contentElements.isEmpty())
        return;

      StringBuilder fullContent = new StringBuilder();
      for (Element content : contentElements) {
        fullContent.append(content.text()).append(System.lineSeparator());
      }

      if (!fullContent.toString().contains("clientes")) {
        System.out.println("nao tem clientes");
        return;
      }

      String title = newsPage.select(tags.get("title")).text();
      String datePublished = newsPage.select(tags.get("date")).attr("content");

      System.out.println("Url: " + newsUrl);
      System.out.println("Título: " + title);
      System.out.println("Conteúdo: " + fullContent);
      System.out.println("Data: " + datePublished);

      tags.put("urlNoticiaSalva", newsUrl);

    } catch (IOException e) {
      System.out.println("Erro ao acessar o link: " + newsUrl);
      e.printStackTrace();
    }
  }
}
