package fatec.morpheus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
// import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import fatec.morpheus.entity.News;
import fatec.morpheus.entity.NewsAuthor;
import fatec.morpheus.repository.NewsAuthorRepository;
import fatec.morpheus.repository.NewsRepository;
import fatec.morpheus.service.NewsService;

@SpringBootTest
class MorpheusApplicationTests {

   @Mock
    private NewsRepository newsRepository;  // Simula o repositório de News

    @Mock
    private NewsAuthorRepository newsAuthorRepository;  // Simula o repositório de NewsAuthor

    @InjectMocks
    private NewsService newsService;  // A classe de serviço que estamos testando

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);  // Inicializa os mocks antes de cada teste
    }

	@Test
	public void testSaveNews_Success() {
		// Dados de entrada
		String title = "Sample Title";
		String content = "Sample Content";
		String autName = "John Doe";
		Date registryDate = new Date(System.currentTimeMillis());
	
		// Mockar o comportamento do repositório de autor
		NewsAuthor author = new NewsAuthor();
		author.setAutName(autName);
		when(newsAuthorRepository.findByAutName(autName)).thenReturn(Optional.of(author));
	
		// Mockar o comportamento do repositório de notícias
		News news = new News();
		news.setNewsTitle(title);
		news.setNewsContent(content);
		news.setNewsAuthor(author); // Associa o autor ao mock de notícia
		news.setNewsRegistryDate(registryDate);
		when(newsRepository.save(any(News.class))).thenReturn(news);
		
	
		// Executar o método de teste
		News savedNews = newsService.saveNews(title, content, autName, registryDate);
	
		// Verificar se a notícia foi salva corretamente
		assertNotNull(savedNews);
		assertEquals(title, savedNews.getNewsTitle());
		assertEquals(content, savedNews.getNewsContent());
		assertEquals(autName, savedNews.getNewsAuthor().getAutName());
		assertEquals(registryDate, savedNews.getNewsRegistryDate());
	
		// Verificar se os métodos mockados foram chamados
		verify(newsAuthorRepository, times(1)).findByAutName(autName);
		verify(newsRepository, times(1)).save(any(News.class));
	}
	
    @Test
    public void testSaveNews_MissingTitle() {
        // Definir dados de entrada com título faltando
        String content = "Sample Content";
        String autName = "John Doe";
        Date registryDate = new Date(System.currentTimeMillis());

        // Verificar se a exceção é lançada ao tentar salvar notícia sem título
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            newsService.saveNews(null, content, autName, registryDate);
        });

        assertEquals("Title is required", exception.getMessage());
    }

    @Test
    public void testSaveNews_AuthorNotFound() {
        // Dados de entrada com autor não existente
        String title = "Sample Title";
        String content = "Sample Content";
        String autName = "Unknown Author";
        Date registryDate = new Date(System.currentTimeMillis());

        // Mockar o comportamento do repositório de autor para retornar vazio
        when(newsAuthorRepository.findByAutName(autName)).thenReturn(Optional.empty());

        // Verificar se a exceção é lançada quando o autor não é encontrado
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            newsService.saveNews(title, content, autName, registryDate);
        });

        assertEquals("Author not found", exception.getMessage());
    }

}
