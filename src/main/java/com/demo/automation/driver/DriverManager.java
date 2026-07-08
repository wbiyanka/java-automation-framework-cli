// ============================================================
// Package: com.demo.automation.driver
// This package contains classes related to WebDriver management.
// ============================================================
package com.demo.automation.driver;

// Import the WebDriverManager library - handles browser driver binaries automatically
import io.github.bonigarcia.wdm.WebDriverManager;

// Import Selenium WebDriver classes
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * ===================================================================
 * DriverManager - WebDriver Instance Manager (Singleton Pattern)
 * ===================================================================
 * 
 * WHAT IS THIS CLASS?
 * This class is responsible for creating and managing the WebDriver instance.
 * WebDriver is the core object in Selenium that controls the browser.
 * 
 * WHAT IS THE SINGLETON PATTERN?
 * The Singleton pattern ensures that only ONE WebDriver instance exists
 * throughout the entire test execution. This prevents multiple browser
 * windows from opening unintentionally.
 * 
 * WHY DO WE NEED THIS?
 * Without WebDriverManager, you would need to:
 * 1. Download chromedriver.exe manually
 * 2. Know your exact Chrome version
 * 3. Set system properties like:
 *    System.setProperty("webdriver.chrome.driver", "path/to/chromedriver.exe")
 * 
 * WebDriverManager handles all of this automatically!
 * ===================================================================
 */
public class DriverManager {

    // ================================================================
    // STATIC VARIABLE
    // ================================================================
    // The single WebDriver instance shared across the entire framework.
    // "private" means only this class can access it.
    // "static" means it belongs to the class, not to any specific object.
    // Initially null - it will be created when first requested.
    // ================================================================
    private static WebDriver driver;

    /**
     * ================================================================
     * getDriver(String browser)
     * ================================================================
     * Returns the WebDriver instance, creating it if it doesn't exist.
     * 
     * This is a "factory method" - it creates and returns objects
     * based on the input parameter.
     * 
     * @param browser The browser type - "chrome" or "firefox"
     *                (case-insensitive, so "Chrome" and "chrome" both work)
     * @return WebDriver instance for the specified browser
     * 
     * HOW IT WORKS:
     * 1. Check if driver is null (not created yet)
     * 2. If null, create a new driver based on the browser parameter
     * 3. If not null, return the existing driver
     * ================================================================
     */
    public static WebDriver getDriver(String browser) {

        // Only create a new driver if one doesn't already exist
        if (driver == null) {

            // Switch statement - like multiple if-else conditions
            // Converts browser parameter to lowercase for case-insensitive comparison
            switch (browser.toLowerCase()) {

                // ====================================================
                // CASE: Firefox Browser
                // ====================================================
                case "firefox":
                    // WebDriverManager automatically:
                    // 1. Detects your Firefox version
                    // 2. Downloads the matching geckodriver
                    // 3. Sets the system property automatically
                    WebDriverManager.firefoxdriver().setup();

                    // FirefoxOptions lets us configure Firefox behavior
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    
                    // Uncomment the line below to run Firefox in headless mode
                    // Headless means the browser runs without a visible UI
                    // This is useful for CI/CD servers that don't have a display
                    // firefoxOptions.addArguments("--headless");

                    // Create a new Firefox driver instance
                    driver = new FirefoxDriver(firefoxOptions);
                    break;  // Exit the switch statement

                // ====================================================
                // CASE: Chrome Browser (also the DEFAULT option)
                // ====================================================
                case "chrome":
                default:
                    // Same automatic setup as Firefox, but for Chrome
                    WebDriverManager.chromedriver().setup();

                    // ChromeOptions lets us customize Chrome behavior
                    ChromeOptions chromeOptions = new ChromeOptions();
                    
                    // Common useful Chrome options for automation:
                    chromeOptions.addArguments("--start-maximized");     // Start browser maximized
                    chromeOptions.addArguments("--disable-notifications"); // Disable popup notifications
                    chromeOptions.addArguments("--remote-allow-origins=*"); // Fix for Chrome v111+ issues
                    
                    // Uncomment for headless mode (no visible browser UI):
                    // chromeOptions.addArguments("--headless");

                    // Create a new Chrome driver instance
                    driver = new ChromeDriver(chromeOptions);
                    break;
            }
        }

        // Return the WebDriver instance (either newly created or existing)
        return driver;
    }

    /**
     * ================================================================
     * getDriver() - Overloaded method (no parameter)
     * ================================================================
     * This is "method overloading" - same method name, different parameters.
     * This convenience method defaults to Chrome if no browser is specified.
     * 
     * @return WebDriver instance (Chrome by default)
     * ================================================================
     */
    public static WebDriver getDriver() {
        // Call the other getDriver method with "chrome" as the default
        return getDriver("chrome");
    }

    /**
     * ================================================================
     * quitDriver() - Clean up the WebDriver instance
     * ================================================================
     * Closes the browser and terminates the WebDriver session.
     * This is CRITICAL to call after tests to free system resources.
     * 
     * After quitting, sets driver back to null so the next test
     * will create a fresh browser instance.
     * ================================================================
     */
    public static void quitDriver() {
        if (driver != null) {
            // Close the browser and end the session
            driver.quit();
            
            // Reset driver to null so getDriver() creates a new one next time
            driver = null;
        }
    }
}
