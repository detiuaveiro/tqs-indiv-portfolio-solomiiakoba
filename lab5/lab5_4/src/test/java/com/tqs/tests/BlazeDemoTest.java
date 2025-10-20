package com.tqs.tests;

import com.tqs.pages.*;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SeleniumJupiter.class)
public class BlazeDemoTest {

    @Test
    public void testPurchaseFlight(FirefoxDriver driver) {
        //definir tamanho da janela para reproduzir o exemplo
        driver.manage().window().setSize(new Dimension(648, 692));

        // Page objects
        HomePage home = new HomePage(driver);
        home.clickFindFlights();

        FlightsPage flights = new FlightsPage(driver);
        flights.selectFirstFlight();

        PurchasePage purchase = new PurchasePage(driver);
        purchase.fillForm("sol", "sol", "sol", "sol", "sol");
        purchase.checkRememberMe();
        purchase.clickPurchase();

        // Espera explícita até o h1 de confirmação estar visível
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        ConfirmationPage confirmation = new ConfirmationPage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(org.openqa.selenium.By.cssSelector("h1")));

        String confirmationText = confirmation.getConfirmationText();
        assertEquals("Thank you for your purchase today!", confirmationText);
    }
}
