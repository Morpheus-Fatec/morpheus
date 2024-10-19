package fatec.morpheus.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fatec.morpheus.entity.MapSource;
import fatec.morpheus.entity.Tag;
import fatec.morpheus.repository.MapSourceRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScrapingService {

    @Autowired
    private MapSourceRepository mapSourceRepository;

    private Set<String> processedUrls = new HashSet<>();

    public void getSearch() {
        System.out.println("Buscando notícias...");

        List<MapSource> sources = mapSourceRepository.findAll();

        if (sources.isEmpty()) {
            System.out.println("Nenhuma fonte encontrada. Finalizando a aplicação.");
            return;
        }

        for (MapSource source : sources) {
            try {
                String portalUrl = source.getSource().getAddress();

                if (portalUrl == null || portalUrl.isBlank()) {
                    System.err.println("URL do portal está ausente. Pulando para a próxima fonte.");
                    continue;
                }

                System.out.println("PORTAL: " + portalUrl);

                Set<String> tagNames = source.getSource().getTags().stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toSet());

                Map<String, String> tagsClass = Map.of(
                    "content", source.getBody(),
                    "title", source.getTitle(),
                    "date", source.getDate(),
                    "author", source.getAuthor(),
                    "urlNoticiaSalva", portalUrl
                );

                extractNews(portalUrl, tagsClass, tagNames);

            } catch (Exception e) {
                System.out.println("Erro ao processar o portal: " + source.getSource().getAddress());
                e.printStackTrace();
            }
        }
        System.out.println("Processamento de notícias concluído.");
    }

    private void extractNews(String url, Map<String, String> tagsClass, Set<String> tagNames) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements newsCards = document.select("a");

            for (Element card : newsCards) {
                String link = card.attr("href");

                if (!link.startsWith("http")) {
                    link = "https:" + link;
                }

                if (link.startsWith(url) && !processedUrls.contains(link)) {
                    if(tagNames.size() == 0) {
                        System.err.println("O link: " + link + " não possui tags. Seguindo para o proximo portal.");
                        return;
                    }
                    scrapeNewsDetails(link, tagsClass, tagNames);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrapeNewsDetails(String newsUrl, Map<String, String> tagsClass, Set<String> tagNames) {
        try {
            Document newsPage = Jsoup.connect(newsUrl).get();

            String title = newsPage.select(tagsClass.get("title")).text();
            String datePublished = newsPage.select(tagsClass.get("date")).text();
            String author = newsPage.select(tagsClass.get("author")).text();

            Elements contentElements = newsPage.select(tagsClass.get("content"));
            StringBuilder fullContent = new StringBuilder();

            for (Element content : contentElements) {
                fullContent.append(content.text()).append(System.lineSeparator());
            }

            String contentString = fullContent.toString();

            boolean containsTag = tagNames.stream().anyMatch(tag -> contentString.toLowerCase().contains(tag.toLowerCase()));

            if (!containsTag) {
                System.out.println("Conteúdo não contém nenhuma das tags: " + tagNames);
                return;
            }

            // Aqui é onde será implementado a persistencia dos dados no banco de dados.
            // Basta inserir os dados abaixo na tabela.
            System.out.println("Título: " + title);
            System.out.println("Autor: " + author);
            System.out.println("Data: " + datePublished);
            System.out.println("Conteúdo: " + contentString);
            System.out.println("URL: " + newsUrl);

        } catch (IOException e) {
            System.out.println("Erro ao acessar o link: " + newsUrl);
            e.printStackTrace();
        }
    }
}
