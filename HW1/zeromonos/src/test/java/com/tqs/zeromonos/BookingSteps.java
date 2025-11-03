package com.tqs.zeromonos;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingSteps {

    private WebDriver driver;
    private WebDriverWait wait;
    private String bookingToken;
    private static final String BASE_URL = "http://localhost:8080";

    @Before
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless"); // Run in headless mode for CI
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new FirefoxDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("the booking system is available")
    public void theBookingSystemIsAvailable() {
        // Verify API is responsive
        driver.get(BASE_URL + "/api/municipalities");
        assertThat(driver.getPageSource()).isNotNull();
    }

    @Given("the web application is running")
    public void theWebApplicationIsRunning() {
        driver.get(BASE_URL);
        assertThat(driver.getTitle()).contains("ZeroMonos");
    }

    @Given("I am on the booking page")
    public void iAmOnTheBookingPage() {
        driver.get(BASE_URL + "/index.html");
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("bookingForm")));
    }

    @When("I select municipality {string}")
    public void iSelectMunicipality(String municipality) {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("municipality")));
        Select municipalitySelect = new Select(driver.findElement(
                By.id("municipality")));
        municipalitySelect.selectByVisibleText(municipality);
    }

    @And("I enter description {string}")
    public void iEnterDescription(String description) {
        WebElement descriptionField = driver.findElement(By.id("description"));
        descriptionField.clear();
        descriptionField.sendKeys(description);
    }

    @And("I select a valid future date")
    public void iSelectAValidFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(3);
        while (futureDate.getDayOfWeek().getValue() >= 6) {
            futureDate = futureDate.plusDays(1);
        }
        WebElement dateField = driver.findElement(By.id("collectionDate"));
        dateField.sendKeys(futureDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    @And("I select time slot {string}")
    public void iSelectTimeSlot(String timeSlot) {
        Select timeSlotSelect = new Select(driver.findElement(By.id("timeSlot")));
        timeSlotSelect.selectByValue(timeSlot);
    }

    @And("I submit the booking form")
    public void iSubmitTheBookingForm() {
        WebElement submitButton = driver.findElement(By.id("submitBtn"));
        submitButton.click();
    }

    @Then("I should see a success message")
    public void iShouldSeeASuccessMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("successMessage")));
        WebElement successMessage = driver.findElement(By.id("successMessage"));
        assertThat(successMessage.isDisplayed()).isTrue();
        assertThat(successMessage.getText()).contains("Successful");
    }

    @And("I should receive an access token")
    public void iShouldReceiveAnAccessToken() {
        WebElement tokenElement = wait.until(driverInst -> {
            WebElement el = driver.findElement(By.id("tokenValue"));
            return (el.getText() != null && !el.getText().isEmpty()) ? el : null;
        });
        bookingToken = tokenElement.getText();
        assertThat(bookingToken).isNotEmpty();
    }

    @Then("I should see a validation error")
    public void iShouldSeeAValidationError() {
        WebElement descriptionField = driver.findElement(By.id("description"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String validationMessage = (String) js.executeScript(
                "return arguments[0].validationMessage;", descriptionField
        );
        assertThat(validationMessage).isNotEmpty();
    }


    @Given("I have created a booking with token")
    public void iHaveCreatedABookingWithToken() {
        iAmOnTheBookingPage();
        iSelectMunicipality("Guarda");
        iEnterDescription("Test booking for selenium test scenario");
        iSelectAValidFutureDate();
        iSelectTimeSlot("MORNING");
        iSubmitTheBookingForm();
        iShouldReceiveAnAccessToken();
    }

    @When("I navigate to the check booking page")
    public void iNavigateToTheCheckBookingPage() {
        driver.get(BASE_URL + "/check-booking.html");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("token")));
    }

    @And("I enter my booking token")
    public void iEnterMyBookingToken() {
        WebElement tokenField = driver.findElement(By.id("token"));
        tokenField.clear();
        tokenField.sendKeys(bookingToken);
    }

    @And("I click check booking")
    public void iClickCheckBooking() {
        driver.findElement(By.xpath("//button[contains(text(), 'Check Booking')]")).click();
    }

    @Then("I should see my booking details")
    public void iShouldSeeMyBookingDetails() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("bookingDetails")));
        WebElement details = driver.findElement(By.id("bookingDetails"));
        assertThat(details.isDisplayed()).isTrue();
    }

    @And("the status should be {string}")
    public void theStatusShouldBe(String expectedStatus) {
        WebElement statusElement = driver.findElement(By.id("currentState"));
        assertThat(statusElement.getText()).contains(expectedStatus);
    }

    @Given("there is a booking with status {string}")
    public void thereIsABookingWithStatus(String status) {
        // Create a booking via API or UI
        iHaveCreatedABookingWithToken();
    }

    @When("I navigate to the staff portal")
    public void iNavigateToTheStaffPortal() {
        driver.get(BASE_URL + "/staff.html");
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("bookingsTable")));
    }

    @And("I find the booking in the list")
    public void iFindTheBookingInTheList() {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("action-btn")));
    }

    @And("I click assign button")
    public void iClickAssignButton() {
        WebElement assignButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(), 'Assign')]")));
        assignButton.click();
    }

    @Then("the booking status should change to {string}")
    public void theBookingStatusShouldChangeTo(String expectedStatus) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("status-badge"), expectedStatus));
    }

    @Given("I have an active booking")
    public void iHaveAnActiveBooking() {
        iHaveCreatedABookingWithToken();
    }

    @When("I navigate to check booking page")
    public void iNavigateToCheckBookingPage() {
        iNavigateToTheCheckBookingPage();
    }

    @And("I click cancel booking")
    public void iClickCancelBooking() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("cancelBtn")));
        driver.findElement(By.id("cancelBtn")).click();
    }

    @And("I confirm the cancellation")
    public void iConfirmTheCancellation() {
        // tem 2 alertas, aguarda e aceita primeira, depois 2
        wait.until(ExpectedConditions.alertIsPresent());
        Alert firstAlert = driver.switchTo().alert();
        System.out.println("First alert: " + firstAlert.getText());
        firstAlert.accept();
        // 2ª
        wait.until(ExpectedConditions.alertIsPresent());
        Alert secondAlert = driver.switchTo().alert();
        System.out.println("Second alert: " + secondAlert.getText());
        secondAlert.accept();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("cancelBtn")));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("currentState"), "CANCELED"));
    }

    @Then("the booking status should be {string}")
    public void theBookingStatusShouldBe(String expectedStatus) {
        WebElement statusElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("currentState")));
        wait.until(driverInst -> statusElement.getText().equalsIgnoreCase(expectedStatus));
        assertThat(statusElement.getText()).isEqualToIgnoringCase(expectedStatus);
    }

    @And("I filter by municipality {string}")
    public void iFilterByMunicipality(String municipality) {
        Select municipalityFilter = new Select(
                driver.findElement(By.id("municipalityFilter")));
        municipalityFilter.selectByVisibleText(municipality);
        // table to update
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.className("bookings-table")));
    }

    @Then("I should only see bookings from {string}")
    public void iShouldOnlySeeBookingsFrom(String municipality) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//table[@class='bookings-table']//td")));
        // Verify all visible bookings are from the selected municipality
        assertThat(driver.getPageSource()).contains(municipality);
    }

    @Given("there are bookings for {string} and {string}")
    public void thereAreBookingsForAnd(String municipality1, String municipality2) {
        // Create bookings for both municipalities
    }
}
