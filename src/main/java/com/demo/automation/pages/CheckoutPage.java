// ============================================================
// Package: com.demo.automation.pages
// ============================================================
package com.demo.automation.pages;

import com.demo.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * ===================================================================
 * CheckoutPage - Page Object for the Complete Checkout Flow
 * ===================================================================
 * 
 * The checkout process on SauceDemo has THREE steps:
 * 
 * STEP 1 - Checkout Information (checkout-step-one.html)
 *   User fills in personal details: First Name, Last Name, ZIP Code
 * 
 * STEP 2 - Checkout Overview (checkout-step-two.html)
 *   User reviews their order summary (items, prices, total)
 *   User clicks "Finish" to complete the purchase
 * 
 * STEP 3 - Checkout Complete (checkout-complete.html)
 *   Order confirmation page showing success message
 * 
 * This single page object handles ALL THREE steps because they
 * represent one continuous user flow. In larger applications,
 * each step might have its own page class.
 * ===================================================================
 */
public class CheckoutPage extends BasePage {

    // ================================================================
    // STEP 1 LOCATORS - Checkout Information Form
    // Note: Form input fields use @FindBy with ID locators.
    // ================================================================

    // Continue button - proceeds from Step 1 to Step 2 (Overview)
    @FindBy(id = "continue")
    private WebElement continueButton;

    // ================================================================
    // STEP 2 LOCATORS - Checkout Overview
    // ================================================================

    // Total price label showing the complete order total
    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    // Finish button - completes the order (Step 2 -> Step 3)
    @FindBy(id = "finish")
    private WebElement finishButton;

    // ================================================================
    // STEP 3 LOCATORS - Checkout Complete
    // ================================================================

    // Success/confirmation header text (e.g., "Thank you for your order!")
    @FindBy(className = "complete-header")
    private WebElement successMessage;

    // Button to return to the products page after order completion
    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    /**
     * Constructor - calls BasePage constructor
     * 
     * @param driver The WebDriver instance
     */
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // ================================================================
    // STEP 1 ACTIONS - Checkout Information Form
    // ================================================================

    /**
     * ================================================================
     * fillCheckoutInfo() - Convenience Method
     * ================================================================
     * Fills out the entire checkout form in one method call.
     * 
     * Instead of calling three separate methods:
     *   checkoutPage.enterFirstName("John");
     *   checkoutPage.enterLastName("Doe");
     *   checkoutPage.enterZipCode("12345");
     * 
     * Tests can use one call:
     *   checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
     * 
     * @param firstName Customer's first name
     * @param lastName Customer's last name
     * @param zipCode Customer's ZIP code
     * ================================================================
     */
    public void fillCheckoutInfo(String firstName, String lastName, String zipCode) {
        // For React controlled inputs, we need to use the native value
        // setter and dispatch an input event so React picks up the change.
        // Simply setting element.value via JS or using sendKeys won't
        // update React's internal state properly.
        //
        // The approach below:
        // 1. Gets the native value property setter (bypassing React's proxy)
        // 2. Calls the native setter with our value
        // 3. Dispatches an 'input' event so React detects the change
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        String script =
            "var setter = Object.getOwnPropertyDescriptor(" +
            "  window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input', {bubbles: true}));";
        
        js.executeScript(script, driver.findElement(By.id("first-name")), firstName);
        js.executeScript(script, driver.findElement(By.id("last-name")), lastName);
        js.executeScript(script, driver.findElement(By.id("postal-code")), zipCode);
    }

    /**
     * Clicks the Continue button to proceed from Step 1 (information)
     * to Step 2 (overview/review).
     */
    public void clickContinue() {
        click(continueButton);
    }

    // ================================================================
    // STEP 2 ACTIONS - Checkout Overview
    // ================================================================

    /**
     * Gets the total amount displayed on the overview page.
     * The text typically looks like: "Total: $43.18"
     * 
     * @return The total price text as a string
     */
    public String getTotalText() {
        return getText(totalLabel);
    }

    /**
     * Clicks the Finish button to complete the purchase.
     * This transitions from Step 2 (overview) to Step 3 (confirmation).
     */
    public void clickFinish() {
        click(finishButton);
    }

    // ================================================================
    // STEP 3 ACTIONS - Checkout Complete
    // ================================================================

    /**
     * Gets the order confirmation/success message.
     * Expected text: "Thank you for your order!"
     * 
     * @return The success message text
     */
    public String getSuccessMessage() {
        return getText(successMessage);
    }

    /**
     * Checks if the order was successfully completed.
     * The success message element is only visible on Step 3.
     * 
     * @return true if the success message is displayed
     */
    public boolean isOrderComplete() {
        return isDisplayed(successMessage);
    }

    /**
     * Clicks the "Back Home" button to return to the products page.
     * This is available on Step 3 after order completion.
     * 
     * @return A new InventoryPage object
     */
    public InventoryPage clickBackToProducts() {
        click(backToProductsButton);
        return new InventoryPage(driver);
    }
}
