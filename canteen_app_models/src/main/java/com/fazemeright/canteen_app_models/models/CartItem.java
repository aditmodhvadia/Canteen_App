package com.fazemeright.canteen_app_models.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class CartItem extends FoodItem implements Serializable {

    private Integer itemQuantity;
    private String itemStatus;
//    private FoodItem foodItem;

    public CartItem(FoodItem foodItem, String itemStatus, Integer cartItemQuantity) {
        super(foodItem.getItemName(), foodItem.getItemPrice(), null, foodItem.getItemCategory());
        this.itemQuantity = cartItemQuantity;
        this.itemStatus = itemStatus;
//        this.foodItem = foodItem;
//        setItemCategory(foodItem.getItemCategory());
//        setItemName(foodItem.getItemName());
//        setItemPrice(foodItem.getItemPrice());
    }

    public CartItem() {
    }

    public String getItemStatus() {
        return itemStatus;
    }

    /*public FoodItem getFoodItem() {
        return foodItem;
    }*/

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
