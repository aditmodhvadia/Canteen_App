package com.example.getfood.ui.loginregister.loginfragment

import com.example.getfood.ui.base.BaseMvpPresenter

interface LoginMvpPresenter<V : LoginMvpView?> : BaseMvpPresenter<V> {
    fun performSignOut()
    val isUserEmailVerified: Boolean
    fun performLogin(userEmail: String, password: String)
    fun verifyUserEmail()
    fun sendEmailForVerification()
    fun forgotPasswordClicked(userEmail: String)
    fun updateToken(token: String?)
}