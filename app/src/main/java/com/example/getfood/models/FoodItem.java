package com.example.getfood.models;

import java.io.Serializable;

public class FoodItem implements Serializable {

    protected String itemName, itemPrice, itemRating, itemCategory;

    public FoodItem(String itemName, String itemPrice, String itemRating, String itemCategory) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemRating = itemRating;
        this.itemCategory = itemCategory;
    }

    public FoodItem() {
    }

    public String getItemCategory() {
        return itemCategory;
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
}
