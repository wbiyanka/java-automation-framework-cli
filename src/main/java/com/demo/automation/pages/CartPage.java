// ============================================================
// Package: com.demo.automation.pages
// ============================================================
package com.demo.automation.pages;

import com.demo.automation.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * ===================================================================
 * CartPage - Page Object for the Shopping Cart Page
 * ===================================================================
 * 
 * This page shows all items the user has added to their shopping cart.
 * Users can:
 * - Review items in their cart
 * - Remove items from the cart
 * - Proceed to checkout
 * - Continue shopping (go back to inventory)
 * 
 * URL: https://www.saucedemo.com/cart.html
 * 
 * KEY CONCEPT: Page Transitions
 * When a method navigates to a different page (like clickCheckout()),
 * it returns a new page object. This makes test code flow naturally:
 * 
 *   checkoutPage = cartPage.clickCheckout();
 *   // Now we can use checkoutPage methods directly
 * ===================================================================
 */
public class CartPage extends BasePage {

    // ================================================================
    // ELEMENT LOCATORS
    // ================================================================

    // Page title element - shows "Your Cart"
    @FindBy(className = "title")
    private WebElement pageTitle;

    // All cart item names - using List because cart can have multiple items
    @FindBy(className = "inventory_item_name")
    private List<WebElement> cartItemNames;

    // The "Checkout" button to proceed with the purchase
    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    // The "Continue Shopping" button to return to the products page
    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    // All "Remove" buttons in the cart (one per item)
    // CSS selector ".cart_button" matches all buttons with this class.
    // On SauceDemo cart page, Remove buttons have the class "cart_button".
    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;

    /**
     * Constructor - calls BasePage constructor
     * 
     * @param driver The WebDriver instance
     */
    public CartPage(WebDriver driver) {
        super(driver);
    }

    // ================================================================
    // PAGE ACTION METHODS
    // ================================================================

    /**
     * Checks if we are on the cart page by verifying the title.
     * 
     * @return true if "Your Cart" title is displayed
     */
    public boolean isOnCartPage() {
        return isDisplayed(pageTitle) && getText(pageTitle).equals("Your Cart");
    }

    /**
     * Gets the number of items currently in the cart.
     * Each item in the cart has its own row with a product name.
     * 
     * @return The count of cart items
     */
    public int getCartItemCount() {
        return cartItemNames.size();
    }

    /**
     * Gets the name of a specific cart item.
     * 
     * @param index The item position (0-based, 0 = first item)
     * @return The product name text
     */
    public String getCartItemName(int index) {
        return getText(cartItemNames.get(index));
    }

    /**
     * Clicks the "Checkout" button to proceed to the checkout process.
     * This navigates to the checkout information form (Step 1 of checkout).
     * 
     * @return A new CheckoutPage object for the checkout flow
     */
    public CheckoutPage clickCheckout() {
        click(checkoutButton);
        // Return a new CheckoutPage for the next step of the flow
        return new CheckoutPage(driver);
    }

    /**
     * Clicks the "Continue Shopping" button to go back to the inventory page.
     * 
     * @return A new InventoryPage object
     */
    public InventoryPage clickContinueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(driver);
    }

    /**
     * Removes an item from the cart by clicking its "Remove" button.
     * The button index matches the item index (0 = first item's remove button).
     * 
     * @param itemIndex The index of the item to remove (0-based)
     */
    public void removeItem(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < removeButtons.size()) {
            click(removeButtons.get(itemIndex));
        } else {
            throw new IndexOutOfBoundsException(
                "Item index " + itemIndex + " is out of bounds. " +
                "Only " + removeButtons.size() + " items in cart."
            );
        }
    }
}
