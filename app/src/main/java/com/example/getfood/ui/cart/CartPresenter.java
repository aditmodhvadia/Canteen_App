package com.example.getfood.ui.cart;

import com.example.canteen_app_models.models.CartItem;
import com.example.canteen_app_models.models.FullOrder;
import com.example.firebase_api_library.listeners.DBValueEventListener;
import com.example.firebase_api_library.listeners.OnTaskCompleteListener;
import com.example.getfood.ui.base.BasePresenter;
import com.example.getfood.utils.AppUtils;

import java.util.ArrayList;

public class CartPresenter<V extends CartMvpView> extends BasePresenter<V> implements CartMvpPresenter<V> {

    public CartPresenter() {
    }

    @Override
    public String getNewOrderKey() {
        return apiManager.getNewOrderKey();

    }

    @Override
    public void setNewOrder(final FullOrder fullOrder) {
        apiManager.setOrderValue(fullOrder, new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
                dataManager.clearCartItems();
                getMvpView().onOrderPlacedSuccessfully(fullOrder.getOrderId());
            }

            @Override
            public void onTaskCompleteButFailed(String errMsg) {
                getMvpView().onOrderFailed(new Error(errMsg));
            }

            @Override
            public void onTaskFailed(Exception e) {

            }
        });
    }

    @Override
    public void getOrderData(String orderId) {
        apiManager.orderDetailListener(orderId, new DBValueEventListener<FullOrder>() {
            @Override
            public void onDataChange(FullOrder data) {
                getMvpView().onOrderDataFetchedSuccessfully(data);
            }

            @Override
            public void onCancelled(Error error) {
//                Keeping same method right now, to be updated when required
                getMvpView().onOrderFailed(error);
            }
        });
    }

    @Override
    public ArrayList<CartItem> getCartItems() {
        return dataManager.getCartItems();
    }

    @Override
    public void increaseCartQuantity(int adapterPosition) {
        dataManager.increaseCartItemQuantity(adapterPosition);
    }

    @Override
    public void decreaseCartItemQuantity(int adapterPosition) {
        dataManager.getCartItems().get(adapterPosition).decreaseQuantity();
    }

    @Override
    public int getCartTotal() {
        return dataManager.getCartTotal();
    }

    @Override
    public String getRollNo() {
        return AppUtils.getRollNoFromEmail(apiManager.getCurrentUserEmail());
    }

    @Override
    public void undoCartItemRemove(CartItem removedItem, int position) {
        dataManager.getCartItems().add(position, removedItem);
    }

    @Override
    public void removeCartItem(int adapterPosition) {
        dataManager.getCartItems().remove(adapterPosition);
    }

    @Override
    public void clearCartItems() {
        dataManager.getCartItems().clear();
    }

    @Override
    public void sortCartItems() {
        dataManager.sortCartItems();
    }
}
