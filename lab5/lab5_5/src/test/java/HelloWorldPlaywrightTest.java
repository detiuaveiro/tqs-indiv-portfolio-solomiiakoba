import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldPlaywrightTest {

    @Test
    void testHelloWorld() {
        // inicializa Playwright
        try (Playwright playwright = Playwright.create()) {

            //cria instância do navegador
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false) //mostra o browser
                            .setSlowMo(500)     //faz visivel
            );

            // cria contexto
            BrowserContext context = browser.newContext();

            // abre nova tab
            Page page = context.newPage();

            // vai para o site
            String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/";
            page.navigate(sutUrl);

            // le o título
            String title = page.title();
            System.out.println("O título é: " + title);
            assertThat(title).isEqualTo("Hands-On Selenium WebDriver with Java");

            //clica no link
            page.click("text=Slow calculator");

            //verifica URL
            String currentUrl = page.url();
            assertThat(currentUrl).contains("slow-calculator");

            //fecha
            browser.close();
        }
    }
}
