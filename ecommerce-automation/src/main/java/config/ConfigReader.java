package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration from config.properties.
 * Supports system property overrides for CI/CD (e.g., -Dbrowser=edge).
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final Properties props = new Properties();

    static {
    try {

        props.load(
            ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")
        );

        log.info("Configuration loaded successfully.");

    } catch (IOException e) {

        throw new RuntimeException(
            "❌ config.properties not found!",
            e
        );
    }
    }

    /**
     * Get a value by key. System property overrides file value (for CI/CD).
     */
    public static String get(String key) {
        // Allow -Dkey=value overrides from Maven/command line
        String sysValue = System.getProperty(key);
        return (sysValue != null && !sysValue.isEmpty()) ? sysValue : props.getProperty(key);
    }

    public static String getBaseUrl()        { return get("base.url"); }
    public static String getBrowser()        { return get("browser"); }
    public static String getEnv()            { return get("env"); }
    public static boolean isHeadless()       { return Boolean.parseBoolean(get("headless")); }
    public static int getImplicitWait()      { return Integer.parseInt(get("implicit.wait")); }
    public static int getExplicitWait()      { return Integer.parseInt(get("explicit.wait")); }
    public static int getPageLoadTimeout()   { return Integer.parseInt(get("page.load.timeout")); }
    public static String getReportPath()     { return get("report.path"); }
    public static String getScreenshotPath() { return get("screenshot.path"); }
    public static String getValidUsername()  { return get("valid.username"); }
    public static String getValidPassword()  { return get("valid.password"); }
    public static String getLockedUsername() { return get("locked.username"); }
}
