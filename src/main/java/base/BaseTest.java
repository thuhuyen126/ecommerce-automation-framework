package base;

import config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import utils.ExtentReportListener;
import utils.ScreenshotUtil;

import java.time.Duration;

/**
 * BaseTest
 *
 * Parent class for all test classes.
 *
 * Responsibilities:
 * - Driver initialization
 * - Browser setup
 * - Wait setup
 * - Parallel execution support
 * - Screenshot capture on failure
 * - Browser cleanup
 */
public class BaseTest {

    private static final Logger log =
            LogManager.getLogger(BaseTest.class);

    // Thread-safe WebDriver for parallel execution
    private static final ThreadLocal<WebDriver> driverThread =
            new ThreadLocal<>();

    private static final ThreadLocal<WebDriverWait> waitThread =
            new ThreadLocal<>();

    // ─────────────────────────────────────────
    // Driver Accessors
    // ─────────────────────────────────────────

    protected WebDriver getDriver() {
        return driverThread.get();
    }

    protected WebDriverWait getWait() {
        return waitThread.get();
    }

    // ─────────────────────────────────────────
    // Setup
    // ─────────────────────────────────────────

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(
            @Optional("") String browser) {

        String browserToUse =
                resolveBrowser(browser);

        log.info(
                "======================================");

        log.info(
                "STARTING TEST | Browser: {} | Env: {}",
                browserToUse,
                ConfigReader.getEnv());

        log.info(
                "======================================");

        WebDriver driver =
        createDriver(browserToUse);

configureDriver(driver);

// Clear cookies before every test
driver.manage().deleteAllCookies();

        WebDriverWait wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(
                                ConfigReader.getExplicitWait()));

        driverThread.set(driver);
        waitThread.set(wait);

