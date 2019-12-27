package com.example.getfood.ui.cart;

import android.content.Context;

import com.example.canteen_app_models.models.FullOrder;
import com.example.getfood.ui.base.BaseView;

public interface CartMvpView extends BaseView {

    Context getContext();

    void onOrderFailed(Error error);

    void onOrderPlacedSuccessfully(String orderId);

    void onOrderDataFetchedSuccessfully(FullOrder fullOrder);
}
