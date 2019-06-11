package com.example.getfood.models;

import android.support.annotation.NonNull;

import com.example.getfood.api.FireBaseApiManager;
import com.google.firebase.database.DataSnapshot;

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

    public static FoodItem fromMap(DataSnapshot dsp, String category) {
        HashMap<String, Object> map = (HashMap<String, Object>) dsp.getValue();
        if (map != null) {
            String rating = null;
            if (map.containsKey(FireBaseApiManager.FoodMenuDetails.RATING)) {
                rating = String.valueOf(map.get(FireBaseApiManager.FoodMenuDetails.RATING));
            }
            return new FoodItem(dsp.getKey(), String.valueOf(map.get(FireBaseApiManager.FoodMenuDetails.PRICE)),
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
