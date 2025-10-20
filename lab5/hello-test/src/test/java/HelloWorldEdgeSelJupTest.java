import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.edge.EdgeDriver;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(SeleniumJupiter.class)
public class HelloWorldEdgeSelJupTest {
    static final Logger log = getLogger(lookup().lookupClass());

    @Test
    void test(EdgeDriver driver) {
        // Same test logic than other "hello world" tests
        String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/";
        driver.get(sutUrl);

        String title = driver.getTitle();
        log.debug("The title of {} is {}", sutUrl, title);

        // Verify
        assertThat(title).isEqualTo("Hands-On Selenium WebDriver with Java");

        // acede ao link
        driver.findElement(By.linkText("Slow calculator")).click();
        String currentUrl = driver.getCurrentUrl();

        // verifica url
        assertThat(currentUrl).contains("slow-calculator");

        String newTitle = driver.getTitle();
        log.debug("Arrived at page with title: {}", newTitle);
    }
}
