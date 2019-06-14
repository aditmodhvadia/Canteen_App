package com.example.getfood.callback;

import com.example.getfood.models.FoodItem;

public interface FoodItemTouchListener {
    void onItemClicked(int position, FoodItem foodItem);
}
