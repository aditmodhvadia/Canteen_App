package com.example.getfood.ui.orderdetail;

import android.content.Context;

import com.example.getfood.models.FullOrder;
import com.example.getfood.ui.base.BaseView;

public interface OrderDetailMvpView extends BaseView {

    Context getContext();

    void bindOrderDetailAdapter(FullOrder updatedOrder);

    void onDatabaseError(Error error);

    void onRatingUpdatedSuccessfully();

    void onRatingUpdateFailed(Error error);
}
