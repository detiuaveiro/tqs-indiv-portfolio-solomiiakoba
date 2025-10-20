import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SeleniumJupiter.class)
public class BlazeDemoTest {

    @Test
    public void testPurchaseFlight(FirefoxDriver driver) {
        driver.manage().window().setSize(new Dimension(648, 692));
        driver.get("https://blazedemo.com");

        driver.findElement(By.cssSelector(".btn-primary")).click();
        driver.findElement(By.cssSelector("tr:nth-child(1) .btn")).click();
        driver.findElement(By.id("inputName")).sendKeys("sol");
        driver.findElement(By.id("address")).sendKeys("sol");
        driver.findElement(By.id("city")).sendKeys("sol");
        driver.findElement(By.id("state")).sendKeys("sol");
        driver.findElement(By.id("nameOnCard")).sendKeys("sol");
        driver.findElement(By.cssSelector(".checkbox")).click();
        driver.findElement(By.cssSelector(".btn-primary")).click(); // clica em "Purchase"

        // espera até 10 segundos pelo h1 aparecer
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String confirmation = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1"))
        ).getText();

        assertEquals("Thank you for your purchase today!", confirmation);
    }
}