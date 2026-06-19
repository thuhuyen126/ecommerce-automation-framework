package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.io.File;

/**
 * ExtentReportListener - TestNG listener that generates HTML reports.
 * Attach to testng.xml via <listeners> tag.
 */
public class ExtentReportListener implements ITestListener, ISuiteListener {

    private static final Logger log = LogManager.getLogger(ExtentReportListener.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    // ── Suite-level ───────────────────────────────────────────

    @Override
    public void onStart(ISuite suite) {
        String reportPath = ConfigReader.getReportPath() + "extent-report.html";

        // Create reports directory if missing
        new File(ConfigReader.getReportPath()).mkdirs();

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setDocumentTitle("E-Commerce Automation Report");
        spark.config().setReportName(ConfigReader.get("report.name"));
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setEncoding("UTF-8");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Project",     "E-Commerce Automation");
        extent.setSystemInfo("Tester",      "QA Team");
        extent.setSystemInfo("Environment", ConfigReader.getEnv().toUpperCase());
        extent.setSystemInfo("Browser",     ConfigReader.getBrowser());
        extent.setSystemInfo("Base URL",    ConfigReader.getBaseUrl());

        log.info("Extent Report initialized at: {}", reportPath);
    }

    @Override
    public void onFinish(ISuite suite) {
        if (extent != null) {
            extent.flush();
            log.info("Extent Report saved.");
        }
    }

    // ── Test-level ────────────────────────────────────────────

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest test = extent.createTest(testName,
            (description != null && !description.isEmpty()) ? description : testName);
        testThread.set(test);
        log.info("Starting test: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().log(Status.PASS, "Test PASSED ✓");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        getTest().log(Status.FAIL, "Test FAILED ✗");
        getTest().log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        getTest().log(Status.SKIP, "Test SKIPPED");
        if (result.getThrowable() != null) {
            getTest().log(Status.SKIP, result.getThrowable());
        }
    }

    // ── Utilities ─────────────────────────────────────────────

    public static ExtentTest getTest() {
        return testThread.get();
    }

    /**
     * Attaches a screenshot to the current test in the Extent Report.
     * Called from BaseTest.tearDown() on failure.
     */
    public static void attachScreenshot(String screenshotPath) {
        if (screenshotPath != null && getTest() != null) {
            try {
                getTest().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
            } catch (Exception e) {
                log.warn("Could not attach screenshot to report: {}", e.getMessage());
            }
        }
    }
}
