package com.example.getfood.ui.cart

import com.example.canteen_app_models.models.CartItem
import com.example.canteen_app_models.models.FullOrder
import com.example.firebase_api_library.listeners.DBValueEventListener
import com.example.firebase_api_library.listeners.OnTaskCompleteListener
import com.example.getfood.ui.base.BasePresenter
import com.example.getfood.utils.AppUtils

class CartPresenter<V : CartMvpView?> : BasePresenter<V>(), CartMvpPresenter<V> {
    override val newOrderKey: String?
        get() = apiManager!!.newOrderKey

    override fun setNewOrder(fullOrder: FullOrder) {
        apiManager!!.setOrderValue(fullOrder, object : OnTaskCompleteListener {
            override fun onTaskSuccessful() {
                dataManager.clearCartItems()
                mvpView?.onOrderPlacedSuccessfully(fullOrder.orderId)
            }

            override fun onTaskCompleteButFailed(errMsg: String?) {
                mvpView?.onOrderFailed(Error(errMsg))
            }

            override fun onTaskFailed(e: Exception?) {}
        })
    }

    override fun getOrderData(orderId: String?) {
        apiManager!!.orderDetailListener(orderId!!, object : DBValueEventListener<FullOrder?> {
            override fun onDataChange(data: FullOrder?) {
                mvpView?.onOrderDataFetchedSuccessfully(data)
            }

            override fun onCancelled(error: Error?) {
//                Keeping same method right now, to be updated when required
                mvpView?.onOrderFailed(error)
            }
        })
    }

    override val cartItems: MutableList<CartItem>
        get() = dataManager.cartItems

    override fun increaseCartQuantity(adapterPosition: Int) {
        dataManager.increaseCartItemQuantity(adapterPosition)
    }

    override fun decreaseCartItemQuantity(adapterPosition: Int) {
        dataManager.cartItems[adapterPosition].decreaseQuantity()
    }

    override val cartTotal: Int
        get() = dataManager.cartTotal
    override val rollNo: String?
        get() = AppUtils.getRollNoFromEmail(apiManager?.userEmail)

    override fun undoCartItemRemove(removedItem: CartItem, position: Int) {
        dataManager.cartItems.add(position, removedItem)
    }

    override fun removeCartItem(adapterPosition: Int) {
        dataManager.cartItems.removeAt(adapterPosition)
    }

    override fun clearCartItems() {
        dataManager.cartItems.clear()
    }

    override fun sortCartItems() {
        dataManager.sortCartItems()
    }
}