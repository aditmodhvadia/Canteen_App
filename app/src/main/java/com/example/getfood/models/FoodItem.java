package com.example.getfood.models;

public class FoodItem {

    private String itemName, itemPrice, itemRating, itemCategory;

    public FoodItem(String itemName, String itemPrice, String itemRating, String itemCategory) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemRating = itemRating;
        this.itemCategory = itemCategory;
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
