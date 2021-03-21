package com.example.getfood.ui.cart

import com.example.canteen_app_models.models.CartItem
import com.example.canteen_app_models.models.FullOrder
import com.example.getfood.ui.base.BaseMvpPresenter

interface CartMvpPresenter<V : CartMvpView?> : BaseMvpPresenter<V> {
    val cartTotal: Int
    val rollNo: String?
    val cartItems: MutableList<CartItem>
    fun increaseCartQuantity(adapterPosition: Int)
    fun decreaseCartItemQuantity(adapterPosition: Int)
    fun undoCartItemRemove(removedItem: CartItem, position: Int)
    fun removeCartItem(adapterPosition: Int)
    fun clearCartItems()
    fun sortCartItems()
    val newOrderKey: String?
    fun setNewOrder(fullOrder: FullOrder)
    fun getOrderData(orderId: String?)
}