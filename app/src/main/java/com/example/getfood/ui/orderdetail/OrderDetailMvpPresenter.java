package com.example.getfood.ui.orderdetail;

import com.example.getfood.models.FullOrder;
import com.example.getfood.models.OrderDetailItem;
import com.example.getfood.ui.base.BaseMvpPresenter;

public interface OrderDetailMvpPresenter<V extends OrderDetailMvpView> extends BaseMvpPresenter<V> {
    void fetchOrderDetails(FullOrder fullOrder);

    void setRatingValue(String ratingValue, OrderDetailItem orderDetailItem);
}
