package com.example.getfood.callback;

import com.fazemeright.canteen_app_models.models.FoodItem;

public interface FoodItemTouchListener {
    void onItemClicked(int position, FoodItem foodItem);
}
