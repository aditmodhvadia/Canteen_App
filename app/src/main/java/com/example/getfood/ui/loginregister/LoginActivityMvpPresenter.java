package com.example.getfood.ui.loginregister;

import android.content.Intent;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface LoginActivityMvpPresenter<V extends LoginActivityMvpView> extends BaseMvpPresenter<V> {
    void bindDynamicLinkEmailVerification(Intent intent);

    boolean isUserLoggedIn();

    void signOutUser();
}
