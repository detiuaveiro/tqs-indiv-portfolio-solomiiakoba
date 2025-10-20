package com.tqs.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PurchasePage {
    private WebDriver driver;

    @FindBy(id = "inputName")
    private WebElement inputName;

    @FindBy(id = "address")
    private WebElement address;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(id = "nameOnCard")
    private WebElement nameOnCard;

    @FindBy(css = ".checkbox")
    private WebElement rememberMeCheckbox;

    @FindBy(css = ".btn-primary")
    private WebElement purchaseButton;

    public PurchasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillForm(String name, String addr, String cityS, String stateS, String cardName) {
        inputName.clear(); inputName.sendKeys(name);
        address.clear(); address.sendKeys(addr);
        city.clear(); city.sendKeys(cityS);
        state.clear(); state.sendKeys(stateS);
        nameOnCard.clear(); nameOnCard.sendKeys(cardName);
    }

    public void checkRememberMe() {
        if (!rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
    }

    public void clickPurchase() {
        purchaseButton.click();
    }
}
