package com.example.getfood.ui.foodmenu

import com.example.canteen_app_models.models.FoodItem
import com.example.getfood.ui.base.BaseView
import java.util.*

interface FoodCategoryMvpView : BaseView {
    fun bindFoodListAdapter(foodItems: ArrayList<FoodItem?>?)
    fun onDatabaseError(error: Error?)
}