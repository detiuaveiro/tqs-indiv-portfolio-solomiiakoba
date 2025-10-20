import com.microsoft.playwright.Page;
import org.junit.jupiter.api.Test;
import com.microsoft.playwright.junit.UsePlaywright;

import static org.assertj.core.api.Assertions.assertThat;

@UsePlaywright
public class HelloPlaywrightJUnitTest {

    @Test
    void testHelloWorld(Page page) {
        //para o site
        String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/";
        page.navigate(sutUrl);

        //verifica o título da página
        String title = page.title();
        assertThat(title).isEqualTo("Hands-On Selenium WebDriver with Java");

        //clica no link e espera
        page.waitForNavigation(() -> {
            page.click("text=Slow calculator");
        });

        //verifica a url
        assertThat(page.url()).contains("slow-calculator");
    }
}
