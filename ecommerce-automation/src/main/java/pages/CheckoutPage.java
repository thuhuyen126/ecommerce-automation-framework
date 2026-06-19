package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * CheckoutPage - Page Object for SauceDemo checkout flow.
 *
 * Flow:
 * 1. checkout-step-one.html  → shipping information
 * 2. checkout-step-two.html  → order overview
 * 3. checkout-complete.html  → success confirmation
 */
public class CheckoutPage {

    private static final Logger log = LogManager.getLogger(CheckoutPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ─────────────────────────────────────────────────────────
    // Step One - Shipping Information
    // ─────────────────────────────────────────────────────────

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ─────────────────────────────────────────────────────────
    // Step Two - Overview
    // ─────────────────────────────────────────────────────────

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    // ─────────────────────────────────────────────────────────
    // Complete Page
    // ─────────────────────────────────────────────────────────

    @FindBy(className = "complete-header")
    private WebElement confirmationHeader;

    @FindBy(className = "complete-text")
    private WebElement confirmationText;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    @FindBy(className = "title")
    private WebElement pageTitle;

    // ─────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────

    public CheckoutPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15));

        PageFactory.initElements(driver, this);
    }

    // ─────────────────────────────────────────────────────────
    // Step One Verification
    // ─────────────────────────────────────────────────────────

    public boolean isStepOneLoaded() {

        try {

            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-step-one"));

            wait.until(
                    ExpectedConditions.visibilityOf(
                            firstNameField));

            return true;

        } catch (TimeoutException e) {

            log.error("Checkout Step One not loaded.");

            return false;
        }
    }

    // ─────────────────────────────────────────────────────────
    // Shipping Information Actions
    // ─────────────────────────────────────────────────────────

    public void enterFirstName(String firstName) {

        WebElement field = wait.until(
                ExpectedConditions.visibilityOf(
                        firstNameField));

        field.clear();
        field.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {

        WebElement field = wait.until(
                ExpectedConditions.visibilityOf(
                        lastNameField));

        field.clear();
        field.sendKeys(lastName);
    }

    public void enterPostalCode(String postalCode) {

        WebElement field = wait.until(
                ExpectedConditions.visibilityOf(
                        postalCodeField));

        field.clear();
        field.sendKeys(postalCode);
    }

    /**
     * Fill shipping form and continue.
     */
    public void fillShippingInfo(
            String firstName,
            String lastName,
            String postalCode) {

        log.info(
                "Filling shipping info: {} {} {}",
                firstName,
                lastName,
                postalCode);

        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);

        clickContinue();
    }

    public void clickContinue() {

        log.info("Clicking Continue button");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        continueButton))
                .click();
    }

    // ─────────────────────────────────────────────────────────
    // Error Handling
    // ─────────────────────────────────────────────────────────

    public boolean isErrorDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOf(
                            errorMessage))
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }

    public String getErrorMessage() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOf(
                            errorMessage))
                    .getText();

        } catch (Exception e) {

            return "";
        }
    }

    // ─────────────────────────────────────────────────────────
    // Step Two Verification
    // ─────────────────────────────────────────────────────────

    public boolean isStepTwoLoaded() {

        try {

            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-step-two"));

            wait.until(
                    ExpectedConditions.visibilityOf(
                            totalLabel));

            return true;

        } catch (TimeoutException e) {

            log.error("Checkout Step Two not loaded.");

            return false;
        }
    }

    // ─────────────────────────────────────────────────────────
    // Order Overview
    // ─────────────────────────────────────────────────────────

    public String getOrderTotal() {

        return wait.until(
                ExpectedConditions.visibilityOf(
                        totalLabel))
                .getText();
    }

    public String getTax() {

        return wait.until(
                ExpectedConditions.visibilityOf(
                        taxLabel))
                .getText();
    }

    // ─────────────────────────────────────────────────────────
    // Checkout Actions
    // ─────────────────────────────────────────────────────────

    public void clickFinish() {

        log.info("Completing checkout");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        finishButton))
                .click();
    }

    /**
 * Cancel checkout and return to cart page.
 */
public ProductsPage clickCancel() {

    log.info("Cancelling checkout");

    wait.until(
            ExpectedConditions.elementToBeClickable(
                    cancelButton))
            .click();

    wait.until(
            ExpectedConditions.urlContains(
                    "inventory.html"));

    return new ProductsPage(driver);
}

    // ─────────────────────────────────────────────────────────
    // Confirmation Page
    // ─────────────────────────────────────────────────────────

    public boolean isOrderConfirmed() {

        try {

            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-complete"));

            wait.until(
                    ExpectedConditions.visibilityOf(
                            confirmationHeader));

            return true;

        } catch (TimeoutException e) {

            log.error("Order confirmation page not loaded.");

            return false;
        }
    }

    public String getConfirmationHeader() {

        return wait.until(
                ExpectedConditions.visibilityOf(
                        confirmationHeader))
                .getText();
    }

    public String getConfirmationText() {

        return wait.until(
                ExpectedConditions.visibilityOf(
                        confirmationText))
                .getText();
    }

    /**
     * Return to products page after successful checkout.
     */
    public ProductsPage backToProducts() {

        log.info("Returning to products page");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        backToProductsButton))
                .click();

        return new ProductsPage(driver);
    }
}