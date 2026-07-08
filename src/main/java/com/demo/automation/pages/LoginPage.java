// ============================================================
// Package: com.demo.automation.pages
// This package contains Page Object classes - one per web page.
// ============================================================
package com.demo.automation.pages;

import com.demo.automation.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ===================================================================
 * LoginPage - Page Object for the SauceDemo Login Page
 * ===================================================================
 * 
 * WHAT IS A PAGE OBJECT?
 * A Page Object represents a single web page in the application.
 * It contains:
 * 1. Element LOCATORS - how to find elements on this page
 * 2. Action METHODS - what users can DO on this page
 * 
 * KEY CONCEPT: Encapsulation
 * The locators are PRIVATE (hidden) and the methods are PUBLIC (exposed).
 * Tests don't need to know HOW elements are found - they just use the methods.
 * 
 * EXAMPLE:
 * Instead of: driver.findElement(By.id("user-name")).sendKeys("standard_user")
 * Tests use: loginPage.enterUsername("standard_user")
 * 
 * THIS IS FOR SAUCEDEMO.COM - a practice/demo website
 * URL: https://www.saucedemo.com/
 * ===================================================================
 */
public class LoginPage extends BasePage {

    // ================================================================
    // ELEMENT LOCATORS
    // ================================================================
    // 
    // @FindBy is a PageFactory annotation that tells Selenium HOW to
    // find an element on the page. It replaces driver.findElement().
    // 
    // COMMON @FindBy STRATEGIES:
    //   @FindBy(id = "value")           - Find by ID attribute
    //   @FindBy(name = "value")         - Find by name attribute
    //   @FindBy(className = "value")    - Find by CSS class
    //   @FindBy(css = "selector")       - Find by CSS selector
    //   @FindBy(xpath = "expression")   - Find by XPath
    //   @FindBy(tagName = "div")        - Find by HTML tag
    // ================================================================

    // Username input field - identified by its HTML id attribute "user-name"
    // HTML: <input id="user-name" type="text" placeholder="Username">
    @FindBy(id = "user-name")
    private WebElement usernameInput;

    // Password input field
    @FindBy(id = "password")
    private WebElement passwordInput;

    // Login button
    @FindBy(id = "login-button")
    private WebElement loginButton;

    // Error message container (appears when login fails)
    // CSS class used here since the element has no unique id
    @FindBy(css = ".error-message-container")
    private WebElement errorMessageContainer;

    /**
     * Constructor - passes WebDriver to the parent BasePage class.
     * BasePage's constructor then calls PageFactory.initElements()
     * to initialize all @FindBy annotated fields.
     * 
     * @param driver The WebDriver instance from the test
     */
    public LoginPage(WebDriver driver) {
        // "super" calls the parent class (BasePage) constructor
        super(driver);
    }

    // ================================================================
    // PAGE ACTION METHODS
    // ================================================================
    // 
    // Each method represents a user action on this page.
    // Methods return "this" (the current page object) to enable
    // METHOD CHAINING - calling multiple methods in one line:
    //   loginPage.enterUsername("user").enterPassword("pass").clickLogin();
    // ================================================================

    /**
     * Types the given username into the username input field.
     * 
     * @param username The text to enter (e.g., "standard_user")
     * @return LoginPage instance (for method chaining)
     */
    public LoginPage enterUsername(String username) {
        // sendKeys() is inherited from BasePage - it clears and types text
        sendKeys(usernameInput, username);
        return this;  // Return "this" to enable chaining
    }

    /**
     * Types the given password into the password input field.
     * 
     * NOTE: In real projects, never hard-code or log passwords!
     * 
     * @param password The password text
     * @return LoginPage instance (for method chaining)
     */
    public LoginPage enterPassword(String password) {
        sendKeys(passwordInput, password);
        return this;
    }

    /**
     * Clicks the Login button to submit the login form.
     * 
     * This method returns void because the next page depends on
     * the credentials used:
     * - Valid credentials -> redirects to InventoryPage
     * - Invalid credentials -> stays on LoginPage (shows error)
     * The test should create the appropriate page object after calling this.
     */
    public void clickLogin() {
        // click() is inherited from BasePage - it waits for clickability first
        click(loginButton);
    }

    /**
     * ================================================================
     * Convenience Method: Complete Login Flow
     * ================================================================
     * 
     * Instead of calling three separate methods:
     *   loginPage.enterUsername("user")
     *           .enterPassword("pass")
     *           .clickLogin();
     * 
     * Tests can use this single method:
     *   loginPage.loginAs("user", "pass");
     * 
     * This is called a "convenience method" or "wrapper method".
     * 
     * @param username The username to enter
     * @param password The password to enter
     */
    public void loginAs(String username, String password) {
        enterUsername(username);  // Type username
        enterPassword(password);  // Type password
        clickLogin();             // Click login button
    }

    /**
     * Gets the error message text displayed after a failed login.
     * The error container only appears when login validation fails.
     * 
     * @return The error message text (e.g., "Epic sadface: Username...")
     */
    public String getErrorMessage() {
        // getText() is inherited from BasePage - waits for visibility first
        return getText(errorMessageContainer);
    }

    /**
     * Checks if the error message is currently displayed on the page.
     * Useful for verifying that validation works:
     *   Assertions.assertTrue(loginPage.isErrorMessageDisplayed());
     * 
     * @return true if error message is visible, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessageContainer);
    }
}
