package com.example.getfood;

import java.util.ArrayList;

public class FoodItem {

    String itemName, itemPrice, itemRating;

    public FoodItem(String itemName, String itemPrice, String itemRating) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemRating = itemRating;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemRating() {
        return itemRating;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemRating(String itemRating) {
        this.itemRating = itemRating;
    }
}
