package com.example.getfood.ui.splash

import com.example.getfood.ui.base.BaseMvpPresenter

interface SplashMvpPresenter<V : SplashMvpView?> : BaseMvpPresenter<V> {
    fun determineIfUserLoggedIn()
    val versionName: String
    fun determineIfUpdateNeeded(versionName: String?)
}