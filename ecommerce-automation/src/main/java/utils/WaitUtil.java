package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtil - Centralized explicit wait utility methods.
 * Avoids duplicating WebDriverWait logic across page classes.
 */
public class WaitUtil {

    private static final Logger log = LogManager.getLogger(WaitUtil.class);

    private WaitUtil() {}

    public static WebElement waitForVisible(WebDriver driver, By locator, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds))
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds))
            .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForUrlContains(WebDriver driver, String urlFragment, int seconds) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.urlContains(urlFragment));
        } catch (TimeoutException e) {
            log.warn("URL did not contain '{}' within {} seconds", urlFragment, seconds);
            return false;
        }
    }

    public static void hardWait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
