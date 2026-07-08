// ============================================================
// Package: com.demo.automation.tests
// ============================================================
package com.demo.automation.tests;

import com.demo.automation.base.BaseTest;
import com.demo.automation.pages.InventoryPage;
import com.demo.automation.pages.LoginPage;
import com.demo.automation.utils.ConfigReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ===================================================================
 * InventoryTest - Test Cases for the Products/Inventory Page
 * ===================================================================
 * 
 * WHAT DOES THIS CLASS TEST?
 * It tests the inventory page functionality:
 * - Adding items to the shopping cart
 * - Verifying cart badge count updates
 * - Page content verification
 * 
 * KEY CONCEPT: @BeforeEach in Child Classes
 * --------------------------------------------------------
 * When a test class extends BaseTest, the execution flow is:
 * 
 * 1. BaseTest.setUp()         → Opens browser, navigates to URL
 * 2. InventoryTest.setup()    → Logs in, navigates to inventory
 * 3. @Test testMethod()       → Runs the actual test
 * 4. BaseTest.tearDown()      → Closes browser
 * 
 * Both @BeforeEach methods run! The parent's runs first,
 * then the child's. This is called "method overriding with super".
 * ===================================================================
 */
public class InventoryTest extends BaseTest {

    // Page object reference - initialized in setup() and used by all tests
    private InventoryPage inventoryPage;

    /**
     * ================================================================
     * setup() - Runs Before Each Test in THIS Class
     * ================================================================
     * 
     * This method logs in and navigates to the inventory page.
     * 
     * IMPORTANT: BaseTest.setUp() runs FIRST (opens browser, goes to URL).
     * This method runs SECOND (performs login).
     * 
     * By putting login in @BeforeEach, each test starts already logged in,
     * saving us from repeating login code in every test method.
     * ================================================================
     */
    @BeforeEach
    public void loginAndNavigateToInventory() {
        // Step 1: Login using valid credentials
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(
            ConfigReader.getUsername(),  // "standard_user"
            ConfigReader.getPassword()    // "secret_sauce"
        );

        // Step 2: Store the InventoryPage reference for test methods
        inventoryPage = new InventoryPage(driver);
    }

    /**
     * ================================================================
     * Test Case 1: Add a Single Item to Cart
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am logged in and on the inventory page
     *   When I add one product to the cart
     *   Then the cart badge should show "1"
     * 
     * PURPOSE:
     *   Verify the add-to-cart functionality works correctly.
     * ================================================================
     */
    @Test
    @DisplayName("Verify user can add an item to the cart")
    public void testAddItemToCart() {

        // ACT: Click the "Add to Cart" button for the first product (index 0)
        inventoryPage.addItemToCart(0);

        // ASSERT: The cart badge should display "1"
        int cartCount = inventoryPage.getCartBadgeCount();
        Assertions.assertEquals(1, cartCount,
            "Cart badge should show 1 after adding one item");
    }

    /**
     * ================================================================
     * Test Case 2: Add Multiple Items to Cart
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am logged in and on the inventory page
     *   When I add three different products to the cart
     *   Then the cart badge should show "3"
     * 
     * PURPOSE:
     *   Verify the cart badge correctly counts multiple items.
     * ================================================================
     */
    @Test
    @DisplayName("Verify user can add multiple items to the cart")
    public void testAddMultipleItemsToCart() {

        // ACT: Add the first product and verify it was added
        inventoryPage.addItemToCart(0);
        Assertions.assertEquals(1, inventoryPage.getCartBadgeCount(),
            "Cart badge should show 1 after adding first item");

        // Add the second product and verify the count increased
        inventoryPage.addItemToCart(1);
        Assertions.assertEquals(2, inventoryPage.getCartBadgeCount(),
            "Cart badge should show 2 after adding second item");

        // Add the third product and verify the count increased
        inventoryPage.addItemToCart(2);
        Assertions.assertEquals(3, inventoryPage.getCartBadgeCount(),
            "Cart badge should show 3 after adding third item");
    }

    /**
     * ================================================================
     * Test Case 3: Verify Inventory Page Content
     * ================================================================
     * 
     * SCENARIO:
     *   Given I am logged in and on the inventory page
     *   Then the page should display "Products" as the title
     *   And the page should show at least one product
     * 
     * PURPOSE:
     *   Verify the inventory page loads correctly with products.
     *   This is a "smoke test" - a quick check that the page works.
     * ================================================================
     */
    @Test
    @DisplayName("Verify inventory page displays products correctly")
    public void testInventoryPageDisplaysProducts() {

        // ASSERT 1: Verify we are on the inventory page
        Assertions.assertTrue(inventoryPage.isOnInventoryPage(),
            "Should be on the inventory page after login");

        // ASSERT 2: Verify products are displayed (at least one)
        Assertions.assertTrue(inventoryPage.getProductCount() > 0,
            "Inventory page should display at least one product");

        // ASSERT 3: Verify the first product has a non-null name
        Assertions.assertNotNull(inventoryPage.getProductName(0),
            "The first product should have a name");
    }
}
