// ============================================================
// Package: com.demo.automation.tests
// ============================================================
package com.demo.automation.tests;

import com.demo.automation.base.BaseTest;
import com.demo.automation.pages.CartPage;
import com.demo.automation.pages.InventoryPage;
import com.demo.automation.pages.LoginPage;
import com.demo.automation.utils.ConfigReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ===================================================================
 * CartTest - Test Cases for Shopping Cart Functionality
 * ===================================================================
 * 
 * WHAT DOES THIS CLASS TEST?
 * It tests the shopping cart page:
 * - Verifying items are displayed in the cart
 * - Removing items from the cart
 * - Navigating back to the inventory page
 * 
 * KEY CONCEPT: Test Setup with Dependencies
 * --------------------------------------------------------
 * These tests have PREREQUISITES:
 * 1. User must be logged in
 * 2. Items must be added to cart
 * 3. User must be on the cart page
 * 
 * Instead of repeating this setup in every test, we put it in
 * @BeforeEach. This is called "test fixture" or "test context".
 * 
 * TRADE-OFF:
 * - Pro: Tests are shorter and focused
 * - Con: If setup is complex, tests can be harder to understand
 * 
 * Alternative: Each test could do its own setup explicitly
 * (more verbose but more transparent).
 * ===================================================================
 */
public class CartTest extends BaseTest {

    // Page objects used across multiple test methods
    private CartPage cartPage;

    /**
     * Setup method that runs before each cart test.
     * 
     * Execution order:
     * 1. BaseTest.setUp() → Opens browser, goes to saucedemo.com
     * 2. CartTest.setupCart() → Logs in, adds items, goes to cart
     * 3. The @Test method runs
     * 4. BaseTest.tearDown() → Closes browser
     */
    @BeforeEach
    public void setupCart() {
        // ============================================================
        // Step 1: Login with valid credentials
        // ============================================================
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(
            ConfigReader.getUsername(),  // "standard_user"
            ConfigReader.getPassword()    // "secret_sauce"
        );

        // ============================================================
        // Step 2: Add two items to the cart from the inventory page
        // ============================================================
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addItemToCart(0);  // Add the first product
        inventoryPage.addItemToCart(1);  // Add the second product

        // ============================================================
        // Step 3: Navigate to the cart page by clicking the cart icon
        // ============================================================
        // clickCartLink() returns a CartPage object
        cartPage = inventoryPage.clickCartLink();

        // Now cartPage is ready for use in all test methods
    }

    /**
     * ================================================================
     * Test Case 1: Verify Cart Displays Added Items
     * ================================================================
     * 
     * SCENARIO:
     *   Given I have added 2 items to the cart
     *   When I navigate to the cart page
     *   Then I should see both items in the cart
     * 
     * PURPOSE:
     *   Verify the cart page correctly displays the items that were added.
     * ================================================================
     */
    @Test
    @DisplayName("Verify cart page displays added items")
    public void testCartDisplaysItems() {

        // ASSERT 1: Verify we are on the cart page
        Assertions.assertTrue(cartPage.isOnCartPage(),
            "Should be on the cart page");

        // ASSERT 2: Verify the cart shows the correct number of items
        Assertions.assertEquals(2, cartPage.getCartItemCount(),
            "Cart should display 2 items (we added 2 in setup)");
    }

    /**
     * ================================================================
     * Test Case 2: Remove Item from Cart
     * ================================================================
     * 
     * SCENARIO:
     *   Given I have 2 items in the cart
     *   When I remove one item
     *   Then the cart should show 1 item remaining
     * 
     * PURPOSE:
     *   Verify the remove functionality works correctly.
     * ================================================================
     */
    @Test
    @DisplayName("Verify user can remove an item from the cart")
    public void testRemoveItemFromCart() {

        // ACT: Remove the first item (index 0) from the cart
        cartPage.removeItem(0);

        // ASSERT: The cart should now have only 1 item
        Assertions.assertEquals(1, cartPage.getCartItemCount(),
            "Cart should show 1 item after removing one of the two items");
    }

    /**
     * ================================================================
     * Test Case 3: Continue Shopping
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am on the cart page with items
     *   When I click "Continue Shopping"
     *   Then I should be returned to the inventory page
     * 
     * PURPOSE:
     *   Verify the "Continue Shopping" button navigates back correctly.
     * ================================================================
     */
    @Test
    @DisplayName("Verify user can continue shopping from cart")
    public void testContinueShopping() {

        // ACT: Click the "Continue Shopping" button
        // This returns an InventoryPage object for the next page
        InventoryPage inventoryPage = cartPage.clickContinueShopping();

        // ASSERT: Verify we are back on the inventory page
        Assertions.assertTrue(inventoryPage.isOnInventoryPage(),
            "Should return to inventory page after clicking 'Continue Shopping'");
    }
}
