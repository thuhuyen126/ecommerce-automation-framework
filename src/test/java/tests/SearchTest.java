package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;

import java.util.List;

/**
 * SearchTest - Covers product browsing and sorting scenarios.
 *
 * Scenarios:
 *   TC_SEARCH_001 - Products page loads with items
 *   TC_SEARCH_002 - Sort by name A-Z
 *   TC_SEARCH_003 - Sort by name Z-A
 *   TC_SEARCH_004 - Sort by price low to high
 *   TC_SEARCH_005 - Sort by price high to low
 *   TC_SEARCH_006 - Verify specific product exists
 */
public class SearchTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeSearch() {
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage.loginAs(
            ConfigReader.getValidUsername(),
            ConfigReader.getValidPassword());
    }

    @Test(description = "TC_SEARCH_001 - Products page loads with at least 1 product")
    public void testProductsPageLoads() {
        Assert.assertTrue(productsPage.isPageLoaded(),
            "Products page should be loaded");
        Assert.assertTrue(productsPage.getProductCount() > 0,
            "At least one product should be visible");
    }

    @Test(description = "TC_SEARCH_002 - Sort by name A-Z")
    public void testSortByNameAZ() {
        productsPage.sortProducts("az");
        List<String> names = productsPage.getAllProductNames();

        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(
                names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0,
                "Products should be sorted A-Z");
        }
    }

    @Test(description = "TC_SEARCH_003 - Sort by name Z-A")
    public void testSortByNameZA() {
        productsPage.sortProducts("za");
        List<String> names = productsPage.getAllProductNames();

        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(
                names.get(i).compareToIgnoreCase(names.get(i + 1)) >= 0,
                "Products should be sorted Z-A");
        }
    }

    @Test(description = "TC_SEARCH_004 - Sort by price low to high")
    public void testSortByPriceLowToHigh() {
        productsPage.sortProducts("lohi");
        List<String> prices = productsPage.getAllProductPrices();

        for (int i = 0; i < prices.size() - 1; i++) {
            double price1 = Double.parseDouble(prices.get(i).replace("$", ""));
            double price2 = Double.parseDouble(prices.get(i + 1).replace("$", ""));
            Assert.assertTrue(price1 <= price2,
                "Prices should be in ascending order");
        }
    }

    @Test(description = "TC_SEARCH_005 - Sort by price high to low")
    public void testSortByPriceHighToLow() {
        productsPage.sortProducts("hilo");
        List<String> prices = productsPage.getAllProductPrices();

        for (int i = 0; i < prices.size() - 1; i++) {
            double price1 = Double.parseDouble(prices.get(i).replace("$", ""));
            double price2 = Double.parseDouble(prices.get(i + 1).replace("$", ""));
            Assert.assertTrue(price1 >= price2,
                "Prices should be in descending order");
        }
    }

    @Test(description = "TC_SEARCH_006 - Verify Sauce Labs Backpack is listed")
    public void testSpecificProductExists() {
        List<String> names = productsPage.getAllProductNames();
        Assert.assertTrue(
            names.stream().anyMatch(n -> n.contains("Sauce Labs Backpack")),
            "Sauce Labs Backpack should be listed in products");
    }
}
