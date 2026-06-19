package utils;

import config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil - Captures browser screenshots on test failure.
 * Screenshots are saved to the configured screenshot path with timestamp.
 */
public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);

    private ScreenshotUtil() {}

    /**
     * Captures a screenshot and saves it to the screenshots directory.
     *
     * @param driver   Active WebDriver instance
     * @param testName Name of the test method (used in filename)
     * @return Absolute path of the saved screenshot, or null on failure
     */
    public static String capture(WebDriver driver, String testName) {
        if (driver == null) {
            log.warn("Driver is null, cannot capture screenshot.");
            return null;
        }

        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = testName + "_" + timestamp + ".png";
        String screenshotDir = ConfigReader.getScreenshotPath();
        String fullPath = screenshotDir + fileName;

        try {
            // Create directory if it doesn't exist
            File dir = new File(screenshotDir);
            if (!dir.exists()) dir.mkdirs();

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(fullPath);
            FileUtils.copyFile(srcFile, destFile);

            log.info("Screenshot saved: {}", fullPath);
            return destFile.getAbsolutePath();
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }
}
