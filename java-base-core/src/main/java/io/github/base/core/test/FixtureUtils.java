package io.github.base.core.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating test fixtures and dummy data.
 * 
 * <p>This class provides methods for generating test data that is commonly
 * needed in unit tests. It helps reduce boilerplate code in tests and
 * provides consistent test data generation.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Static methods for easy import</li>
 *   <li>Configurable data generation</li>
 *   <li>Realistic test data</li>
 *   <li>Thread-safe operations</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Generate test data
 * String email = FixtureUtils.randomEmail();
 * LocalDate birthDate = FixtureUtils.randomDate();
 * List<String> names = FixtureUtils.randomList(5, () -> FixtureUtils.randomName());
 * 
 * // Create test objects
 * User user = FixtureUtils.createUser();
 * List<User> users = FixtureUtils.createUsers(10);
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public final class FixtureUtils {
    
    private static final String[] FIRST_NAMES = {
        "John", "Jane", "Michael", "Sarah", "David", "Lisa", "Robert", "Emily",
        "James", "Jessica", "William", "Ashley", "Richard", "Amanda", "Charles"
    };
    
    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
        "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez",
        "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"
    };
    
    private static final String[] EMAIL_DOMAINS = {
        "example.com", "test.com", "demo.org", "sample.net", "mock.io"
    };
    
    private static final String[] STREET_NAMES = {
        "Main St", "Oak Ave", "Elm St", "Park Rd", "Cedar Ln", "Maple Dr",
        "Pine St", "First Ave", "Second St", "Third Rd"
    };
    
    private static final String[] CITIES = {
        "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia",
        "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville"
    };
    
    private static final String[] STATES = {
        "NY", "CA", "IL", "TX", "AZ", "PA", "TX", "CA", "TX", "CA", "TX", "FL"
    };
    
    private FixtureUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Generates a random first name.
     * 
     * @return a random first name
     */
    public static String randomFirstName() {
        return FIRST_NAMES[ThreadLocalRandom.current().nextInt(FIRST_NAMES.length)];
    }
    
    /**
     * Generates a random last name.
     * 
     * @return a random last name
     */
    public static String randomLastName() {
        return LAST_NAMES[ThreadLocalRandom.current().nextInt(LAST_NAMES.length)];
    }
    
    /**
     * Generates a random full name.
     * 
     * @return a random full name
     */
    public static String randomName() {
        return randomFirstName() + " " + randomLastName();
    }
    
    /**
     * Generates a random email address.
     * 
     * @return a random email address
     */
    public static String randomEmail() {
        String firstName = randomFirstName().toLowerCase();
        String lastName = randomLastName().toLowerCase();
        String domain = EMAIL_DOMAINS[ThreadLocalRandom.current().nextInt(EMAIL_DOMAINS.length)];
        int number = ThreadLocalRandom.current().nextInt(1000);
        return String.format("%s.%s%d@%s", firstName, lastName, number, domain);
    }
    
    /**
     * Generates a random phone number in US format.
     * 
     * @return a random phone number
     */
    public static String randomPhoneNumber() {
        int areaCode = ThreadLocalRandom.current().nextInt(200, 999);
        int exchange = ThreadLocalRandom.current().nextInt(200, 999);
        int number = ThreadLocalRandom.current().nextInt(1000, 9999);
        return String.format("(%d) %d-%d", areaCode, exchange, number);
    }
    
    /**
     * Generates a random street address.
     * 
     * @return a random street address
     */
    public static String randomStreetAddress() {
        int number = ThreadLocalRandom.current().nextInt(1, 9999);
        String street = STREET_NAMES[ThreadLocalRandom.current().nextInt(STREET_NAMES.length)];
        return String.format("%d %s", number, street);
    }
    
    /**
     * Generates a random city.
     * 
     * @return a random city
     */
    public static String randomCity() {
        return CITIES[ThreadLocalRandom.current().nextInt(CITIES.length)];
    }
    
    /**
     * Generates a random state.
     * 
     * @return a random state
     */
    public static String randomState() {
        return STATES[ThreadLocalRandom.current().nextInt(STATES.length)];
    }
    
    /**
     * Generates a random ZIP code.
     * 
     * @return a random ZIP code
     */
    public static String randomZipCode() {
        return String.format("%05d", ThreadLocalRandom.current().nextInt(10000, 99999));
    }
    
    /**
     * Generates a random date between the specified range.
     * 
     * @param startYear the start year (inclusive)
     * @param endYear the end year (inclusive)
     * @return a random date
     */
    public static LocalDate randomDate(int startYear, int endYear) {
        int year = ThreadLocalRandom.current().nextInt(startYear, endYear + 1);
        int month = ThreadLocalRandom.current().nextInt(1, 13);
        int day = ThreadLocalRandom.current().nextInt(1, 29); // Safe for all months
        return LocalDate.of(year, month, day);
    }
    
    /**
     * Generates a random date between 1950 and 2000.
     * 
     * @return a random date
     */
    public static LocalDate randomDate() {
        return randomDate(1950, 2000);
    }
    
    /**
     * Generates a random date of birth (between 18 and 65 years ago).
     * 
     * @return a random date of birth
     */
    public static LocalDate randomBirthDate() {
        int currentYear = LocalDate.now().getYear();
        return randomDate(currentYear - 65, currentYear - 18);
    }
    
    /**
     * Generates a random date and time.
     * 
     * @return a random date and time
     */
    public static LocalDateTime randomDateTime() {
        LocalDate date = randomDate();
        int hour = ThreadLocalRandom.current().nextInt(24);
        int minute = ThreadLocalRandom.current().nextInt(60);
        int second = ThreadLocalRandom.current().nextInt(60);
        return date.atTime(hour, minute, second);
    }
    
    /**
     * Generates a random integer between the specified range.
     * 
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     * @return a random integer
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
    
    /**
     * Generates a random integer between 0 and 100.
     * 
     * @return a random integer
     */
    public static int randomInt() {
        return randomInt(0, 100);
    }
    
    /**
     * Generates a random long between the specified range.
     * 
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     * @return a random long
     */
    public static long randomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }
    
    /**
     * Generates a random double between the specified range.
     * 
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     * @return a random double
     */
    public static double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    
    /**
     * Generates a random boolean.
     * 
     * @return a random boolean
     */
    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }
    
    /**
     * Generates a random string of the specified length.
     * 
     * @param length the length of the string
     * @return a random string
     */
    public static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    /**
     * Generates a random string of length 10.
     * 
     * @return a random string
     */
    public static String randomString() {
        return randomString(10);
    }
    
    /**
     * Generates a list of random items using the provided supplier.
     * 
     * @param size the size of the list
     * @param supplier the supplier to generate items
     * @param <T> the type of items
     * @return a list of random items
     */
    public static <T> List<T> randomList(int size, java.util.function.Supplier<T> supplier) {
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(supplier.get());
        }
        return list;
    }
    
    /**
     * Generates a set of random items using the provided supplier.
     * 
     * @param size the size of the set
     * @param supplier the supplier to generate items
     * @param <T> the type of items
     * @return a set of random items
     */
    public static <T> Set<T> randomSet(int size, java.util.function.Supplier<T> supplier) {
        Set<T> set = new HashSet<>();
        while (set.size() < size) {
            set.add(supplier.get());
        }
        return set;
    }
    
    /**
     * Generates a random UUID.
     * 
     * @return a random UUID
     */
    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Generates a random element from the provided array.
     * 
     * @param array the array to choose from
     * @param <T> the type of elements
     * @return a random element from the array
     */
    public static <T> T randomElement(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
    
    /**
     * Generates a random element from the provided collection.
     * 
     * @param collection the collection to choose from
     * @param <T> the type of elements
     * @return a random element from the collection
     */
    public static <T> T randomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        List<T> list = new ArrayList<>(collection);
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}
