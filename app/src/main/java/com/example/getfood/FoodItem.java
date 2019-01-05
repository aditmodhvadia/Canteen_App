package com.example.getfood;

import java.util.ArrayList;

public class FoodItem {

    ArrayList<String> itemName, itemPrice, itemRating;

    public FoodItem(ArrayList<String> itemName, ArrayList<String> itemPrice, ArrayList<String> itemRating) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemRating = itemRating;
    }

    public void setItemName(ArrayList<String> itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(ArrayList<String> itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemRating(ArrayList<String> itemRating) {
        this.itemRating = itemRating;
    }

    public ArrayList<String> getItemName() {

        return itemName;
    }

    public ArrayList<String> getItemPrice() {
        return itemPrice;
    }

    public ArrayList<String> getItemRating() {
        return itemRating;
    }
}
