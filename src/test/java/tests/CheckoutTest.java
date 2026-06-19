package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.FakerUtil;
import utils.JsonDataReader;

/**
 * CheckoutTest - Covers the full checkout flow.
 *
 * Scenarios:
 * TC_CHECKOUT_001 - Successful checkout
 * TC_CHECKOUT_002 - Empty first name validation
 * TC_CHECKOUT_003 - Empty last name validation
 * TC_CHECKOUT_004 - Empty postal code validation
 * TC_CHECKOUT_005 - Cancel on step two returns to products page
 * TC_CHECKOUT_006 - Order total displayed correctly
 * TC_CHECKOUT_007 - Data-driven checkout validation
 */
public class CheckoutTest extends BaseTest {

    private CartPage cartPage;

    @BeforeMethod(alwaysRun = true)
    public void loginAndPrepareCart() {

        LoginPage loginPage = new LoginPage(getDriver());

        ProductsPage productsPage = loginPage.loginAs(
                ConfigReader.getValidUsername(),
                ConfigReader.getValidPassword());

        Assert.assertTrue(
                productsPage.isPageLoaded(),
                "Products page should load after login");

        // Reset state before each test
        productsPage.resetAppState();

        // Add test product
        productsPage.addProductToCartByName(
                "sauce-labs-backpack");

        // Open cart
        cartPage = productsPage.goToCart();

        Assert.assertTrue(
                cartPage.isPageLoaded(),
                "Cart page should load");
    }

    @Test(description =
            "TC_CHECKOUT_001 - Successful end-to-end checkout")
    public void testSuccessfulCheckout() {

        CheckoutPage checkoutPage =
                cartPage.proceedToCheckout();

        Assert.assertTrue(
                checkoutPage.isStepOneLoaded(),
                "Checkout Step One should load");

        // Fill random user data
        checkoutPage.fillShippingInfo(
                FakerUtil.getFirstName(),
                FakerUtil.getLastName(),
                FakerUtil.getZipCode());

        Assert.assertTrue(
                checkoutPage.isStepTwoLoaded(),
                "Checkout Step Two should load");

        // Verify total exists
        String total = checkoutPage.getOrderTotal();

        Assert.assertNotNull(
                total,
                "Order total should not be null");

        Assert.assertFalse(
                total.isBlank(),
                "Order total should not be empty");

        // Finish checkout
        checkoutPage.clickFinish();

        Assert.assertTrue(
                checkoutPage.isOrderConfirmed(),
                "Order confirmation page should appear");

        Assert.assertTrue(
                checkoutPage.getConfirmationHeader()
                        .toLowerCase()
                        .contains("thank you"),
                "Confirmation header should contain 'Thank you'");
    }

    @Test(description =
            "TC_CHECKOUT_002 - Empty first name shows validation error")
    public void testEmptyFirstName() {

        CheckoutPage checkoutPage =
                cartPage.proceedToCheckout();

        checkoutPage.fillShippingInfo(
                "",
                "Nguyen",
                "70000");

        Assert.assertTrue(
                checkoutPage.isErrorDisplayed(),
                "Validation error should appear");

        Assert.assertTrue(
                checkoutPage.getErrorMessage()
                        .contains("First Name is required"),
                "Correct error message should appear");
    }

    @Test(description =
            "TC_CHECKOUT_003 - Empty last name shows validation error")
    public void testEmptyLastName() {

        CheckoutPage checkoutPage =
                cartPage.proceedToCheckout();

        checkoutPage.fillShippingInfo(
                "Tran",
                "",
                "70000");

        Assert.assertTrue(
                checkoutPage.isErrorDisplayed(),
                "Validation error should appear");

        Assert.assertTrue(
                checkoutPage.getErrorMessage()
                        .contains("Last Name is required"),
                "Correct error message should appear");
    }

    @Test(description =
            "TC_CHECKOUT_004 - Empty postal code shows validation error")
    public void testEmptyPostalCode() {

        CheckoutPage checkoutPage =
                cartPage.proceedToCheckout();

        checkoutPage.fillShippingInfo(
                "Le",
                "Van A",
                "");

        Assert.assertTrue(
                checkoutPage.isErrorDisplayed(),
                "Validation error should appear");

        Assert.assertTrue(
                checkoutPage.getErrorMessage()
                        .contains("Postal Code is required"),
                "Correct error message should appear");
    }

    @Test(description =
            "TC_CHECKOUT_005 - Cancel on step two returns to products page")
    public void testCancelCheckout() {

        CheckoutPage checkoutPage =
                cartPage.proceedToCheckout();

        checkoutPage.fillShippingInfo(
                "Test",
                "User",
                "12345");

        Assert.assertTrue(
                checkoutPage.isStepTwoLoaded(),
                "Checkout Step Two should load");

        // Cancel checkout
        ProductsPage productsPage =
                checkoutPage.clickCancel();

        Assert.assertTrue(
                productsPage.isPageLoaded(),
                "Cancel should return user to products page");
    }

    @Test(description =
            "TC_CHECKOUT_006 - Order total displayed on overview page")
    public void testOrderTotalDisplayed() {

        CheckoutPage checkoutPage =
                cartPage.proceedToCheckout();

        checkoutPage.fillShippingInfo(
                "Test",
                "User",
                "12345");

        Assert.assertTrue(
                checkoutPage.isStepTwoLoaded(),
                "Checkout Step Two should load");

        String total =
                checkoutPage.getOrderTotal();

        Assert.assertNotNull(
                total,
                "Total label should exist");

        Assert.assertTrue(
                total.contains("Total:"),
                "Total label should contain 'Total:'");
    }

    @Test(
            description =
                    "TC_CHECKOUT_007 - Data-driven checkout validation",
            dataProvider = "checkoutDataProvider"
    )
    public void testCheckoutDataDriven(
            String firstName,
            String lastName,
            String zipCode,
            String scenario) {

        CheckoutPage checkoutPage =
                cartPage.proceedToCheckout();

        checkoutPage.fillShippingInfo(
                firstName,
                lastName,
                zipCode);

        if ("valid_checkout".equals(scenario)) {

            Assert.assertTrue(
                    checkoutPage.isStepTwoLoaded(),
                    scenario + ": Should proceed to Step Two");

        } else {

            Assert.assertTrue(
                    checkoutPage.isErrorDisplayed(),
                    scenario + ": Validation error should appear");
        }
    }

    @DataProvider(name = "checkoutDataProvider")
    public Object[][] provideCheckoutData() {

        return JsonDataReader.getCheckoutData();
    }
}