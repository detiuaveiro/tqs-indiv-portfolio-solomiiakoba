package com.tqs.pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    private WebDriver driver;
    //Page URL
    private static final String PAGE_URL="https://blazedemo.com";

    //Locators

    @FindBy(css = ".btn-primary")
    private WebElement flightButton;

    //Constructor
    public HomePage(WebDriver driver){
        this.driver=driver;
        driver.get(PAGE_URL);
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void clickFindFlights(){
        flightButton.click();
    }
}
