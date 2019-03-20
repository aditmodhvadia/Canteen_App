package com.example.getfood.ui.orderlist;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface OrderListMvpPresenter<V extends OrderListMvpView> extends BaseMvpPresenter<V> {
    void fetchOrderList(String rollNo);
}
