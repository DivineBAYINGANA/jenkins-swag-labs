package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

/**
 * Page Object for the Swag Labs Product Detail page.
 */
public class ProductDetailPage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final SelenideElement productName   = $(".inventory_details_name");
    private final SelenideElement productDesc   = $(".inventory_details_desc");
    private final SelenideElement productPrice  = $(".inventory_details_price");
    private final SelenideElement productImage  = $(".inventory_details_img");
    private final SelenideElement addToCartBtn  = $("[data-test^='add-to-cart']");
    private final SelenideElement removeBtn     = $("[data-test^='remove']");
    private final SelenideElement backButton    = $("#back-to-products");
    private final SelenideElement cartBadge     = $(".shopping_cart_badge");
    private final SelenideElement cartLink      = $(".shopping_cart_link");

    // ── Actions ───────────────────────────────────────────────────────────────

    @Step("Add product to cart from detail page")
    public ProductDetailPage addToCart() {
        addToCartBtn.shouldBe(visible).shouldBe(enabled).click();
        return this;
    }

    @Step("Remove product from cart via detail page")
    public ProductDetailPage removeFromCart() {
        removeBtn.shouldBe(visible).click();
        return this;
    }

    @Step("Go back to products list")
    public ProductsPage goBack() {
        backButton.shouldBe(visible).click();
        return new ProductsPage();
    }

    @Step("Navigate to cart from detail page")
    public CartPage goToCart() {
        cartLink.shouldBe(visible).click();
        return new CartPage();
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    @Step("Verify product detail page is loaded")
    public ProductDetailPage verifyPageLoaded() {
        productName.shouldBe(visible);
        productDesc.shouldBe(visible);
        productPrice.shouldBe(visible);
        productImage.shouldBe(visible);
        addToCartBtn.shouldBe(visible);
        return this;
    }

    @Step("Verify product name is '{expectedName}'")
    public ProductDetailPage verifyProductName(String expectedName) {
        productName.shouldHave(exactText(expectedName));
        return this;
    }

    @Step("Verify price starts with '$'")
    public ProductDetailPage verifyPriceFormat() {
        productPrice.shouldHave(matchText("^\\$[0-9]+(\\.[0-9]{2})?$"));
        return this;
    }

    @Step("Verify 'Add to cart' button is visible")
    public ProductDetailPage verifyAddToCartVisible() {
        addToCartBtn.shouldBe(visible);
        return this;
    }

    @Step("Verify 'Remove' button is visible after adding to cart")
    public ProductDetailPage verifyRemoveButtonVisible() {
        removeBtn.shouldBe(visible);
        return this;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getName()        { return productName.getText();  }
    public String getDescription() { return productDesc.getText();  }
    public String getPriceText()   { return productPrice.getText(); }

    public double getPrice() {
        return Double.parseDouble(productPrice.getText().replace("$", ""));
    }
}