package com.example.getfood.ui.orderdetail;

import com.example.getfood.ui.base.BaseMvpPresenter;
import com.fazemeright.canteen_app_models.models.FullOrder;
import com.fazemeright.canteen_app_models.models.OrderDetailItem;

public interface OrderDetailMvpPresenter<V extends OrderDetailMvpView> extends BaseMvpPresenter<V> {
    void fetchOrderDetails(FullOrder fullOrder);

    void setRatingValue(String ratingValue, OrderDetailItem orderDetailItem);
}
