package com.example.getfood.Fragment.login;

import android.widget.EditText;

public class LoginPresenter implements LoginMvpPresenter{

    LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }


    @Override
    public void performLogin(String userEmail, String userPassword, EditText email, EditText password) {

//        loginView.loginErrror();

    }
}
