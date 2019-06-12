package com.example.getfood.ui.loginregister;

import android.support.annotation.NonNull;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface RegisterMvpPresenter<V extends RegisterMvpView> extends BaseMvpPresenter<V> {
    void performRegistration(@NonNull String userEmail, @NonNull String password, String confirmPassword);

    void verifyUserEmail();

    void signOutUser();
}
