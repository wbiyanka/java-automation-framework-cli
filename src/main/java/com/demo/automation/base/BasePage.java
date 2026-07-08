// ============================================================
// Package: com.demo.automation.base
// This package contains base/abstract classes that other classes inherit from.
// ============================================================
package com.demo.automation.base;

import com.demo.automation.utils.ConfigReader;
import com.demo.automation.utils.WaitHelper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

/**
 * ===================================================================
 * BasePage - Parent of All Page Object Classes
 * ===================================================================
 * 
 * WHAT IS THE PAGE OBJECT MODEL (POM)?
 * --------------------------------
 * POM is a design pattern where each web page has a corresponding class.
 * 
 * Instead of writing:  driver.findElement(By.id("login")).click()
 * in your test code, you write:  loginPage.clickLoginButton()
 * 
 * BENEFITS OF POM:
 * 1. Readability - Test code reads like a story
 * 2. Reusability - Page methods can be used by many tests
 * 3. Maintainability - If a locator changes, update ONE class, not ALL tests
 * 4. Clean separation - Tests focus on LOGIC, page classes focus on LOCATORS
 * 
 * WHAT DOES THIS BASE CLASS DO?
 * It provides common methods that ALL page objects use:
 * - click() with explicit wait
 * - sendKeys() with explicit wait
 * - getText() with explicit wait
 * - isDisplayed() for checking visibility
 * 
 * KEY CONCEPT: Inheritance
 * All page classes (LoginPage, InventoryPage, etc.) EXTEND this class.
 * This means they inherit all these utility methods automatically.
 * ===================================================================
 */
public class BasePage {

    // ================================================================
    // PROTECTED FIELDS
    // "protected" means child classes (pages) can access these directly
    // ================================================================
    
    // The WebDriver instance - shared across all pages
    protected WebDriver driver;
    
    // Helper for explicit waits - ensures reliable element interaction
    protected WaitHelper waitHelper;
    
    // Timeout value from config.properties (read once during construction)
    protected int explicitWait;

    /**
     * ================================================================
     * Constructor - Initializes the page object
     * ================================================================
     * 
     * WHAT IS PageFactory.initElements()?
     * In our page classes, we use @FindBy annotations like:
     *   @FindBy(id = "login-button")
     *   private WebElement loginButton;
     * 
     * PageFactory.initElements() "magically" connects these annotations
     * to actual WebElement references using the driver.
     * 
     * WITHOUT PageFactory, we'd have to write:
     *   loginButton = driver.findElement(By.id("login-button"));
     * 
     * @param driver The WebDriver instance from the test
     */
    public BasePage(WebDriver driver) {
        // Store the driver reference
        this.driver = driver;
        
        // Create a WaitHelper instance for explicit waits
        this.waitHelper = new WaitHelper(driver);
        
        // Read the timeout from config (converted to int by ConfigReader)
        this.explicitWait = ConfigReader.getExplicitWait();
        
        // Initialize all @FindBy annotated elements in this page class
        // "this" refers to the current page object
        PageFactory.initElements(driver, this);
    }

    /**
     * ================================================================
     * click(WebElement element) - Click with Wait
     * ================================================================
     * 
     * WHY WRAP A SIMPLE CLICK?
     * A raw element.click() fails if the element isn't ready.
     * This method waits for the element to be clickable first,
     * making tests much more reliable.
     * 
     * @param element The WebElement to click
     * ================================================================
     */
    protected void click(WebElement element) {
        // Step 1: Wait until the element is both visible and enabled
        waitHelper.waitForElementClickable(element, explicitWait);
        
        // Step 2: Perform the click action using JavaScript executor.
        // JavaScript click is used here because it's more reliable than
        // Selenium's native click() - it bypasses overlapping element
        // issues and ChromeDriver compatibility quirks.
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * ================================================================
     * sendKeys(WebElement element, String text) - Type Text with Wait
     * ================================================================
     * 
     * Types text into an input field.
     * Clears existing text first to ensure clean input.
     * 
     * WHY CLEAR FIRST?
     * Some input fields have default/pre-filled text. If we just
     * sendKeys, the new text APPENDS to the existing text.
     * Clearing first ensures only our intended text is present.
     * 
     * @param element The input field element
     * @param text The text to type into the field
     * ================================================================
     */
    protected void sendKeys(WebElement element, String text) {
        // Step 1: Wait for the element to be visible
        waitHelper.waitForElementVisible(element, explicitWait);
        
        // Step 2: Clear any existing text in the field
        element.clear();
        
        // Step 3: Type the new text character by character
        element.sendKeys(text);
    }

    /**
     * ================================================================
     * getText(WebElement element) - Get Visible Text
     * ================================================================
     * 
     * Returns the visible text content of an element.
     * Waits for the element to be visible before reading text.
     * 
     * @param element The element to extract text from
     * @return The visible text content (empty string if no text)
     * ================================================================
     */
    protected String getText(WebElement element) {
        // Wait for the element to be visible first
        waitHelper.waitForElementVisible(element, explicitWait);
        
        // Return the visible text (not hidden text)
        return element.getText();
    }

    /**
     * ================================================================
     * isDisplayed(WebElement element) - Check Element Visibility
     * ================================================================
     * 
     * Checks if an element is visible on the page.
     * Returns true/false instead of throwing an error.
     * 
     * WHY TRY-CATCH?
     * Normally, Selenium throws an exception if an element isn't found.
     * The try-catch catches that exception and returns false instead.
     * This is useful for "verifying absence" - checking that an
     * error message does NOT appear, for example.
     * 
     * @param element The element to check
     * @return true if the element is visible, false otherwise
     * ================================================================
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            // isDisplayed() returns true if element is visible
            return element.isDisplayed();
        } catch (Exception e) {
            // Element not found or not visible - returns false instead of crashing
            return false;
        }
    }
}
