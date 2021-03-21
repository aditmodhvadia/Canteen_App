package com.example.getfood.ui.cart

import com.example.canteen_app_models.models.FullOrder
import com.example.getfood.ui.base.BaseView

interface CartMvpView : BaseView {
    fun onOrderFailed(error: Error?)
    fun onOrderPlacedSuccessfully(orderId: String?)
    fun onOrderDataFetchedSuccessfully(fullOrder: FullOrder?)
}