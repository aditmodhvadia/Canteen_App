package com.example.getfood.ui.loginregister;

import com.example.getfood.ui.base.BasePresenter;

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    public LoginPresenter() {
    }


    @Override
    public void performSignOut() {
        apiManager.forceSignOutUser();
    }

    @Override
    public boolean isUserEmailVerified() {
        return apiManager.isUserEmailVerified();
    }
}
