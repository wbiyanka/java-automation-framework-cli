// ============================================================
// Package: com.demo.automation.pages
// ============================================================
package com.demo.automation.pages;

import com.demo.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * ===================================================================
 * InventoryPage - Page Object for the Products/Inventory Page
 * ===================================================================
 * 
 * This page appears AFTER successful login. It shows a grid of
 * products that users can browse and add to their shopping cart.
 * 
 * URL: https://www.saucedemo.com/inventory.html
 * 
 * KEY CONCEPT: List<WebElement>
 * When multiple elements share the same locator (e.g., multiple
 * "Add to Cart" buttons), we use List<WebElement> to store them.
 * Each item in the list corresponds to one matching element on the page.
 * 
 * EXAMPLE:
 * @FindBy(className = "inventory_item_name")
 * List<WebElement> productNames;  // All product name elements
 * 
 * productNames.get(0) -> first product name
 * productNames.get(1) -> second product name
 * ===================================================================
 */
public class InventoryPage extends BasePage {

    // ================================================================
    // ELEMENT LOCATORS
    // ================================================================

    // Page title element - shows "Products" at the top of the page
    @FindBy(className = "title")
    private WebElement pageTitle;

    // NOTE on "Add to Cart" button approach:
    // We use driver.findElements() in addItemToCart() (not @FindBy List)
    // to get a fresh list from the current DOM on every call. This
    // handles dynamic DOM changes reliably because after clicking a
    // button, the CSS class "btn_inventory" stays on it, but we always
    // re-query for the latest list of buttons.

    // Shopping cart badge - shows the number of items in cart
    // This element only appears when the cart has at least one item
    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    // Shopping cart icon/link (top right corner of the page)
    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    // ALL product name elements on the page
    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    /**
     * Constructor - calls BasePage constructor which initializes all @FindBy elements
     * 
     * @param driver The WebDriver instance
     */
    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    // ================================================================
    // PAGE ACTION METHODS
    // ================================================================

    /**
     * Verifies we are on the inventory page by checking the page title.
     * This is a "verification" or "assertion helper" method.
     * 
     * @return true if "Products" title is visible, false otherwise
     */
    public boolean isOnInventoryPage() {
        // Check that the title element is displayed AND has the expected text
        return isDisplayed(pageTitle) && getText(pageTitle).equals("Products");
    }

    /**
     * Returns the page title text for verification.
     * 
     * @return The text of the page title (should be "Products")
     */
    public String getPageTitle() {
        return getText(pageTitle);
    }

    /**
     * ================================================================
     * addItemToCart(int itemIndex)
     * ================================================================
     * Adds a specific product to the shopping cart by index.
     * 
     * Index is 0-based, meaning:
     *   itemIndex 0 = first product on the page
     *   itemIndex 1 = second product on the page
     *   itemIndex 2 = third product, and so on
     * 
     * @param itemIndex The position of the product on the page (0 = first)
     * 
     * @throws IndexOutOfBoundsException if the index is out of range
     * ================================================================
     */
    public void addItemToCart(int itemIndex) {
        // Find ALL buttons with the btn_inventory class.
        // After clicking, the button text changes from "Add to cart"
        // to "Remove" but the btn_inventory class stays.
        // We use driver.findElements() directly (not @FindBy List)
        // to get a fresh list from the current DOM every time.
        List<WebElement> buttons = driver.findElements(
            By.cssSelector("button.btn_inventory")
        );
        
        // Validate that the requested item index exists in the list
        if (itemIndex >= 0 && itemIndex < buttons.size()) {
            // Click the "Add to Cart" button at the specified index.
            // The click() method (inherited from BasePage) uses JavaScript
            // executor internally, which is more reliable than Selenium's
            // native click() for dynamic SPA buttons.
            click(buttons.get(itemIndex));
        } else {
            // Throw an error with a helpful message if index is invalid
            throw new IndexOutOfBoundsException(
                "Item index " + itemIndex + " is out of bounds. " +
                "Only " + buttons.size() + " items available."
            );
        }
    }

    /**
     * ================================================================
     * getCartBadgeCount()
     * ================================================================
     * Reads the number displayed on the shopping cart badge.
     * 
     * The badge is a small red circle on the cart icon that shows
     * the number of items in the cart. It only appears when count > 0.
     * 
     * If the badge is not visible (empty cart), this returns 0.
     * 
     * @return The number of items in the cart
     * ================================================================
     */
    public int getCartBadgeCount() {
        try {
            // Convert the badge text to an integer
            // Badge text might be "1", "2", "3", etc.
            return Integer.parseInt(getText(cartBadge));
        } catch (Exception e) {
            // If the badge is not visible, the cart is empty
            return 0;
        }
    }

    /**
     * Clicks on the shopping cart link (top right) to navigate to the cart page.
     * 
     * @return A new CartPage object (since we navigate to a different page)
     */
    public CartPage clickCartLink() {
        click(cartLink);
        
        // Return a new page object for the cart page
        // The test will use this object to interact with the cart
        return new CartPage(driver);
    }

    /**
     * Gets the name of a product at the specified index.
     * 
     * @param index The product position (0-based)
     * @return The product name text (e.g., "Sauce Labs Backpack")
     */
    public String getProductName(int index) {
        return getText(productNames.get(index));
    }

    /**
     * Gets the total number of products displayed on the inventory page.
     * 
     * @return The count of visible products
     */
    public int getProductCount() {
        return productNames.size();
    }
}
