package com.example.getfood.ui.cart;

import com.example.canteen_app_models.models.CartItem;
import com.example.canteen_app_models.models.FullOrder;
import com.example.getfood.ui.base.BaseMvpPresenter;

import java.util.ArrayList;

public interface CartMvpPresenter<V extends CartMvpView> extends BaseMvpPresenter<V> {


    int getCartTotal();

    String getRollNo();

    ArrayList<CartItem> getCartItems();

    void increaseCartQuantity(int adapterPosition);

    void decreaseCartItemQuantity(int adapterPosition);

    void undoCartItemRemove(CartItem removedItem, int position);

    void removeCartItem(int adapterPosition);

    void clearCartItems();

    void sortCartItems();

    String getNewOrderKey();

    void setNewOrder(FullOrder fullOrder);

    void getOrderData(String orderId);
}
