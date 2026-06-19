package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * ProductsPage - Page Object for inventory/products page.
 */
public class ProductsPage {

    private static final Logger log = LogManager.getLogger(ProductsPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ─────────────────────────────────────────────
    // Locators
    // ─────────────────────────────────────────────

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────

    public ProductsPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15));

        PageFactory.initElements(driver, this);
    }

    // ─────────────────────────────────────────────
    // Verification Methods
    // ─────────────────────────────────────────────

    public boolean isPageLoaded() {

        try {

            wait.until(ExpectedConditions.urlContains("inventory"));

            wait.until(ExpectedConditions.visibilityOf(pageTitle));

            return pageTitle.getText().equalsIgnoreCase("Products");

        } catch (Exception e) {

            log.error("Products page failed to load", e);

            return false;
        }
    }

    public String getPageTitle() {

        return wait.until(
                ExpectedConditions.visibilityOf(pageTitle))
                .getText();
    }

    // ─────────────────────────────────────────────
    // Product Actions
    // ─────────────────────────────────────────────

    /**
     * Add product to cart by product slug.
     * Example:
     * sauce-labs-backpack
     */
    public void addProductToCartByName(String productName) {

    String slug = productName
            .toLowerCase()
            .replace(" ", "-");

    String addButtonId = "add-to-cart-" + slug;

    log.info("Adding product: {}", productName);

    WebElement addButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id(addButtonId)));

    addButton.click();

    String removeButtonId = "remove-" + slug;

    wait.until(
            ExpectedConditions.presenceOfElementLocated(
                    By.id(removeButtonId)));

    wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.id(removeButtonId)));

    wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id(removeButtonId)));
}

    /**
     * Remove product from inventory page.
     */
    public void removeProductFromPageByName(String productName) {

        String slug = productName
                .toLowerCase()
                .replace(" ", "-");

        String buttonId = "remove-" + slug;

        log.info("Removing product from cart: {}", productName);

        WebElement removeButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id(buttonId)));

        removeButton.click();

        // Wait add button appears again
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("add-to-cart-" + slug)));
    }

    public List<String> getAllProductNames() {

        return productNames.stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getAllProductPrices() {

        return productPrices.stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getProductCount() {

        return productItems.size();
    }

    // ─────────────────────────────────────────────
    // Sorting
    // ─────────────────────────────────────────────

    /**
     * Sort options:
     * az
     * za
     * lohi
     * hilo
     */
    public void sortProducts(String sortOption) {

        log.info("Sorting products by: {}", sortOption);

        wait.until(
                ExpectedConditions.visibilityOf(sortDropdown));

        Select select = new Select(sortDropdown);

        select.selectByValue(sortOption);

        // Small stabilization wait
        wait.until(ExpectedConditions.visibilityOfAllElements(productItems));
    }

    // ─────────────────────────────────────────────
    // Cart
    // ─────────────────────────────────────────────

    public int getCartCount() {

        try {

            List<WebElement> badges = driver.findElements(
                    By.cssSelector(".shopping_cart_badge"));

            if (badges.isEmpty()) {
                return 0;
            }

            String text = badges.get(0).getText().trim();

            return Integer.parseInt(text);

        } catch (Exception e) {

            log.warn("Unable to get cart count");

            return 0;
        }
    }

    public CartPage goToCart() {

        log.info("Navigating to cart");

        wait.until(
                ExpectedConditions.elementToBeClickable(cartLink))
                .click();

        wait.until(ExpectedConditions.urlContains("cart"));

        return new CartPage(driver);
    }

    // ─────────────────────────────────────────────
    // Menu Actions
    // ─────────────────────────────────────────────

    /**
     * Open sidebar menu safely.
     */
    private void openMenu() {

        wait.until(
                ExpectedConditions.elementToBeClickable(menuButton));

        menuButton.click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.className("bm-menu-wrap")));
    }

    /**
     * Close sidebar menu safely.
     */
    private void closeMenu() {

        try {

            WebElement closeBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.id("react-burger-cross-btn")));

            closeBtn.click();

            wait.until(
                    ExpectedConditions.invisibilityOfElementLocated(
                            By.className("bm-menu-wrap")));

        } catch (Exception e) {

            log.warn("Menu already closed");
        }
    }

    /**
 * Logout from application.
 */
public void logout() {

    log.info("Logging out");

    openMenu();

    WebElement logoutBtn = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id("logout_sidebar_link")));

    logoutBtn.click();

    // Wait login page loaded completely
    wait.until(
            ExpectedConditions.urlContains(
                    "saucedemo"));

    wait.until(
            ExpectedConditions.presenceOfElementLocated(
                    By.id("login-button")));

    wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.id("login-button")));

    wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id("login-button")));

    log.info("Logout successful");
}

    /**
     * Reset application/cart state.
     */
    public void resetAppState() {

    log.info("Resetting application state");

    openMenu();

    WebElement resetBtn = wait.until(
            ExpectedConditions.elementToBeClickable(
                    By.id("reset_sidebar_link")));

    resetBtn.click();

    wait.until(
            ExpectedConditions.visibilityOf(pageTitle));

    wait.until(
            ExpectedConditions.elementToBeClickable(cartLink));

    closeMenu();
}
}