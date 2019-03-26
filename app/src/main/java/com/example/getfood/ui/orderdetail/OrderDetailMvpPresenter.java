package com.example.getfood.ui.orderdetail;

import com.example.getfood.Models.OrderDetailItem;
import com.example.getfood.ui.base.BaseMvpPresenter;

public interface OrderDetailMvpPresenter<V extends OrderDetailMvpView> extends BaseMvpPresenter<V> {
    void fetchOrderDetails(String orderID);

    void setRatingValue(String ratingValue, OrderDetailItem orderDetailItem);
}
