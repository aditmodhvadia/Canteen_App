package com.example.getfood.ui.orderlist

import com.example.getfood.ui.base.BaseMvpPresenter

interface OrderListMvpPresenter<V : OrderListMvpView?> : BaseMvpPresenter<V> {
    fun fetchOrderList()
}