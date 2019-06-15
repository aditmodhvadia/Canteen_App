package com.example.getfood.ui.foodmenu;

import android.content.Context;

import com.example.getfood.ui.base.BaseView;

public interface FoodMenuDisplayActivityMvpView extends BaseView {

    Context getContext();

    void onPasswordResetEmailSentSuccessfully();

    void onPasswordResetEmailSentFailed(String errMsg);

    void openCart();

    void cantOpenCart();
}
