package com.example.getfood.ui.cart;

import android.support.annotation.NonNull;

import com.example.getfood.ui.base.BasePresenter;
import com.example.getfood.utils.AppUtils;
import com.fazemeright.canteen_app_models.models.CartItem;
import com.fazemeright.canteen_app_models.models.FullOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
        apiManager.setOrderValue(fullOrder, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dataManager.clearCartItems();
                    getMvpView().onOrderPlacedSuccessfully(fullOrder.getOrderId());
                } else {
                    getMvpView().onOrderFailed(task.getException());
                }
            }
        });
    }

    @Override
    public void getOrderData(String orderId) {
        apiManager.getNewOrderData(orderId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMvpView().onOrderDataFetchedSuccessfully(dataSnapshot.getValue(FullOrder.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Keeping same method right now, to be updated when required
                getMvpView().onOrderFailed(new Exception(databaseError.getMessage()));
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
