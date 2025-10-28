package tqs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import io.cucumber.java.en.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookStepsDef {
    private static Playwright playwright;
    private static Browser browser;
    private static Page page;

    @Given("the user is on the online bookstore")
    public void openStore() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        page = browser.newPage();
        page.navigate("https://cover-bookstore.onrender.com/");
    }
    @When("the user searches for {string}")
    public void userSearchesFor(String bookName) {
        page.getByPlaceholder("Search for books, authors, etc...").first().fill(bookName);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
    }
    @Then("a book titled {string} by {string} should appear in the results")
    public void verifyBookAppears(String title, String author) {
        Locator results = page.getByTestId("book-search-item");
        Locator correctBook = results.filter(new Locator.FilterOptions().setHasText(title));
        assertThat(correctBook.locator("span[class*='bookTitle']")).containsText(title);
        assertThat(correctBook.locator("span[class*='bookAuthor']")).containsText(author);
        page.close();
        browser.close();
        playwright.close();
    }
    @Then("a message {string} should be displayed")
    public void verifyNoResults(String message) {
        page.waitForSelector(".SearchList_emptySearchImage__3R6v8");
        Locator emptyStateImage = page.locator(".SearchList_emptySearchImage__3R6v8");
        assertThat(emptyStateImage).isVisible();
        page.close();
        browser.close();
        playwright.close();
    }

}
