package com.example.getfood.ui.foodmenu;

import com.example.getfood.ui.base.BasePresenter;
import com.fazemeright.firebase_api__library.listeners.OnTaskCompleteListener;

public class FoodMenuDisplayActivityPresenter<V extends FoodMenuDisplayActivityMvpView> extends BasePresenter<V> implements FoodMenuDisplayActivityMvpPresenter<V> {

    public FoodMenuDisplayActivityPresenter() {
    }

    @Override
    public String getUserEmail() {
        return apiManager.getCurrentUserEmail();
    }

    @Override
    public void sendPasswordResetEmail() {
        apiManager.sendPasswordResetEmail(getUserEmail(), new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
                signOutUser();
                getMvpView().onPasswordResetEmailSentSuccessfully();
            }

            @Override
            public void onTaskCompleteButFailed(String errMsg) {
                getMvpView().onPasswordResetEmailSentFailed(errMsg);
            }

            @Override
            public void onTaskFailed(Exception e) {

            }
        });
    }

    @Override
    public void openCart() {
        if (dataManager.getCartSize() > 0) {
            getMvpView().openCart();
        } else {
            getMvpView().cantOpenCart();
        }
    }

    @Override
    public void signOutUser() {
        apiManager.forceSignOutUser();
    }
}
