package com.example.getfood.models;

public class CartItem {

    private Integer cartItemQuantity;
    private FoodItem foodItem;

    public CartItem(FoodItem foodItem, Integer cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
        this.foodItem = foodItem;
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
