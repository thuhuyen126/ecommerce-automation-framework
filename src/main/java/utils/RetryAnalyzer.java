package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log =
            LogManager.getLogger(RetryAnalyzer.class);

    private static final int MAX_RETRY = 1;

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {

        if (retryCount < MAX_RETRY_COUNT) {

            retryCount++;

            log.warn(
                    "Retrying test '{}' ({}/{})",
                    result.getMethod().getMethodName(),
                    retryCount,
                    MAX_RETRY_COUNT);

            return true;
        }

        return false;
    }
}