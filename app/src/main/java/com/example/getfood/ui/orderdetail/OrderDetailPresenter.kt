package com.example.getfood.ui.orderdetail

import com.example.canteen_app_models.models.FullOrder
import com.example.firebase_api_library.listeners.DBValueEventListener
import com.example.getfood.ui.base.BasePresenter

class OrderDetailPresenter<V : OrderDetailMvpView?> : BasePresenter<V>(), OrderDetailMvpPresenter<V> {
    override fun fetchOrderDetails(fullOrder: FullOrder) {
        apiManager!!.orderDetailListener(fullOrder.orderId!!, object : DBValueEventListener<FullOrder?> {
            override fun onDataChange(data: FullOrder?) {
                data?.let {
                    mvpView?.bindOrderDetailAdapter(data)
                }
            }

            override fun onCancelled(error: Error?) {
                mvpView?.onDatabaseError(error)
            }
        })
    }

    override fun setRatingValueForOrderItem(rating: Float, position: Int, order: String?) {
        apiManager!!.setRatingValueForOrderItem(rating, position, order)
    }
}