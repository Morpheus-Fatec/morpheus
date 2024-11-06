package fatec.morpheus.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fatec.morpheus.entity.MapSource;
import fatec.morpheus.entity.News;
import fatec.morpheus.entity.NewsAuthor;
import fatec.morpheus.entity.NewsSource;
import fatec.morpheus.repository.MapSourceRepository;
import fatec.morpheus.repository.NewsAuthorRepository;
import fatec.morpheus.repository.NewsRepository;
import fatec.morpheus.repository.NewsSourceRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ScrapingService {

    @Autowired
    private MapSourceRepository mapSourceRepository;

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsAuthorRepository newsAuthorRepository;

    @Autowired
    private NewsSourceRepository newsSourceRepository;

    @Autowired
    private AdaptedTagsService adaptedTagsService;

    @Autowired
    private NewsRepository newsRepository;

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

                String portalCodeUrl = String .valueOf(source.getSource().getCode());

                if (portalUrl == null || portalUrl.isBlank()) {
                    System.err.println("URL do portal está ausente. Pulando para a próxima fonte.");
                    continue;
                }

                System.out.println("PORTAL: " + portalUrl);

                List<String> tagsAndVariations = findVariation(source.getSource().getCode());

                Map<String, String> tagsClass = Map.of(
                    "content", source.getBody(),
                    "title", source.getTitle(),
                    "date", source.getDate(),
                    "author", source.getAuthor(),
                    "urlNoticiaSalva", portalUrl
                );

                extractNews(portalUrl, tagsClass, tagsAndVariations, portalCodeUrl);

            } catch (Exception e) {
                System.out.println("Erro ao processar o portal: " + source.getSource().getAddress());
                e.printStackTrace();
            }
        }
        System.out.println("Processamento de notícias concluído.");
    }

    private void extractNews(String url, Map<String, String> tagsClass, List<String> tagsAndVariations, String portalCodeUrl) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements newsCards = document.select("a");

            for (Element card : newsCards) {
                String link = card.attr("href");

                if (!link.startsWith("http")) {
                    link = "https:" + link;
                }

                if (tagsClass.get("title").isEmpty() || tagsClass.get("content").isEmpty() || 
                    tagsClass.get("date").isEmpty() || tagsClass.get("author").isEmpty()) {
                    System.err.println("Uma ou mais chaves de tagsClass estão vazias.");
                    return;
                }

                if (link.startsWith(url) && !processedUrls.contains(link)) {
                    if(tagsAndVariations.size() == 0) {
                        System.err.println("O link: " + link + " não possui tags. Seguindo para o proximo portal.");
                        return;
                    }
                    processedUrls.add(link);
                    scrapeNewsDetails(link, tagsClass, tagsAndVariations, portalCodeUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrapeNewsDetails(String newsUrl, Map<String, String> tagsClass, List<String> tagsAndVariations, String portalCodeUrl) {
        try {
            Document newsPage = Jsoup.connect(newsUrl).get();

            if (newsRepository.existsByNewAddress(newsUrl)) {
                System.out.println("Notícia já existente no banco de dados: " + newsUrl);
                return;
            }

            String title = newsPage.select(tagsClass.get("title")).text();
            String datePublished = newsPage.select(tagsClass.get("date")).text();

            String authorName = newsPage.select(tagsClass.get("author")).text();

            Elements contentElements = newsPage.select(tagsClass.get("content"));
            StringBuilder fullContent = new StringBuilder();

            for (Element content : contentElements) {
                fullContent.append(content.text()).append(System.lineSeparator());
            }

            String contentString = fullContent.toString();

            boolean containsTag = tagsAndVariations.stream().anyMatch(tag -> contentString.toLowerCase().contains(tag.toLowerCase()));

            if (!containsTag) {
                System.out.println("Conteúdo não contém nenhuma das tags: " + tagsAndVariations);
                return;
            }

            NewsAuthor newsAuthor = newsAuthorRepository.findByAutName(authorName);
            if (newsAuthor == null) {
                newsAuthor = new NewsAuthor();
                newsAuthor.setAutName(authorName);
                newsAuthor = newsAuthorRepository.save(newsAuthor);
            }

            News news = new News();
            news.setNewsTitle(title);
            news.setNewsContent(contentString);
            news.setNewAddress(newsUrl);
            news.setNewsAuthor(newsAuthor);

            int srcCod = Integer.parseInt(portalCodeUrl);
            NewsSource sourceNews = newsSourceRepository.findByCode(srcCod);
            if (sourceNews != null) {
                news.setSourceNews(sourceNews);
            } else {
                System.out.println("Fonte não encontrada para src_cod: " + srcCod);
                return;
            }

            newsService.saveNews(news);
            System.out.println("Notícia salva: " + title);

        } catch (IOException e) {
            System.out.println("Erro ao acessar o link: " + newsUrl);
            e.printStackTrace();
        }
    }

    private List<String> findVariation(int newsSourceCode){
        List<String> tagsAndVariations = adaptedTagsService.findVariation(newsSourceCode);
        return tagsAndVariations;
    }

}