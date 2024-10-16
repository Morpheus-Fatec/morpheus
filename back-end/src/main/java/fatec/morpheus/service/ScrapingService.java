package fatec.morpheus.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fatec.morpheus.entity.MapSource;
import fatec.morpheus.entity.ResponseDTO;
import fatec.morpheus.repository.MapSourceRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ScrapingService {

    @Autowired
    private MapSourceRepository mapSourceRepository;

    private Set<String> processedUrls = new HashSet<>();

    public Set<ResponseDTO> getSearch() {
        Set<ResponseDTO> responseDTOS = new HashSet<>();
        System.out.println("Buscando notícias...");

        List<MapSource> sources = mapSourceRepository.findAll();

        for (MapSource source : sources) {
            try {
                String portalUrl = source.getUrl();
                System.out.println("PORTAL: " + portalUrl);

                Map<String, String> tags = Map.of(
                    "content", source.getBody(),
                    "title", source.getTitle(),
                    "date", source.getDate(),   
                    "author", source.getAuthor(),
                    "urlNoticiaSalva", portalUrl
                );

                System.out.println("Tags: " + tags);

                extractNews(responseDTOS, portalUrl, tags);

            } catch (Exception e) {
                System.out.println("Erro ao processar o portal: " + source.getUrl());
                e.printStackTrace();
            }
        }

        return responseDTOS;
    }

    private void extractNews(Set<ResponseDTO> responseDTOS, String url, Map<String, String> tags) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements newsCards = document.select("a");

            for (Element card : newsCards) {
                String link = card.attr("href");

                if (!link.startsWith("http")) {
                  link = "https:" + link;
                }

                if (link.startsWith(url) && !processedUrls.contains(link)) {
                  System.out.println("Link: " + link);
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
            // System.out.println("URL: " + newsPage);

            String fullContent = newsPage.select(tags.get("content")).text();
            String title = newsPage.select(tags.get("title")).text();
            String datePublished = newsPage.select(tags.get("date")).attr("content");

            // System.out.println("Url: " + newsUrl);
            // System.out.println("Title: " + title);
            // System.out.println("Date Published: " + datePublished);
            // System.out.println("Content: " + fullContent);

        } catch (IOException e) {
            System.out.println("Erro ao acessar o link: " + newsUrl);
            e.printStackTrace();
        }
    }
}
