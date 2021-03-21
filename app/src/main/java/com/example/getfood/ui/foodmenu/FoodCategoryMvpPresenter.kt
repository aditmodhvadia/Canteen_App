package com.example.getfood.ui.foodmenu

import com.example.canteen_app_models.models.CartItem
import com.example.getfood.ui.base.BaseMvpPresenter

interface FoodCategoryMvpPresenter<V : FoodCategoryMvpView?> : BaseMvpPresenter<V> {
    fun fetchFoodList(category: String?)
    val cartItems: MutableList<CartItem>
    fun addFoodItemToCart(cartItem: CartItem)
}