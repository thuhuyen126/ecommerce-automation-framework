package utils;

import net.datafaker.Faker;

import java.util.Locale;

/**
 * FakerUtil - Generates random but realistic test data using DataFaker.
 * Useful for checkout forms, user registration, etc.
 */
public class FakerUtil {

    private static final Faker faker = new Faker(new Locale("en-US"));

    private FakerUtil() {}

    public static String getFirstName()    { return faker.name().firstName(); }
    public static String getLastName()     { return faker.name().lastName(); }
    public static String getFullName()     { return faker.name().fullName(); }
    public static String getEmail()        { return faker.internet().emailAddress(); }
    public static String getZipCode()      { return faker.address().zipCode(); }
    public static String getCity()         { return faker.address().city(); }
    public static String getPhoneNumber()  { return faker.phoneNumber().phoneNumber(); }
    public static String getPassword()     { return faker.internet().password(8, 16, true, true); }
    public static String getProductName()  { return faker.commerce().productName(); }

    /** Returns a random price between min and max. */
    public static double getPrice(double min, double max) {
        return Math.round((min + Math.random() * (max - min)) * 100.0) / 100.0;
    }
}
