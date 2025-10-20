import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SeleniumJupiter.class)
class SearchBookTest {

    @Test
    void testSearchHarryPotterRobust(EdgeDriver driver) {
        driver.get("https://cover-bookstore.onrender.com/");

        // Campo de pesquisa
        WebElement searchBox = driver.findElement(By.cssSelector("[data-testid='book-search-input']"));
        searchBox.sendKeys("Harry Potter");

        // Submeter pesquisa
        driver.findElement(By.cssSelector("button")).click();

        // Espera explícita pelos itens da lista
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> results = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("[data-testid='book-search-item']"))
        );

        // Primeiro livro da lista
        WebElement firstBook = results.get(0);

        // Título e autor dentro do container (CSS ou XPath relativo)
        WebElement title = firstBook.findElement(By.cssSelector("span[class*='bookTitle']"));
        WebElement author = firstBook.findElement(By.cssSelector("span[class*='bookAuthor']"));

        // Validação
        assertThat(title.getText()).contains("Harry Potter and the Sorcerer's Stone");
        assertThat(author.getText()).contains("J.K. Rowling");
    }
}
