package fatec.morpheus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fatec.morpheus.entity.News;
import fatec.morpheus.service.NewsService;

import java.sql.Date;

@Component
public class NewsRunner implements CommandLineRunner {

    @Autowired
    private NewsService newsService;

    @Override
    public void run(String... args) throws Exception {
        // Dados de exemplo
        String title = "Título de Exemplo";
        String content = "Conteúdo da notícia de exemplo.";
        String author = "Autor de Exemplo";
        Date registryDate = new Date(System.currentTimeMillis());
        Integer sourceId = 1; // ID da fonte de notícia já existente no banco

        // Chama o método de salvar
        News savedNews = newsService.saveNews(title, content, author, registryDate, sourceId);

        // Exibe o resultado
        System.out.println("Notícia salva com ID: " + savedNews.getNewsCod());
    }
}