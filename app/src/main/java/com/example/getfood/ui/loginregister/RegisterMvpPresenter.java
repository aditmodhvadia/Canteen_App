package com.example.getfood.ui.loginregister;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface RegisterMvpPresenter<V extends RegisterMvpView> extends BaseMvpPresenter<V> {
    void performRegistration(String userEmail, String password, String confirmPassword);

    void verifyUserEmail();

    void signOutUser();
}
