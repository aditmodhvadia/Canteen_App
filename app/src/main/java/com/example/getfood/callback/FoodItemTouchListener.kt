package com.example.getfood.callback

import com.example.canteen_app_models.models.FoodItem

open interface FoodItemTouchListener {
    fun onItemClicked(position: Int, foodItem: FoodItem?)
}