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
 * LoginPage - Page Object for SauceDemo login page.
 *
 * URL:
 * https://www.saucedemo.com/
 *
 * Responsibilities:
 * - Login actions
 * - Validation handling
 * - Login page verification
 */
public class LoginPage {

    private static final Logger log =
            LogManager.getLogger(LoginPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ─────────────────────────────────────────
    // Locators
    // ─────────────────────────────────────────

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(className = "login_logo")
    private WebElement loginLogo;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────

    public LoginPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15));

        PageFactory.initElements(driver, this);
    }

    // ─────────────────────────────────────────
    // Verification Methods
    // ─────────────────────────────────────────

    /**
     * Verify login page loaded completely.
     */
    public boolean isPageLoaded() {

        try {

            wait.until(ExpectedConditions.urlContains("saucedemo"));

            wait.until(
                    ExpectedConditions.visibilityOf(loginLogo));

            wait.until(
                    ExpectedConditions.visibilityOf(usernameField));

            wait.until(
                    ExpectedConditions.visibilityOf(passwordField));

            return loginButton.isDisplayed();

        } catch (Exception e) {

            log.error("Login page failed to load.", e);

            return false;
        }
    }

    /**
     * Returns current page URL.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ─────────────────────────────────────────
    // Input Actions
    // ─────────────────────────────────────────

    public void enterUsername(String username) {

        log.info("Entering username: {}",
                username == null || username.isEmpty()
                        ? "(empty)"
                        : username);

        wait.until(
                ExpectedConditions.visibilityOf(usernameField));

        usernameField.clear();

        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {

        log.info("Entering password");

        wait.until(
                ExpectedConditions.visibilityOf(passwordField));

        passwordField.clear();

        passwordField.sendKeys(password);
    }

    public void clearUsername() {

        wait.until(
                ExpectedConditions.visibilityOf(usernameField));

        usernameField.clear();
    }

    public void clearPassword() {

        wait.until(
                ExpectedConditions.visibilityOf(passwordField));

        passwordField.clear();
    }

    // ─────────────────────────────────────────
    // Button Actions
    // ─────────────────────────────────────────

    public void clickLoginButton() {

        log.info("Clicking Login button");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        loginButton));

        loginButton.click();
    }

    // ─────────────────────────────────────────
    // Business Actions
    // ─────────────────────────────────────────

    /**
     * Perform login and return ProductsPage.
     *
     * Caller should verify success/failure.
     */
    public ProductsPage loginAs(
            String username,
            String password) {

        enterUsername(username);

        enterPassword(password);

        clickLoginButton();

        log.info("Login submitted for user: {}", username);

        return new ProductsPage(driver);
    }

    /**
     * Login expecting failure.
     */
    public LoginPage loginExpectingFailure(
            String username,
            String password) {

        enterUsername(username);

        enterPassword(password);

        clickLoginButton();

        return this;
    }

    // ─────────────────────────────────────────
    // Error Handling
    // ─────────────────────────────────────────

    public boolean isErrorDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOf(errorMessage))
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }

    public String getErrorMessage() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOf(errorMessage))
                    .getText();

        } catch (Exception e) {

            log.warn("Error message not displayed.");

            return "";
        }
    }
}