package com.example.getfood.ui.loginregister.loginfragment;

import android.support.annotation.NonNull;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface LoginMvpPresenter<V extends LoginMvpView> extends BaseMvpPresenter<V> {

    void performSignOut();

    boolean isUserEmailVerified();

    void performLogin(@NonNull String userEmail, @NonNull String password);

    void verifyUserEmail();

    void sendEmailForVerification();

    void forgotPasswordClicked(String userEmail);
}
