package com.fazemeright.canteen_app_models.models;

import android.support.annotation.NonNull;

import com.fazemeright.canteen_app_models.helpers.FoodMenuDetails;

import java.io.Serializable;
import java.util.HashMap;

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

    public static FoodItem fromMap(Object dsp, String category, String itemName) {
        HashMap<String, Object> map = (HashMap<String, Object>) dsp;
        if (map != null) {
            String rating = null;
            if (map.containsKey(FoodMenuDetails.RATING)) {
                rating = String.valueOf(map.get(FoodMenuDetails.RATING));
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

    public String getItemRating() {
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
}
