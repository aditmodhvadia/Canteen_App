package com.example.getfood.ui.loginregister;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface LoginMvpPresenter<V extends LoginMvpView> extends BaseMvpPresenter<V> {

    void performSignOut();

    boolean isUserEmailVerified();
}
