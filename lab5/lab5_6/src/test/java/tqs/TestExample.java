package tqs;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.*;

import org.junit.jupiter.api.*;

@UsePlaywright
public class TestExample {
    @Test
    void test(Page page) {
        page.navigate("https://blazedemo.com/");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Find Flights")).click();
        page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName("Choose This Flight 43 Virgin")).getByRole(AriaRole.BUTTON).click();
        page.getByPlaceholder("First Last").click();
        page.getByPlaceholder("First Last").fill("sol");
        page.getByPlaceholder("Anytown").click();
        page.getByPlaceholder("Anytown").fill("sol");
        page.getByPlaceholder("State").click();
        page.getByPlaceholder("State").fill("florida");
        page.getByPlaceholder("12345").click();
        page.getByPlaceholder("John Smith").click();
        page.getByPlaceholder("John Smith").fill("sol");
        page.getByText("Remember me").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Purchase Flight")).click();
    }
}