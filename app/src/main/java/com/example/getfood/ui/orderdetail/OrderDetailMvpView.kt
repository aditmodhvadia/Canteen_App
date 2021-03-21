package com.example.getfood.ui.orderdetail

import com.example.canteen_app_models.models.FullOrder
import com.example.getfood.ui.base.BaseView

interface OrderDetailMvpView : BaseView {

    fun bindOrderDetailAdapter(updatedOrder: FullOrder)
    fun onDatabaseError(error: Error?)
    fun onRatingUpdatedSuccessfully()
    fun onRatingUpdateFailed(error: Error)
}