package com.example.getfood.ui.orderdetail;

import com.example.getfood.ui.base.BaseMvpPresenter;
import com.fazemeright.canteen_app_models.models.FullOrder;

public interface OrderDetailMvpPresenter<V extends OrderDetailMvpView> extends BaseMvpPresenter<V> {
    void fetchOrderDetails(FullOrder fullOrder);

    void setRatingValueForOrderItem(float rating, int position, String order);
}
