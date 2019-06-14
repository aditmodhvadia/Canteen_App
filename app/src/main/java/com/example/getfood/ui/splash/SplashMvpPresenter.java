package com.example.getfood.ui.splash;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface SplashMvpPresenter<V extends SplashMvpView> extends BaseMvpPresenter<V> {
    void determineIfUserLoggedIn();

    String getVersionName();

    void determineIfUpdateNeeded(String versionName);
}
