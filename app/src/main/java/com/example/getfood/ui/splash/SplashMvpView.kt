package com.example.getfood.ui.splash

import android.content.Context
import com.example.getfood.ui.base.BaseView

interface SplashMvpView : BaseView {
    val context: Context
    fun userIsSignedIn()
    fun userIsNotSignedIn()
    fun updateNotRequired()
    fun updateRequired()
}