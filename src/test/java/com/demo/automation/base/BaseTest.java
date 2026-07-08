// ============================================================
// Package: com.demo.automation.base (test version)
// Base classes for tests go here.
// ============================================================
package com.demo.automation.base;

import com.demo.automation.driver.DriverManager;
import com.demo.automation.utils.ConfigReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

/**
 * ===================================================================
 * BaseTest - Parent of All Test Classes
 * ===================================================================
 * 
 * WHAT IS A TEST BASE CLASS?
 * It contains setup and teardown code that ALL tests share.
 * Instead of duplicating WebDriver initialization in every test class,
 * we put it here and let test classes EXTEND this class.
 * 
 * TEST LIFECYCLE (order of execution):
 * 
 *   [BeforeEach] setUp()          → Creates browser, navigates to URL
 *   [Test]       testSomething()  → Your actual test code
 *   [AfterEach]  tearDown()       → Closes browser, frees resources
 * 
 * JUnit 5 ANNOTATIONS USED:
 * @BeforeEach - runs BEFORE each @Test method
 * @AfterEach  - runs AFTER each @Test method
 * 
 * WHY INHERITANCE?
 * When LoginTest extends BaseTest, it automatically gets setUp() and
 * tearDown(). The test class only needs to write test methods.
 * 
 *   public class LoginTest extends BaseTest {
 *       @Test
 *       public void testValidLogin() {
 *           // driver is already available! (protected field)
 *           // URL is already loaded! (done in setUp)
 *           LoginPage loginPage = new LoginPage(driver);
 *           // ... your test logic ...
 *       }
 *   }
 * ===================================================================
 */
public class BaseTest {

    // ================================================================
    // PROTECTED FIELD
    // "protected" means child classes (LoginTest, etc.) can access this.
    // Tests use this driver to create page objects.
    // ================================================================
    protected WebDriver driver;

    /**
     * ================================================================
     * setUp() - Runs BEFORE Every Test Method
     * ================================================================
     * 
     * @BeforeEach tells JUnit to run this method before each @Test.
     * 
     * WHAT THIS METHOD DOES:
     * 1. Creates a new WebDriver instance (opens a browser)
     * 2. Navigates to the application URL (from config.properties)
     * 
     * Each test starts with a FRESH browser at the login page.
     * This ensures tests are independent and don't share state.
     * ================================================================
     */
    @BeforeEach
    public void setUp() {
        // Step 1: Get the browser type from config.properties
        // ConfigReader reads configuration files for us
        String browser = ConfigReader.getBrowser();
        
        // Step 2: Create a WebDriver instance for the specified browser
        // DriverManager handles browser driver setup automatically
        driver = DriverManager.getDriver(browser);
        
        // Step 3: Navigate to the application URL
        // The URL is stored in config.properties so it can be changed easily
        driver.get(ConfigReader.getApplicationUrl());
        
        // Optional: Print confirmation for debugging
        System.out.println("✓ Browser opened: " + browser);
        System.out.println("✓ Navigated to: " + ConfigReader.getApplicationUrl());
    }

    /**
     * ================================================================
     * tearDown() - Runs AFTER Every Test Method
     * ================================================================
     * 
     * @AfterEach tells JUnit to run this method after each @Test,
     * regardless of whether the test passed or failed.
     * 
     * WHAT THIS METHOD DOES:
     * 1. Closes the browser
     * 2. Terminates the WebDriver session
     * 3. Frees system resources (memory, processes)
     * 
     * WHY IS CLEANUP IMPORTANT?
     * - Each open browser uses memory and CPU
     * - If tests don't clean up, you'll have many browser processes
     * - Tests could interfere with each other (shared cookies, sessions)
     * ================================================================
     */
    @AfterEach
    public void tearDown() {
        // Close the browser and clean up WebDriver resources
        DriverManager.quitDriver();
        
        System.out.println("✓ Browser closed. Test cleanup complete.");
    }
}
