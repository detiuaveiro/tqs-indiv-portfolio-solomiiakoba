package tqs;
import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright
public class SearchBookPlaywrightTest {

    @Test
    void testSearchHarryPotter(Page page) {
        page.navigate("https://cover-bookstore.onrender.com/");
        Locator searchBox = page.getByPlaceholder("Search for books, authors, etc...").filter(
                new Locator.FilterOptions().setHasText("")
        );
        searchBox.first().fill("Harry Potter");

        //botão de pesquisa
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        //resultados
        Locator results = page.getByTestId("book-search-item");

        // filtar livro correto
        Locator correctBook = results.filter(
                new Locator.FilterOptions().setHasText("Harry Potter and the Sorcerer's Stone")
        );

        // extrai título e autor
        Locator title = correctBook.locator("span[class*='bookTitle']");
        Locator author = correctBook.locator("span[class*='bookAuthor']");

        // validar
        assertThat(title).containsText("Harry Potter and the Sorcerer's Stone");
        assertThat(author).containsText("J.K. Rowling");
    }
}
