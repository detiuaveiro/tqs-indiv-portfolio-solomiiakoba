import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SeleniumJupiter.class)
class SimpleSearchBookTest {
    @Test
    void testSearchHarryPotter(EdgeDriver driver) {
        // 1. Aceder ao site
        driver.get("https://cover-bookstore.onrender.com/");
        // 2. Encontrar o campo de pesquisa
        WebElement searchBox = driver.findElement(By.cssSelector("[data-testid='book-search-input']"));
        searchBox.sendKeys("Harry Potter");
        // 3. Submeter a pesquisa
        driver.findElement(By.cssSelector("button")).click();
        // 4. Aguardar um pouco
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 5. Encontrar os resultados
        WebElement title = driver.findElement(By.cssSelector(".SearchList_bookTitle__1wo4a"));
        // WebElement title = driver.findElement(By.xpath("//div[@class='SearchList_bookInfoContainer__bkMz1']/span[@class='SearchList_bookTitle__1wo4a']"));
        WebElement author = driver.findElement(By.cssSelector(".SearchList_bookAuthor__3giPc"));
        // 6. Validar o resultado
        assertThat(title.getText()).contains("Harry Potter and the Sorcerer's Stone");
        assertThat(author.getText()).contains("J.K. Rowling");
    }
}