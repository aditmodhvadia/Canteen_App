package com.example.getfood.ui.loginregister

import android.content.Intent
import com.example.getfood.ui.base.BaseMvpPresenter

open interface LoginActivityMvpPresenter<V : LoginActivityMvpView?> : BaseMvpPresenter<V> {
    fun bindDynamicLinkEmailVerification(intent: Intent?)
    val isUserLoggedIn: Boolean
    fun signOutUser()
    fun updateToken(token: String?)
}