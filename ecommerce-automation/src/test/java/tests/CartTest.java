package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;

/**
 * CartTest - Covers shopping cart scenarios.
 *
 * Scenarios:
 *   TC_CART_001 - Add single product
 *   TC_CART_002 - Add multiple products
 *   TC_CART_003 - Product appears in cart
 *   TC_CART_004 - Remove product from cart page
 *   TC_CART_005 - Cart is empty by default
 *   TC_CART_006 - Continue shopping returns to products
 */
public class CartTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {

        // Login
        LoginPage loginPage = new LoginPage(getDriver());

        productsPage = loginPage.loginAs(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword()
        );

        // Verify products page loaded
        Assert.assertTrue(
                productsPage.isPageLoaded(),
                "Products page failed to load after login"
        );

        // Reset application state safely
        productsPage.resetAppState();

        // Verify cart starts empty
        Assert.assertEquals(
                productsPage.getCartCount(),
                0,
                "Cart should be empty before each test"
        );
    }

    @Test(description = "TC_CART_001 - Adding one product increases cart count to 1")
    public void testAddSingleProduct() {

        productsPage.addProductToCartByName("sauce-labs-backpack");

        Assert.assertEquals(
                productsPage.getCartCount(),
                1,
                "Cart badge should show 1 after adding one item"
        );
    }

    @Test(description = "TC_CART_002 - Adding multiple products updates cart count correctly")
    public void testAddMultipleProducts() {

        productsPage.addProductToCartByName("sauce-labs-backpack");
        productsPage.addProductToCartByName("sauce-labs-bike-light");
        productsPage.addProductToCartByName("sauce-labs-bolt-t-shirt");

        Assert.assertEquals(
                productsPage.getCartCount(),
                3,
                "Cart badge should show 3 after adding 3 items"
        );
    }

    @Test(description = "TC_CART_003 - Product appears in cart page after adding")
    public void testProductAppearsInCart() {

        productsPage.addProductToCartByName("sauce-labs-backpack");

        CartPage cartPage = productsPage.goToCart();

        Assert.assertTrue(
                cartPage.isPageLoaded(),
                "Cart page should load"
        );

        Assert.assertTrue(
                cartPage.isProductInCart("Sauce Labs Backpack"),
                "Backpack should appear in cart"
        );
    }

    @Test(description = "TC_CART_004 - Remove product from cart page")
    public void testRemoveProductFromCart() {

        productsPage.addProductToCartByName("sauce-labs-backpack");

        CartPage cartPage = productsPage.goToCart();

        cartPage.removeItem("sauce-labs-backpack");

        Assert.assertTrue(
                cartPage.isCartEmpty(),
                "Cart should be empty after removing the only item"
        );
    }

    @Test(description = "TC_CART_005 - Cart count is 0 when no items added")
    public void testEmptyCart() {

        Assert.assertEquals(
                productsPage.getCartCount(),
                0,
                "Fresh session cart should be empty"
        );
    }

    @Test(description = "TC_CART_006 - Continue shopping returns to products page")
    public void testContinueShopping() {

        productsPage.addProductToCartByName("sauce-labs-backpack");

        CartPage cartPage = productsPage.goToCart();

        ProductsPage backToProducts = cartPage.continueShopping();

        Assert.assertTrue(
                backToProducts.isPageLoaded(),
                "Continue shopping should return to products page"
        );
    }
}