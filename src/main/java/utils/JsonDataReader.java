package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * JsonDataReader - Reads JSON test data files for data-driven testing.
 * Used with TestNG @DataProvider to supply test inputs.
 */
public class JsonDataReader {

    private static final Logger log = LogManager.getLogger(JsonDataReader.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DATA_PATH = "src/test/resources/testdata/";

    private JsonDataReader() {}

    /**
     * Generic method to read any JSON array file into Object[][] for @DataProvider.
     */
    public static Object[][] readJsonFile(String fileName, String... keys) {
        try {
            List<Map<String, Object>> data = mapper.readValue(
                new File(DATA_PATH + fileName),
                new TypeReference<>() {});

            Object[][] result = new Object[data.size()][keys.length];
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < keys.length; j++) {
                    result[i][j] = data.get(i).get(keys[j]);
                }
            }
            log.info("Loaded {} records from {}", data.size(), fileName);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read test data: " + fileName, e);
        }
    }

    /**
     * Login test data provider.
     * Returns: [username, password, expectedResult, description]
     */
    public static Object[][] getLoginData() {
        return readJsonFile("login_data.json",
            "username", "password", "expectedResult", "description");
    }

    /**
     * Checkout test data provider.
     * Returns: [firstName, lastName, zipCode, scenario]
     */
    public static Object[][] getCheckoutData() {
        return readJsonFile("checkout_data.json",
            "firstName", "lastName", "zipCode", "scenario");
    }
}