        navigateToApplication(driver);
    }

    // ─────────────────────────────────────────
    // Browser Resolution
    // ─────────────────────────────────────────

    /**
     * Browser priority:
     * 1. JVM property
     * 2. TestNG parameter
     * 3. config.properties
     */
    private String resolveBrowser(String browser) {

        String browserToUse =
                System.getProperty("browser");

        if (browserToUse == null
                || browserToUse.isBlank()) {

            browserToUse = browser;
        }

        if (browserToUse == null
                || browserToUse.isBlank()) {

            browserToUse =
                    ConfigReader.getBrowser();
        }

        return browserToUse.trim().toLowerCase();
    }

    // ─────────────────────────────────────────
    // Driver Factory
    // ─────────────────────────────────────────

    private WebDriver createDriver(String browser) {

    boolean headless = ConfigReader.isHeadless();

    // Nếu chạy trên GitHub Actions thì bắt buộc headless
    if (System.getenv("GITHUB_ACTIONS") != null) {
        headless = true;
    }

    log.info("Headless mode: {}", headless);

    return switch (browser) {

        case "chrome" -> createChromeDriver(headless);

        case "edge" -> createEdgeDriver(headless);

        case "firefox" -> createFirefoxDriver(headless);

        default -> throw new IllegalArgumentException(
                "Unsupported browser: "
                        + browser
                        + " | Supported: chrome | edge | firefox");
    };
}

    // ─────────────────────────────────────────
    // Chrome
    // ─────────────────────────────────────────

    private WebDriver createChromeDriver(boolean headless) {

    WebDriverManager.chromedriver().setup();

    ChromeOptions options = new ChromeOptions();

    options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

    options.addArguments(
            "--disable-notifications",
            "--disable-popup-blocking",
            "--disable-infobars",
            "--disable-extensions",
            "--remote-allow-origins=*",
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--window-size=1920,1080"
    );

    if (headless) {

        options.addArguments(
                "--headless=new"
        );
    }

    options.setAcceptInsecureCerts(true);

    return new ChromeDriver(options);
}

    private void setupCommonChromeOptions(
        ChromeOptions options) {

    options.setPageLoadStrategy(
            PageLoadStrategy.NORMAL);

    options.addArguments(
            "--disable-notifications",
            "--disable-popup-blocking",
            "--disable-infobars",
            "--disable-extensions",
            "--remote-allow-origins=*",
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--window-size=1920,1080");

    options.setAcceptInsecureCerts(true);
}

    // ─────────────────────────────────────────
    // Edge
    // ─────────────────────────────────────────

    private WebDriver createEdgeDriver(
            boolean headless) {

        WebDriverManager.edgedriver().setup();

        EdgeOptions options =
                new EdgeOptions();

        options.setPageLoadStrategy(
                PageLoadStrategy.NORMAL);

        options.addArguments(
                "--disable-notifications",
                "--disable-popup-blocking",
                "--start-maximized");

        if (headless) {

            options.addArguments(
                    "--headless=new",
                    "--window-size=1920,1080");
        }

        return new EdgeDriver(options);
    }

    // ─────────────────────────────────────────
    // Firefox
    // ─────────────────────────────────────────

    private WebDriver createFirefoxDriver(
            boolean headless) {

        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options =
                new FirefoxOptions();

        options.setPageLoadStrategy(
                PageLoadStrategy.NORMAL);

        if (headless) {

            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }

    // ─────────────────────────────────────────
    // Driver Configuration
    // ─────────────────────────────────────────

    private void configureDriver(
        WebDriver driver) {

    driver.manage().timeouts()
            .implicitlyWait(
                    Duration.ofSeconds(
                            ConfigReader.getImplicitWait()));

    driver.manage().timeouts()
            .pageLoadTimeout(
                    Duration.ofSeconds(
                            ConfigReader.getPageLoadTimeout()));

    driver.manage().timeouts()
            .scriptTimeout(
                    Duration.ofSeconds(30));
}

    // ─────────────────────────────────────────
    // Navigation
    // ─────────────────────────────────────────

    private void navigateToApplication(
            WebDriver driver) {

        String url =
                ConfigReader.getBaseUrl();

        log.info("Opening URL: {}", url);

        driver.get(url);

        log.info("Application loaded successfully.");
    }

    // ─────────────────────────────────────────
    // Teardown
    // ─────────────────────────────────────────

    @AfterMethod(alwaysRun = true)
    public void tearDown(
            ITestResult result) {

        WebDriver driver =
                getDriver();

        try {

            handleTestResult(result, driver);

        } finally {

            quitDriver(driver);
        }
    }

    // ─────────────────────────────────────────
    // Result Handling
    // ─────────────────────────────────────────

    private void handleTestResult(
            ITestResult result,
            WebDriver driver) {

        String testName =
                result.getMethod().getMethodName();

        switch (result.getStatus()) {

            case ITestResult.SUCCESS -> {

                log.info(
                        "TEST PASSED: {}",
                        testName);
            }

            case ITestResult.FAILURE -> {

                log.error(
                        "TEST FAILED: {}",
                        testName);

                log.error(
                        "Failure reason:",
                        result.getThrowable());

                captureFailureScreenshot(
                        driver,
                        testName);
            }

            case ITestResult.SKIP -> {

                log.warn(
                        "TEST SKIPPED: {}",
                        testName);
            }

            default -> log.warn(
                    "UNKNOWN TEST STATUS: {}",
                    testName);
        }
    }

    // ─────────────────────────────────────────
    // Screenshot
    // ─────────────────────────────────────────

    private void captureFailureScreenshot(
            WebDriver driver,
            String testName) {

        try {

            String screenshotPath =
                    ScreenshotUtil.capture(
                            driver,
                            testName);

            ExtentReportListener.attachScreenshot(
                    screenshotPath);

            log.info(
                    "Screenshot captured: {}",
                    screenshotPath);

        } catch (Exception e) {

            log.error(
                    "Failed to capture screenshot.",
                    e);
        }
    }

    // ─────────────────────────────────────────
    // Driver Cleanup
    // ─────────────────────────────────────────

    private void quitDriver(
            WebDriver driver) {

        try {

            if (driver != null) {

                driver.quit();

                log.info("Browser closed successfully.");
            }

        } catch (Exception e) {

            log.error(
                    "Error while closing browser.",
                    e);

        } finally {

            driverThread.remove();
            waitThread.remove();
        }
    }
}