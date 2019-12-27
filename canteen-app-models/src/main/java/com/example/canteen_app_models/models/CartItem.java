package com.example.canteen_app_models.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class CartItem extends FoodItem implements Serializable {

    private Integer itemQuantity;
    private String itemStatus;
//    TODO: separate user item rating from normal item rating and store them separately

    public CartItem(FoodItem foodItem, String itemStatus, Integer cartItemQuantity) {
        super(foodItem.getItemName(), foodItem.getItemPrice(), -1, foodItem.getItemCategory());
        this.itemQuantity = cartItemQuantity;
        this.itemStatus = itemStatus;
    }

    public CartItem() {
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void increaseQuantity() {
        this.itemQuantity++;
    }

    public void decreaseQuantity() {
        this.itemQuantity--;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " Item Quantity: " + itemQuantity + " Item Status: " + itemStatus;
    }
}
