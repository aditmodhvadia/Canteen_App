package com.example.getfood.ui.loginregister;

import android.widget.EditText;

public interface LoginMvpPresenter {

    void performLogin(String userEmail, String userPassword, EditText email, EditText password);

}
