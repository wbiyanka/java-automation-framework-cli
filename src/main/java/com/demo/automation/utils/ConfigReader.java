// ============================================================
// Package: com.demo.automation.utils
// This package contains utility/helper classes used across the framework.
// ============================================================
package com.demo.automation.utils;

// Import Java's Properties class - reads .properties configuration files
import java.io.InputStream;
import java.util.Properties;

/**
 * ===================================================================
 * ConfigReader - Read Configuration from Properties File
 * ===================================================================
 * 
 * WHAT IS THIS CLASS?
 * This utility class reads configuration settings from
 * "config.properties" file located in src/test/resources/.
 * 
 * WHY DO WE NEED THIS?
 * Hard-coding values like URLs, browser names, and passwords in test
 * code is bad practice. Instead, we store them in a configuration file.
 * 
 * BENEFITS OF EXTERNALIZING CONFIGURATION:
 * 1. Change settings without modifying code
 * 2. Run tests against different environments (dev, staging, prod)
 * 3. Keep sensitive data (passwords) out of the source code
 * 4. Make the framework more flexible and reusable
 * 
 * KEY CONCEPT: Static Initializer
 * The "static { ... }" block runs automatically when the class
 * is first loaded into memory. This ensures config is loaded
 * before any test runs.
 * ===================================================================
 */
public class ConfigReader {

    // ================================================================
    // Properties object - stores key-value pairs from the config file
    // "private" - only this class can access it
    // "static" - shared across all instances; loaded once
    // ================================================================
    private static Properties properties = new Properties();

    /**
     * Static Initializer Block
     * ---------------------------
     * This code runs ONCE when the class is first loaded by the JVM.
     * It loads the config.properties file from the test resources folder.
     * 
     * The "src/test/resources" folder is automatically added to the
     * classpath by Maven, meaning files there can be loaded using
     * getResourceAsStream().
     */
    static {
        try {
            // Get the config.properties file as an input stream
            // getClassLoader() loads files from the classpath
            // getResourceAsStream() reads the file content
            InputStream inputStream = ConfigReader.class.getClassLoader()
                    .getResourceAsStream("config.properties");

            // Check if the file was found
            if (inputStream != null) {
                // Load all key-value pairs from the file into the Properties object
                properties.load(inputStream);
                
                // Always close streams to free system resources
                inputStream.close();
                
                System.out.println("✓ Config loaded successfully from config.properties");
            } else {
                // File not found - throw an error with helpful message
                throw new RuntimeException(
                    "config.properties file not found in src/test/resources/\n" +
                    "Please create this file with the required configuration."
                );
            }
        } catch (Exception e) {
            // Print the error and re-throw to stop execution
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration: " + e.getMessage());
        }
    }

    /**
     * ================================================================
     * Configuration Getter Methods
     * ================================================================
     * Each method reads a specific property from the config file.
     * 
     * WHY USE SEPARATE METHODS?
     * Instead of calling properties.getProperty() everywhere,
     * these methods provide:
     * 1. Clear names (getApplicationUrl vs getProperty("app.url"))
     * 2. Type conversion (getExplicitWait returns int, not String)
     * 3. Centralized error handling
     * 4. Easy to find all config values used in the framework
     * ================================================================
     */

    /**
     * @return The base URL of the application under test
     *         (from config.properties: "app.url")
     */
    public static String getApplicationUrl() {
        return properties.getProperty("app.url");
    }

    /**
     * @return The browser to use for tests: "chrome" or "firefox"
     *         (from config.properties: "browser")
     */
    public static String getBrowser() {
        return properties.getProperty("browser");
    }

    /**
     * @return The default explicit wait timeout in SECONDS
     *         (from config.properties: "explicit.wait")
     *         
     * NOTE: We use Integer.parseInt() to convert the String from
     * the properties file into an integer. If the value is not
     * a valid number, this will throw a NumberFormatException.
     */
    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait"));
    }

    /**
     * @return Standard test username for login
     *         (from config.properties: "username")
     */
    public static String getUsername() {
        return properties.getProperty("username");
    }

    /**
     * @return Standard test password for login
     *         (from config.properties: "password")
     */
    public static String getPassword() {
        return properties.getProperty("password");
    }
}
