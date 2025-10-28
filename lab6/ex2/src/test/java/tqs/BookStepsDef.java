package tqs;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BookStepsDef {

    private Library library;
    private List<Book> searchResults;

    //yyyy-mm-dd ---
    @ParameterType("([0-9]{4}-[0-9]{2}-[0-9]{2})")
    public LocalDate iso8601Date(String date) {
        return LocalDate.parse(date);
    }

    @Given("a library exists")
    public void libraryExists() {
        library = new Library();
        searchResults = new ArrayList<>();
    }

    @Given("the following books exist:")
    public void addBooks(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String title = row.get("title");
            String author = row.get("author");
            LocalDate published = LocalDate.parse(row.get("published"));
            LocalDateTime publishedTime = published.atStartOfDay();
            Book book = new Book(author, title, publishedTime);
            library.addBook(book);
        }
    }


    @Given("a book with the title {string}, written by {string}, published in {iso8601Date}")
    public void addBook(String title, String author, LocalDate date) {
        LocalDateTime dateTime = date.atStartOfDay();
        Book book = new Book(author, title, dateTime);
        library.addBook(book);
    }

    @When("the customer searches for books by author {string}")
    public void searchBooksByAuthor(String author) {
        searchResults = library.findBooksByAuthor(author);
    }

    @When("the customer searches for books published between {iso8601Date} and {iso8601Date}")
    public void searchBooksByDate(LocalDate start, LocalDate end) {
        LocalDateTime startTime = start.atStartOfDay();
        LocalDateTime endTime = end.atStartOfDay();
        searchResults = library.findBooks(startTime, endTime);
    }

    @Then("the search results should contain {int} book")
    @Then("the search results should contain {int} books")
    public void checkNumberOfBooks(int expected) {
        assertEquals(expected, searchResults.size());
    }

    @Then("the book title should be {string}")
    public void checkBookTitle(String title) {
        assertEquals(title, searchResults.get(0).getTitle());
    }

}
