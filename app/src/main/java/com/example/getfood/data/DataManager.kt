package com.example.getfood.data

import com.example.canteen_app_models.models.CartItem
import java.util.*

class DataManager {
    var cartItems: MutableList<CartItem>
        get() {
            return Companion.cartItems
        }
        set(cartItems) {
            Companion.cartItems = cartItems
        }
    val cartSize: Int
        get() {
            return Companion.cartItems.size
        }
    val cartTotal: Int
        get() {
            var total = 0
            for (item: CartItem? in Companion.cartItems) {
                total += item!!.itemPrice!!.toInt() * (item.itemQuantity)!!
            }
            return total
        }

    fun increaseCartItemQuantity(position: Int) {
        Companion.cartItems[position].increaseQuantity()
    }

    fun sortCartItems() {
        Companion.cartItems.sortWith { o1, o2 ->
            o1.itemCategory!!.length - o2.itemCategory!!.length
        }
    }

    fun clearCartItems() {
        Companion.cartItems.clear()
    }

    companion object {
        private var cartItems: MutableList<CartItem> = mutableListOf()
        private lateinit var dataManager: DataManager

        @get:Synchronized
        val instance: DataManager
            get() {
                if (!this::dataManager.isInitialized) {
                    dataManager = DataManager()
                    cartItems = ArrayList()
                }
                return dataManager
            }
    }
}