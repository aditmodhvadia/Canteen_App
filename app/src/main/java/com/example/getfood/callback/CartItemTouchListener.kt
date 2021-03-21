package com.example.getfood.callback

import com.example.canteen_app_models.models.CartItem

open interface CartItemTouchListener {
    fun onIncreaseClicked(adapterPosition: Int)
    fun onDecreaseClicked(adapterPosition: Int)
    fun onItemRemoved(adapterPosition: Int)
    fun onItemRemoveUndo(removedItem: CartItem, mRecentlyDeletedItemPosition: Int)
}