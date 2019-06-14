package com.example.getfood.callback;

import com.example.getfood.models.CartItem;

public interface CartItemTouchListener {
    void onIncreaseClicked(int adapterPosition);

    void onDecreaseClicked(int adapterPosition);

    void onItemRemoved(int adapterPosition);

    void onItemRemoveUndo(CartItem removedItem, int mRecentlyDeletedItemPosition);
}
