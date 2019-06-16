package com.example.getfood.ui.cart;

import android.content.Context;

import com.example.getfood.ui.base.BaseView;
import com.fazemeright.canteen_app_models.models.FullOrder;

public interface CartMvpView extends BaseView {

    Context getContext();

    void onOrderFailed(Error error);

    void onOrderPlacedSuccessfully(String orderId);

    void onOrderDataFetchedSuccessfully(FullOrder fullOrder);
}
