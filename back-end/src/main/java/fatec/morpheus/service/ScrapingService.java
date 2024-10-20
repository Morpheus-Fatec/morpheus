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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class ScrapingService {

    @Autowired
    private MapSourceRepository mapSourceRepository;
    @Autowired
    private AdaptedTagsService adaptedTagsService;
    private Set<Tag> foundTags = new HashSet<>();

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

                List<Tag> tags = source.getSource().getTags();
                Map<Tag, List<String>> tagsAndVariations = getTagsVariations(tags);
                // System.out.println("tagsAndVariations: " + tagsAndVariations);
                

                Map<String, String> tagsClass = Map.of(
                    "content", source.getBody(),
                    "title", source.getTitle(),
                    "date", source.getDate(),
                    "author", source.getAuthor(),
                    "urlNoticiaSalva", portalUrl
                );

                extractNews(portalUrl, tagsClass, tagsAndVariations);

            } catch (Exception e) {
                System.out.println("Erro ao processar o portal: " + source.getSource().getAddress());
                e.printStackTrace();
            }
        }
        System.out.println("Processamento de notícias concluído.");
    }

    private void extractNews(String url, Map<String, String> tagsClass, Map<Tag, List<String>> tagsAndVariations) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements newsCards = document.select("a");

            for (Element card : newsCards) {
                String link = card.attr("href");

                if (!link.startsWith("http")) {
                    link = "https:" + link;
                }

                if (link.startsWith(url) && !processedUrls.contains(link)) {
                    if(tagsAndVariations.size() == 0) {
                        System.err.println("O link: " + link + " não possui tags. Seguindo para o proximo portal.");
                        return;
                    }
                    scrapeNewsDetails(link, tagsClass, tagsAndVariations);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrapeNewsDetails(String newsUrl, Map<String, String> tagsClass, Map<Tag, List<String>> tagsAndVariations) {
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
            System.out.println("Verificando conteúdo da notícia...");
    
            boolean foundTag = false;
    
            for (Map.Entry<Tag, List<String>> entry : tagsAndVariations.entrySet()) {
                List<String> variations = entry.getValue();
                for (String variation : variations) {
                    System.out.println("Procurando por variação: " + variation);
                    if (contentString.toLowerCase().contains(variation.toLowerCase())) {
                        System.out.println("Variação encontrada: " + variation);
                        foundTags.add(entry.getKey());
                        foundTag = true;
                        break;
                    }
                }
                if (foundTag) break; // Para assim que uma tag ou variação for encontrada
            }
    
            if (!foundTag) {
                System.out.println("Conteúdo não contém nenhuma das tags.");
                return;
            }
    
            // Aqui é onde será implementada a persistência dos dados no banco de dados.
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
     

    private Map<Tag, List<String>> getTagsVariations(List<Tag> tags) {
        Map<Tag, List<String>> tagsAndVariations  = new HashMap<>();
        for (Tag tag : tags) {
           tagsAndVariations.put(tag, adaptedTagsService.findVariation(tag.getTagName()));
        }
        System.out.println("Tags e variações: ");
        System.out.println(tagsAndVariations);
        System.out.println("-------------------------------------------------");
        return tagsAndVariations;
    }
}
