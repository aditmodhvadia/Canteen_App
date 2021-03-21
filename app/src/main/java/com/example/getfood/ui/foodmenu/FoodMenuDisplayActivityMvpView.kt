package com.example.getfood.ui.foodmenu

import com.example.getfood.ui.base.BaseView

interface FoodMenuDisplayActivityMvpView : BaseView {

    fun onPasswordResetEmailSentSuccessfully()
    fun onPasswordResetEmailSentFailed(errMsg: String?)
    fun openCart()
    fun cantOpenCart()
}