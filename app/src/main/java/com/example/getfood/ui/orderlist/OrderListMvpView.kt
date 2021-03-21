package com.example.getfood.ui.orderlist

import com.example.canteen_app_models.models.FullOrder
import com.example.getfood.ui.base.BaseView
import java.util.*

interface OrderListMvpView : BaseView {
    fun bindListAdapter(orderListItems: ArrayList<FullOrder?>?)

    fun onRollNumberNull()
}