package com.example.getfood.ui.loginregister.registerfragment

import com.example.getfood.ui.base.BaseMvpPresenter

open interface RegisterMvpPresenter<V : RegisterMvpView?> : BaseMvpPresenter<V> {
    fun performRegistration(userEmail: String, password: String, confirmPassword: String)
    fun verifyUserEmail()
    fun signOutUser()
    fun isTermsAndConditionChecked(checked: Boolean): Boolean
}