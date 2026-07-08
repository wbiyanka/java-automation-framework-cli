// ============================================================
// Package: com.demo.automation.tests
// This package contains all test classes.
// ============================================================
package com.demo.automation.tests;

// Import BaseTest - our parent class that handles setup/teardown
import com.demo.automation.base.BaseTest;

// Import page objects to interact with the application
import com.demo.automation.pages.InventoryPage;
import com.demo.automation.pages.LoginPage;

// Import ConfigReader to access test data from config.properties
import com.demo.automation.utils.ConfigReader;

// Import JUnit 5 classes for assertions and test annotations
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ===================================================================
 * LoginTest - Test Cases for Login Functionality
 * ===================================================================
 * 
 * WHAT DOES THIS CLASS TEST?
 * It tests the login page of SauceDemo, covering:
 * - Successful login with valid credentials
 * - Error handling for invalid credentials
 * - Locked out user scenario
 * 
 * KEY CONCEPT: Test Structure (Arrange-Act-Assert)
 * Every test method follows this pattern:
 * 
 * 1. ARRANGE (or Given)
 *    - Set up the test data and page objects
 *    - Example: Create LoginPage object
 * 
 * 2. ACT (or When)
 *    - Perform the action being tested
 *    - Example: Click the login button
 * 
 * 3. ASSERT (or Then)
 *    - Verify the expected outcome
 *    - Example: Check that inventory page is displayed
 * 
 * This is called the "Given-When-Then" pattern in BDD.
 * ===================================================================
 */
public class LoginTest extends BaseTest {

    /**
     * ================================================================
     * Test Case 1: Valid Login with Standard User
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am on the login page
     *   When I enter valid username and password
     *   And I click the login button
     *   Then I should be redirected to the inventory page
     * 
     * PURPOSE:
     *   Verify that a standard user can log in successfully.
     *   This is a "happy path" or "positive" test case.
     * 
     * @DisplayName provides a human-readable name in test reports
     * ================================================================
     */
    @Test
    @DisplayName("Verify valid user can login successfully")
    public void testValidLogin() {

        // ============================================================
        // ARRANGE: Prepare the test
        // ============================================================
        // Create the LoginPage object to interact with the login page
        // "driver" is inherited from BaseTest (it's a protected field)
        LoginPage loginPage = new LoginPage(driver);

        // ============================================================
        // ACT: Perform the login action
        // ============================================================
        // Use the convenience method loginAs() that combines:
        //   enterUsername() + enterPassword() + clickLogin()
        loginPage.loginAs(
            ConfigReader.getUsername(),  // "standard_user"
            ConfigReader.getPassword()   // "secret_sauce"
        );

        // ============================================================
        // ASSERT: Verify the expected outcome
        // ============================================================
        // After successful login, we should be on the inventory page
        InventoryPage inventoryPage = new InventoryPage(driver);

        // AssertTrue checks that the condition is TRUE
        // If isOnInventoryPage() returns false, the test FAILS
        Assertions.assertTrue(inventoryPage.isOnInventoryPage(),
            "User should be redirected to the inventory page after valid login");

        // Also verify the page title is exactly "Products"
        Assertions.assertEquals("Products", inventoryPage.getPageTitle(),
            "The inventory page should display 'Products' as the title");

        // If both assertions pass, the test passes!
    }

    /**
     * ================================================================
     * Test Case 2: Invalid Login with Wrong Credentials
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am on the login page
     *   When I enter INVALID username and password
     *   And I click the login button
     *   Then I should see an error message
     *   And I should remain on the login page
     * 
     * PURPOSE:
     *   Verify that the application properly rejects bad credentials.
     *   This is a "negative" or "error path" test case.
     * ================================================================
     */
    @Test
    @DisplayName("Verify error message for invalid login credentials")
    public void testInvalidLogin() {

        // ARRANGE: Create LoginPage object
        LoginPage loginPage = new LoginPage(driver);

        // ACT: Attempt login with clearly invalid credentials
        // These credentials don't exist in the system
        loginPage.loginAs("invalid_user", "wrong_password");

        // ASSERT 1: Check that an error message is displayed
        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid login");

        // ASSERT 2: Check that the error message contains expected text
        // The actual message on SauceDemo is:
        // "Epic sadface: Username and password do not match any user in this service"
        Assertions.assertTrue(
            loginPage.getErrorMessage().contains("Username and password do not match"),
            "Error message should indicate username/password mismatch"
        );
    }

    /**
     * ================================================================
     * Test Case 3: Locked Out User Cannot Login
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am on the login page
     *   When I enter the locked_out_user credentials
     *   And I click the login button
     *   Then I should see a "locked out" error message
     * 
     * PURPOSE:
     *   Verify that the application prevents locked users from logging in.
     *   This demonstrates testing business rule validations.
     * ================================================================
     */
    @Test
    @DisplayName("Verify locked out user cannot login")
    public void testLockedOutUser() {

        // ARRANGE: Create LoginPage object
        LoginPage loginPage = new LoginPage(driver);

        // ACT: Attempt login with the locked out user
        // SauceDemo provides this special test user
        // Note: We use ConfigReader.getPassword() since the password is the same
        loginPage.loginAs("locked_out_user", ConfigReader.getPassword());

        // ASSERT 1: Error message should be displayed
        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for locked out user");

        // ASSERT 2: Error message should mention "locked out"
        Assertions.assertTrue(
            loginPage.getErrorMessage().contains("locked out"),
            "Error message should indicate the user is locked out"
        );
    }
}
