package fatec.morpheus.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fatec.morpheus.entity.MapSource;
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

    public void getSearch() {
        System.out.println("Buscando notícias...");

        List<MapSource> sources = mapSourceRepository.findAll();

        for (MapSource source : sources) {
            try {
                String portalUrl = source.getSource().getAddress();
                System.out.println("PORTAL: " + portalUrl);

                Map<String, String> tags = Map.of(
                    "content", source.getBody(),
                    "title", source.getTitle(),
                    "date", source.getDate(),
                    "author", source.getAuthor(),
                    "urlNoticiaSalva", portalUrl
                );

                extractNews(portalUrl, tags);

            } catch (Exception e) {
                System.out.println("Erro ao processar o portal: " + source.getSource().getAddress());
                e.printStackTrace();
            }
        }
    }

    private void extractNews(String url, Map<String, String> tags) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements newsCards = document.select("a");

            for (Element card : newsCards) {
                String link = card.attr("href");

                if (!link.startsWith("http")) {
                    link = "https:" + link;
                }

                System.out.println(link);

                if (link.startsWith(url) && !processedUrls.contains(link)) {
                    if (link.contains("https://www.cnnbrasil.com.br") ||
                        link.contains("https://www.metropoles.com") ||
                        link.contains("https://www.g1.globo.com")) {
                        return;
                    }
                    scrapeNewsDetails(link, tags);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrapeNewsDetails(String newsUrl, Map<String, String> tags) {
        try {
            Document newsPage = Jsoup.connect(newsUrl).get();

            String title = newsPage.select(tags.get("title")).text();
            String datePublished = newsPage.select(tags.get("date")).text();

            Elements contentElements = newsPage.select(tags.get("content"));
            StringBuilder fullContent = new StringBuilder();

            for (Element content : contentElements) {
                fullContent.append(content.text()).append(System.lineSeparator());
            }

            String contentString = fullContent.toString();
            
            // if (!contentString.toLowerCase().contains("hoje")) {
            //     System.out.println("Não foi encontrado notícias com a tag 'amanhã'");
            //     return;
            // }

            System.out.println("Título: " + title);
            System.out.println("Data: " + datePublished);
            System.out.println("Conteúdo: " + contentString);
            System.out.println("URL: " + newsUrl);

        } catch (IOException e) {
            System.out.println("Erro ao acessar o link: " + newsUrl);
            e.printStackTrace();
        }
    }
}

