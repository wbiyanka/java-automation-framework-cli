// ============================================================
// Package: com.demo.automation.tests
// ============================================================
package com.demo.automation.tests;

import com.demo.automation.base.BaseTest;
import com.demo.automation.pages.CartPage;
import com.demo.automation.pages.CheckoutPage;
import com.demo.automation.pages.InventoryPage;
import com.demo.automation.pages.LoginPage;
import com.demo.automation.utils.ConfigReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ===================================================================
 * CheckoutTest - Test Cases for the Checkout Flow
 * ===================================================================
 * 
 * WHAT DOES THIS CLASS TEST?
 * It tests the complete checkout process on SauceDemo:
 * - Filling out the checkout information form (Step 1)
 * - Reviewing the order on the overview page (Step 2)
 * - Completing the order and seeing the confirmation (Step 3)
 * 
 * This is called an "END-TO-END" or "E2E" test - it tests a complete
 * user journey from login through purchase confirmation.
 * 
 * THE CHECKOUT FLOW:
 *   Login → Add Item → Go to Cart → Click Checkout →
 *   Fill Form → Continue → Review → Finish → See Confirmation
 * ===================================================================
 */
public class CheckoutTest extends BaseTest {

    // Page objects used across test methods
    private CheckoutPage checkoutPage;

    /**
     * Setup method that runs before each checkout test.
     * 
     * This puts the test at the checkout information page (Step 1),
     * ready to fill in the form and complete the purchase.
     * 
     * Steps:
     * 1. Login with valid credentials
     * 2. Add a product to the cart
     * 3. Navigate to the cart
     * 4. Click the Checkout button
     */
    @BeforeEach
    public void setupCheckout() {

        // Step 1: Login to the application
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(
            ConfigReader.getUsername(),
            ConfigReader.getPassword()
        );

        // Step 2: Add the first product to the shopping cart
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addItemToCart(0);

        // Step 3: Navigate to the cart page
        CartPage cartPage = inventoryPage.clickCartLink();

        // Step 4: Click the "Checkout" button
        // clickCheckout() returns a CheckoutPage object for Step 1
        checkoutPage = cartPage.clickCheckout();

        // checkoutPage is now ready - test methods can fill the form
    }

    /**
     * ================================================================
     * Test Case 1: Complete Checkout Flow (Happy Path)
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am on the checkout information page
     *   When I fill in valid customer details
     *   And I click Continue
     *   And I click Finish on the overview page
     *   Then I should see the order confirmation message
     * 
     * This tests the ENTIRE purchase flow from form to confirmation.
     * It's a positive "happy path" test.
     * ================================================================
     */
    @Test
    @DisplayName("Verify user can complete the full checkout process")
    public void testCompleteCheckout() {

        // ============================================================
        // STEP 1: Fill Checkout Information
        // ============================================================
        // Fill out the form with customer details
        checkoutPage.fillCheckoutInfo("John", "Doe", "12345");
        
        // Click Continue to proceed to the overview page (Step 2)
        checkoutPage.clickContinue();

        // ============================================================
        // STEP 2: Order Overview / Review
        // ============================================================
        // Verify we are on the overview page by checking for the total
        // The total label contains text like "Total: $32.39"
        Assertions.assertTrue(checkoutPage.getTotalText().contains("Total"),
            "Overview page should display a total amount before finishing");

        // Click Finish to complete the order (navigate to Step 3)
        checkoutPage.clickFinish();

        // ============================================================
        // STEP 3: Order Confirmation
        // ============================================================
        // Verify the order was completed successfully
        Assertions.assertTrue(checkoutPage.isOrderComplete(),
            "Order should be completed successfully");

        // Verify the success message text is exactly what we expect
        Assertions.assertEquals(
            "Thank you for your order!",
            checkoutPage.getSuccessMessage(),
            "Success message should confirm the order"
        );
    }

    /**
     * ================================================================
     * Test Case 2: Checkout with Empty First Name
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am on the checkout information page
     *   When I fill in the form WITHOUT a first name
     *   And I click Continue
     *   Then I should remain on the checkout page (validation error)
     * 
     * PURPOSE:
     *   Verify that form validation prevents proceeding without
     *   required fields. This is a "negative" test case.
     * ================================================================
     */
    @Test
    @DisplayName("Verify validation when first name is empty during checkout")
    public void testCheckoutWithoutFirstName() {

        // ACT: Fill form with empty first name
        // Empty string "" represents a blank field
        checkoutPage.fillCheckoutInfo("", "Doe", "12345");
        checkoutPage.clickContinue();

        // ASSERT: Should remain on the checkout information page
        // (not advance to the overview page)
        // We check the current URL to confirm we didn't move forward
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(
            currentUrl.contains("checkout-step-one"),
            "Should remain on checkout Step 1 when first name is empty"
        );

        // Note: SauceDemo doesn't show inline error messages for
        // missing fields, but the page doesn't advance either.
        // In real projects, you'd also check for a visible error message.
    }

    /**
     * ================================================================
     * Test Case 3: Complete Checkout with Different Customer Data
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am on the checkout information page
     *   When I fill in different customer details
     *   And I proceed through the entire checkout
     *   Then I should see the order confirmation
     * 
     * PURPOSE:
     *   Verify the checkout works with different customer data.
     *   This shows the test is not hard-coded to one set of values.
     * ================================================================
     */
    @Test
    @DisplayName("Verify checkout with different customer information")
    public void testCheckoutWithDifferentCustomer() {

        // ACT: Use different customer data this time
        checkoutPage.fillCheckoutInfo("Jane", "Smith", "67890");
        checkoutPage.clickContinue();

        // Verify overview page loaded
        Assertions.assertTrue(
            checkoutPage.getTotalText().contains("Total"),
            "Should see order total on the overview page"
        );

        // Complete the order
        checkoutPage.clickFinish();

        // ASSERT: Verify order success
        Assertions.assertTrue(checkoutPage.isOrderComplete(),
            "Order should complete successfully for different customer");
        Assertions.assertEquals(
            "Thank you for your order!",
            checkoutPage.getSuccessMessage(),
            "Success message should match expected text"
        );
    }
}
