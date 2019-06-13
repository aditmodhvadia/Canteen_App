package com.example.getfood.ui.loginregister.loginfragment;

import android.content.Context;

import com.example.getfood.ui.base.BaseView;

public interface LoginMvpView extends BaseView {

    Context getContext();

    void valueEntryError(String errMsg);

    void userVerifiedSuccessfully();

    void onUserEmailVerificationFailed();

    void onPasswordResetEmailSentSuccessfully();
}
