package com.example.getfood.Models;

public class CartItem {

    private String cartItemName, cartItemCategory;
    private Integer cartItemQuantity, cartItemPrice;

    public CartItem(String cartItemName, String cartItemCategory, Integer cartItemQuantity, Integer cartItemPrice) {
        this.cartItemName = cartItemName;
        this.cartItemCategory = cartItemCategory;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
    }

    public CartItem(String itemName){
        this.cartItemName = itemName;
    }

    public String getCartItemName() {
        return cartItemName;
    }

    public void setCartItemName(String cartItemName) {
        this.cartItemName = cartItemName;
    }

    public String getCartItemCategory() {
        return cartItemCategory;
    }

    public void setCartItemCategory(String cartItemCategory) {
        this.cartItemCategory = cartItemCategory;
    }

    public Integer getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setCartItemQuantity(Integer cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public Integer getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(Integer cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }
}
