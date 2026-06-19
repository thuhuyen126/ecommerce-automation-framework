package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import utils.JsonDataReader;

/**
 * LoginTest - Covers all login scenarios for SauceDemo.
 *
 * Scenarios:
 *   TC_LOGIN_001 - Valid login
 *   TC_LOGIN_002 - Invalid password
 *   TC_LOGIN_003 - Empty username
 *   TC_LOGIN_004 - Empty password
 *   TC_LOGIN_005 - Locked account
 *   TC_LOGIN_006 - Data-driven login (JSON)
 *   TC_LOGIN_007 - Logout
 */
public class LoginTest extends BaseTest {

    @Test(description = "TC_LOGIN_001 - Valid credentials redirect to products page")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.loginAs(
            ConfigReader.getValidUsername(),
            ConfigReader.getValidPassword());

        Assert.assertTrue(productsPage.isPageLoaded(),
            "After valid login, user should be on inventory page");
        Assert.assertEquals(productsPage.getPageTitle(), "Products",
            "Page title should be 'Products'");
    }

    @Test(description = "TC_LOGIN_002 - Invalid password shows error")
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(ConfigReader.getValidUsername());
        loginPage.enterPassword("wrong_password_123");
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
            "Error message should be visible for invalid password");
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("Username and password do not match"),
            "Error text should mention credential mismatch");
    }

    @Test(description = "TC_LOGIN_003 - Empty username shows validation error")
    public void testEmptyUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername("");
        loginPage.enterPassword(ConfigReader.getValidPassword());
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"));
    }

    @Test(description = "TC_LOGIN_004 - Empty password shows validation error")
    public void testEmptyPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(ConfigReader.getValidUsername());
        loginPage.enterPassword("");
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"));
    }

    @Test(description = "TC_LOGIN_005 - Locked account shows locked-out error")
    public void testLockedAccount() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(ConfigReader.getLockedUsername());
        loginPage.enterPassword(ConfigReader.getValidPassword());
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
            "Locked user should see 'locked out' message");
    }

    @Test(description = "TC_LOGIN_006 - Data-driven login from JSON",
          dataProvider = "loginDataProvider")
    public void testLoginDataDriven(String username, String password,
                                    String expectedResult, String description) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        switch (expectedResult) {
            case "success" -> {
                ProductsPage productsPage = new ProductsPage(getDriver());
                Assert.assertTrue(productsPage.isPageLoaded(),
                    description + ": Should land on products page");
            }
            case "locked" -> Assert.assertTrue(
                loginPage.getErrorMessage().contains("locked out"),
                description + ": Should show locked error");
            case "empty_username" -> Assert.assertTrue(
                loginPage.getErrorMessage().contains("Username is required"),
                description + ": Should require username");
            case "empty_password" -> Assert.assertTrue(
                loginPage.getErrorMessage().contains("Password is required"),
                description + ": Should require password");
            default -> Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                description + ": Should show some error");
        }
    }

    @Test(description = "TC_LOGIN_007 - Successful logout returns to login page")
    public void testLogout() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.loginAs(
            ConfigReader.getValidUsername(),
            ConfigReader.getValidPassword());

        Assert.assertTrue(productsPage.isPageLoaded(), "Should be on products page");
        productsPage.logout();

        Assert.assertTrue(loginPage.isPageLoaded(),
            "After logout, user should be back on login page");
    }

    @DataProvider(name = "loginDataProvider", parallel = false)
    public Object[][] provideLoginData() {
        return JsonDataReader.getLoginData();
    }
}
