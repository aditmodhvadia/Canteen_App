package com.example.getfood.ui.foodmenu

import com.example.canteen_app_models.models.CartItem
import com.example.canteen_app_models.models.FoodItem
import com.example.firebase_api_library.listeners.DBValueEventListener
import com.example.getfood.ui.base.BasePresenter
import java.util.*

class FoodCategoryPresenter<V : FoodCategoryMvpView?> : BasePresenter<V>(), FoodCategoryMvpPresenter<V> {
    override fun fetchFoodList(category: String?) {
        apiManager!!.foodMenuListener(category!!, object : DBValueEventListener<ArrayList<FoodItem?>?> {
            override fun onDataChange(data: ArrayList<FoodItem?>?) {
                mvpView?.bindFoodListAdapter(data)
            }

            override fun onCancelled(error: Error?) {}

        })
    }

    override val cartItems: MutableList<CartItem>
        get() = dataManager.cartItems

    override fun addFoodItemToCart(cartItem: CartItem) {
        dataManager.cartItems.add(cartItem)
    }
}