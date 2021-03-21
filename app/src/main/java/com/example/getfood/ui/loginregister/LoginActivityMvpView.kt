package com.example.getfood.ui.loginregister

import android.content.Context
import com.example.getfood.ui.base.BaseView

open interface LoginActivityMvpView : BaseView {
    val context: Context
    fun onSuccessfulVerificationAndSignIn()
    fun onFailedVerificationOrSignIn(e: Exception)
    fun onTokenUpdatedSuccessfully()
}