package com.example.getfood.ui.loginregister;

import android.content.Context;

import com.example.getfood.ui.base.BaseView;

public interface LoginActivityMvpView extends BaseView {

    Context getContext();

    void onSuccessfulVerificationAndSignIn();

    void onFailedVerificationOrSignIn(Exception e);

    void onTokenUpdatedSuccessfully();
}
