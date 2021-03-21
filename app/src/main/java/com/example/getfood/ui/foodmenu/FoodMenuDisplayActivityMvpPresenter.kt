package com.example.getfood.ui.foodmenu

import com.example.getfood.ui.base.BaseMvpPresenter

interface FoodMenuDisplayActivityMvpPresenter<V : FoodMenuDisplayActivityMvpView?> : BaseMvpPresenter<V> {
    val userEmail: String?
    fun sendPasswordResetEmail()
    fun signOutUser()
    fun openCart()
}