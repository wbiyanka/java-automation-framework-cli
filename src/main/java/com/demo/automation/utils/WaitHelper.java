// ============================================================
// Package: com.demo.automation.utils
// ============================================================
package com.demo.automation.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * ===================================================================
 * WaitHelper - Explicit Wait Utility Methods
 * ===================================================================
 * 
 * WHAT ARE WAITS AND WHY DO WE NEED THEM?
 * Websites load content dynamically - images, buttons, and text
 * appear at different times. If Selenium tries to click a button
 * before it appears, the test will fail with an error.
 * 
 * TYPES OF WAITS IN SELENIUM:
 * 1. Implicit Wait - Global setting; waits up to N seconds for
 *    every element-finding operation. Simple but can slow tests.
 * 
 * 2. Explicit Wait - Waits for a SPECIFIC condition on a SPECIFIC
 *    element. More precise and efficient. THIS is what we use here.
 * 
 * 3. Thread.sleep() - ALWAYS pauses for the full duration, even
 *    if the element appears early. BAD PRACTICE - avoid this!
 * 
 * KEY CONCEPT: Explicit Waits are preferred because:
 * - They wait JUST LONG ENOUGH (not a fixed time)
 * - They check for SPECIFIC conditions (visibility, clickability)
 * - Tests run faster and are more reliable
 * ===================================================================
 */
public class WaitHelper {

    // Store the WebDriver reference for creating wait objects
    private WebDriver driver;

    /**
     * Constructor - receives the WebDriver instance from the page object
     * 
     * @param driver The WebDriver instance used by the test
     */
    public WaitHelper(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * ================================================================
     * waitForElementVisible(WebElement element, int timeoutSeconds)
     * ================================================================
     * Waits until the specified element is VISIBLE on the page.
     * 
     * "Visible" means:
     * - The element exists in the DOM (HTML)
     * - The element has a height and width greater than 0
     * - The element is not hidden via CSS (display:none, visibility:hidden)
     * 
     * USE CASE: Waiting for an element to appear after a page load,
     *          or after clicking a button that triggers dynamic content.
     * 
     * @param element The WebElement to wait for
     * @param timeoutSeconds Maximum time to wait (in seconds)
     * @return The visible WebElement (ready to interact with)
     * ================================================================
     */
    public WebElement waitForElementVisible(WebElement element, int timeoutSeconds) {
        // Create a WebDriverWait with the specified timeout
        // Duration.ofSeconds() converts seconds to Java's Duration format
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        
        // Wait until the element is visible, then return it
        // ExpectedConditions has many useful conditions:
        //   visibilityOf, elementToBeClickable, presenceOfElementLocated, etc.
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * ================================================================
     * waitForElementClickable(WebElement element, int timeoutSeconds)
     * ================================================================
     * Waits until the specified element is both VISIBLE and ENABLED.
     * 
     * "Clickable" means:
     * - The element is visible (see above)
     * - The element is enabled (not disabled via HTML attribute)
     *   Example of disabled: <button disabled>Submit</button>
     * 
     * USE CASE: Always wait for clickability before clicking!
     *          This prevents the most common Selenium errors.
     * 
     * @param element The WebElement to wait for
     * @param timeoutSeconds Maximum time to wait (in seconds)
     * @return The clickable WebElement
     * ================================================================
     */
    public WebElement waitForElementClickable(WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * ================================================================
     * hardWait(int milliseconds) - WHY YOU SHOULD AVOID THIS
     * ================================================================
     * Pauses execution for a fixed amount of time.
     * 
     * This method exists to show you what NOT to do.
     * 
     * PROBLEMS with Thread.sleep():
     * 1. Wastes time - pauses even if element appears early
     * 2. Flaky tests - if network is slower than usual, sleep may
     *    not be long enough
     * 3. Hard to maintain - changing network conditions require
     *    updating all sleep durations
     * 
     * Always use Explicit Waits instead!
     * 
     * @param milliseconds Time to sleep in milliseconds
     *                     (1000 ms = 1 second)
     * ================================================================
     */
    public void hardWait(int milliseconds) {
        try {
            // Thread.sleep() pauses the CURRENT thread of execution
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // Restore the interrupted status
            Thread.currentThread().interrupt();
        }
    }
}
