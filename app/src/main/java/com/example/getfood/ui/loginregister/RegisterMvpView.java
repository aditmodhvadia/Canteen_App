package com.example.getfood.ui.loginregister;

import android.content.Context;

import com.example.getfood.ui.base.BaseView;

public interface RegisterMvpView extends BaseView {

    Context getContext();

    void valueEntryError(String errorMsg);

    void onUserCreatedSuccessfully();

    void userVerifiedSuccessfully();

    void onUserEmailVerificationFailed();
}
