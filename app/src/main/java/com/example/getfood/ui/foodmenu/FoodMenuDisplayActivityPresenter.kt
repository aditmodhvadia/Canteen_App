package com.example.getfood.ui.foodmenu

import com.example.firebase_api_library.listeners.OnTaskCompleteListener
import com.example.getfood.ui.base.BasePresenter

class FoodMenuDisplayActivityPresenter<V : FoodMenuDisplayActivityMvpView?> : BasePresenter<V>(), FoodMenuDisplayActivityMvpPresenter<V> {
    override fun sendPasswordResetEmail() {
        apiManager!!.sendPasswordResetEmail(userEmail, object : OnTaskCompleteListener {
            override fun onTaskSuccessful() {
                signOutUser()
                mvpView?.onPasswordResetEmailSentSuccessfully()
            }

            override fun onTaskCompleteButFailed(errMsg: String?) {
                mvpView?.onPasswordResetEmailSentFailed(errMsg)
            }

            override fun onTaskFailed(e: Exception?) {}
        })
    }

    override fun openCart() {
        if (dataManager.cartSize > 0) {
            mvpView?.openCart()
        } else {
            mvpView?.cantOpenCart()
        }
    }

    override fun signOutUser() {
        apiManager!!.forceSignOutUser()
    }

    override val userEmail: String?
        get() = apiManager?.userEmail
}