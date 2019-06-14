package com.example.getfood.ui.cart;

import android.content.Context;

import com.example.getfood.models.FullOrder;
import com.example.getfood.ui.base.BaseView;

public interface CartMvpView extends BaseView {

    Context getContext();

    void onOrderFailed(Exception exception);

    void onOrderPlacedSuccessfully(String orderId);

    void onOrderDataFetchedSuccessfully(FullOrder fullOrder);
}
