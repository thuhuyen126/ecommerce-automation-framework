package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * CartPage - Page Object for shopping cart page.
 */
public class CartPage {

    private static final Logger log =
            LogManager.getLogger(CartPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ─────────────────────────────────────────────
    // Locators
    // ─────────────────────────────────────────────

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(className = "title")
    private WebElement pageTitle;

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────

    public CartPage(WebDriver driver) {

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

        wait.until(
                ExpectedConditions.urlContains(
                        "cart.html"));

        wait.until(
                ExpectedConditions.visibilityOf(
                        pageTitle));

        return pageTitle.getText()
                .equalsIgnoreCase("Your Cart");

    } catch (Exception e) {

        return false;
    }
    }

    public int getCartItemCount() {

        try {

            wait.until(ExpectedConditions.visibilityOf(pageTitle));

            return cartItems.size();

        } catch (Exception e) {

            return 0;
        }
    }

    public List<String> getCartItemNames() {

        wait.until(ExpectedConditions.visibilityOf(pageTitle));

        return cartItemNames.stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean isProductInCart(String productName) {

        return getCartItemNames().stream()
                .anyMatch(name ->
                        name.equalsIgnoreCase(productName));
    }

    public boolean isCartEmpty() {

        try {

            return driver.findElements(
                    By.className("cart_item")).isEmpty();

        } catch (Exception e) {

            return true;
        }
    }

    // ─────────────────────────────────────────────
    // Cart Actions
    // ─────────────────────────────────────────────

    /**
     * Remove item from cart by product slug.
     * Example:
     * sauce-labs-backpack
     */
    public void removeItem(String productName) {

        String slug = productName
                .toLowerCase()
                .replace(" ", "-");

        String removeButtonId = "remove-" + slug;

        log.info("Removing item from cart: {}", productName);

        WebElement removeButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id(removeButtonId)));

        removeButton.click();

        // Wait item removed from DOM
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.id(removeButtonId)));
    }

    /**
     * Proceed to checkout page.
     */
    public CheckoutPage proceedToCheckout() {

        log.info("Proceeding to checkout");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        checkoutButton))
                .click();

        wait.until(
                ExpectedConditions.urlContains("checkout-step-one"));

        return new CheckoutPage(driver);
    }

    /**
     * Continue shopping and return to products page.
     */
    public ProductsPage continueShopping() {

        log.info("Continuing shopping");

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        continueShoppingButton))
                .click();

        wait.until(
                ExpectedConditions.urlContains("inventory"));

        return new ProductsPage(driver);
    }
}