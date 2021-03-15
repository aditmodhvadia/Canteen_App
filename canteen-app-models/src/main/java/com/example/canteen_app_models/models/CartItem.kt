package com.example.canteen_app_models.models

import java.io.Serializable

data class CartItem(var itemQuantity: Int? = null,
                    var itemStatus: String? = null,
                    var foodItem: FoodItem) : FoodItem(), Serializable {


    fun increaseQuantity() {
        itemQuantity = itemQuantity?.plus(1)
    }

    fun decreaseQuantity() {
        itemQuantity = itemQuantity?.minus(1)
    }
}