package com.example.getfood.ui.orderlist

import com.example.canteen_app_models.models.FullOrder
import com.example.firebase_api_library.listeners.DBValueEventListener
import com.example.getfood.ui.base.BasePresenter
import com.example.getfood.utils.AppUtils
import java.util.*

class OrderListPresenter<V : OrderListMvpView?> : BasePresenter<V>(), OrderListMvpPresenter<V> {
    override fun fetchOrderList() {
        val userRollNo: String? = AppUtils.getRollNoFromEmail(apiManager?.userEmail)
        if (userRollNo == null) {
            mvpView?.onRollNumberNull()
            return
        }
        apiManager!!.orderListListener(object : DBValueEventListener<ArrayList<FullOrder?>?> {
            override fun onDataChange(data: ArrayList<FullOrder?>?) {
//                TODO: Later add option to flip the order list based on order timing
                data?.reverse()
                mvpView?.bindListAdapter(data)
            }

            override fun onCancelled(error: Error?) {}
        })
    }
}