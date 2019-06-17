package com.example.getfood.ui.foodmenu;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface FoodMenuDisplayActivityMvpPresenter<V extends FoodMenuDisplayActivityMvpView> extends BaseMvpPresenter<V> {

    String getUserEmail();

    void sendPasswordResetEmail();

    void signOutUser();

    void openCart();
}
