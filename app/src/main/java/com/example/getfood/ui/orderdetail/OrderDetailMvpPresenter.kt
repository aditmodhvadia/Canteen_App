package com.example.getfood.ui.orderdetail

import com.example.canteen_app_models.models.FullOrder
import com.example.getfood.ui.base.BaseMvpPresenter

interface OrderDetailMvpPresenter<V : OrderDetailMvpView?> : BaseMvpPresenter<V> {
    fun fetchOrderDetails(fullOrder: FullOrder)
    fun setRatingValueForOrderItem(rating: Float, position: Int, order: String?)
}