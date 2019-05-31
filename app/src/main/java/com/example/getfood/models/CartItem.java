package com.example.getfood.models;

import java.io.Serializable;

public class CartItem implements Serializable {

    private Integer cartItemQuantity;
    private String itemStatus;
    private FoodItem foodItem;

    public CartItem(FoodItem foodItem, String itemStatus, Integer cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
        this.itemStatus = itemStatus;
        this.foodItem = foodItem;
    }

    public CartItem() {
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public String getCartItemName() {
        return foodItem.getItemName();
    }

    public Integer getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setCartItemQuantity(Integer cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }
}
