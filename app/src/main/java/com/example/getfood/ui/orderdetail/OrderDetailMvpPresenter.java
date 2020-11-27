package com.example.getfood.ui.orderdetail;

import com.example.canteen_app_models.models.FullOrder;
import com.example.getfood.ui.base.BaseMvpPresenter;

public interface OrderDetailMvpPresenter<V extends OrderDetailMvpView> extends BaseMvpPresenter<V> {
    void fetchOrderDetails(FullOrder fullOrder);

    void setRatingValueForOrderItem(float rating, int position, String order);
}
