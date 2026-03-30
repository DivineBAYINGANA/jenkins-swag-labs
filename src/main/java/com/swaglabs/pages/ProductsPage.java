package com.swaglabs.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Page Object for the Swag Labs Products (inventory) page.
 */
public class ProductsPage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final SelenideElement pageTitle        = $("[data-test='title']");
    private final SelenideElement cartLink         = $(".shopping_cart_link");
    private final SelenideElement cartBadge        = $(".shopping_cart_badge");
    private final SelenideElement sortDropdown     = $("[data-test='product_sort_container']");
    private final SelenideElement burgerMenuBtn    = $("#react-burger-menu-btn");
    private final SelenideElement logoutLink       = $("#logout_sidebar_link");
    private final ElementsCollection inventoryItems = $$(".inventory_item");
    private final ElementsCollection itemNames      = $$(".inventory_item_name");
    private final ElementsCollection itemPrices     = $$(".inventory_item_price");
    private final ElementsCollection addToCartBtns  = $$("[data-test^='add-to-cart']");
    private final ElementsCollection removeBtns     = $$("[data-test^='remove']");

    // ── Navigation ────────────────────────────────────────────────────────────

    @Step("Navigate to the cart")
    public CartPage goToCart() {
        cartLink.shouldBe(visible).click();
        return new CartPage();
    }

    @Step("Open burger menu and logout")
    public LoginPage logout() {
        burgerMenuBtn.shouldBe(visible).click();
        logoutLink.shouldBe(visible).click();
        return new LoginPage();
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    @Step("Add product '{productName}' to cart")
    public ProductsPage addToCartByName(String productName) {
        // Build the data-test attribute from the product name
        String dataTest = "add-to-cart-" + productName.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        $("[data-test='" + dataTest + "']").shouldBe(visible).click();
        return this;
    }

    @Step("Add first product to cart")
    public ProductsPage addFirstProductToCart() {
        addToCartBtns.shouldHave(sizeGreaterThan(0));
        addToCartBtns.first().click();
        return this;
    }

    @Step("Add product at index {index} to cart (0-based)")
    public ProductsPage addProductToCartByIndex(int index) {
        addToCartBtns.get(index).shouldBe(visible).click();
        return this;
    }

    @Step("Remove first item from cart via products page")
    public ProductsPage removeFirstItemFromCart() {
        removeBtns.shouldHave(sizeGreaterThan(0));
        removeBtns.first().click();
        return this;
    }

    @Step("Select sort option: {option}")
    public ProductsPage selectSortOption(String option) {
        sortDropdown.shouldBe(visible).selectOptionByValue(option);
        return this;
    }

    @Step("Open product detail for '{productName}'")
    public ProductDetailPage openProductDetail(String productName) {
        itemNames.findBy(exactText(productName)).shouldBe(visible).click();
        return new ProductDetailPage();
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    @Step("Verify Products page is loaded")
    public ProductsPage verifyPageLoaded() {
        pageTitle.shouldBe(visible).shouldHave(exactText("Products"));
        inventoryItems.shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Verify product count is {expectedCount}")
    public ProductsPage verifyProductCount(int expectedCount) {
        inventoryItems.shouldHave(com.codeborne.selenide.CollectionCondition.size(expectedCount));
        return this;
    }

    @Step("Verify cart badge shows {expectedCount}")
    public ProductsPage verifyCartBadge(int expectedCount) {
        cartBadge.shouldBe(visible).shouldHave(exactText(String.valueOf(expectedCount)));
        return this;
    }

    @Step("Verify cart badge is not visible (cart is empty)")
    public ProductsPage verifyCartIsEmpty() {
        cartBadge.shouldNotBe(visible);
        return this;
    }

    @Step("Verify first product name is '{expectedName}'")
    public ProductsPage verifyFirstProductName(String expectedName) {
        itemNames.first().shouldHave(exactText(expectedName));
        return this;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public ElementsCollection getInventoryItems() { return inventoryItems; }
    public ElementsCollection getItemNames()       { return itemNames;       }
    public ElementsCollection getItemPrices()      { return itemPrices;      }
    public SelenideElement    getCartBadge()        { return cartBadge;       }
    public String             getPageTitle()        { return pageTitle.getText(); }

    public int getCartCount() {
        if (cartBadge.isDisplayed()) {
            return Integer.parseInt(cartBadge.getText());
        }
        return 0;
    }

    public String getFirstItemName() {
        return itemNames.first().getText();
    }

    public double getFirstItemPrice() {
        String raw = itemPrices.first().getText().replace("$", "");
        return Double.parseDouble(raw);
    }
}