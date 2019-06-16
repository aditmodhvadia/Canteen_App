package com.example.getfood.ui.orderdetail;

import android.content.Context;

import com.example.getfood.ui.base.BaseView;
import com.fazemeright.canteen_app_models.models.FullOrder;

public interface OrderDetailMvpView extends BaseView {

    Context getContext();

    void bindOrderDetailAdapter(FullOrder updatedOrder);

    void onDatabaseError(Error error);

    void onRatingUpdatedSuccessfully();

    void onRatingUpdateFailed(Error error);
}
