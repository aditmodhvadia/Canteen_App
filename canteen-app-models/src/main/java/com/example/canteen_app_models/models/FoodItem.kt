package com.example.canteen_app_models.models;

import androidx.annotation.NonNull;

import com.example.canteen_app_models.helpers.FoodMenuDetails;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class FoodItem implements Serializable {

    protected String itemName, itemPrice, itemCategory;
    protected long itemRating;

    public FoodItem(String itemName, String itemPrice, long itemRating, String itemCategory) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemRating = itemRating;
        this.itemCategory = itemCategory;
    }

    public FoodItem() {
    }

    public static FoodItem fromMap(Object dsp, String category, String itemName) {
        HashMap<String, Object> map = (HashMap<String, Object>) dsp;
        if (map != null) {
            long rating = -1;
            if (map.containsKey(FoodMenuDetails.RATING)) {
                rating = (long) map.get(FoodMenuDetails.RATING);
            }
            return new FoodItem(itemName, String.valueOf(map.get(FoodMenuDetails.PRICE)),
                    rating,
                    category);
        } else {
            return null;
        }
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

    public long getItemRating() {
        return itemRating;
    }

    @NonNull
    @Override
    public String toString() {
        String itemData = "";
        itemData = itemData.concat("\n\t\t Item Name: " + itemName
                + " Item Category: " + itemCategory + " Item Price: " + itemPrice
                + " Item Rating: " + itemRating);
        return "\n Item Data : " + itemData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return itemRating == foodItem.itemRating &&
                itemName.equals(foodItem.itemName) &&
                Objects.equals(itemPrice, foodItem.itemPrice) &&
                Objects.equals(itemCategory, foodItem.itemCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, itemPrice, itemCategory, itemRating);
    }
}
