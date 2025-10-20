package com.tqs.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FlightsPage {
    private WebDriver driver;

    @FindBy(css = "tr:nth-child(1) .btn")
    private WebElement firstFlightSelectButton;

    public FlightsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void selectFirstFlight() {
        firstFlightSelectButton.click();
    }
}
