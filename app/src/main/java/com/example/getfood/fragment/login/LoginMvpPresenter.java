package com.example.getfood.fragment.login;

import android.widget.EditText;

public interface LoginMvpPresenter {

    void performLogin(String userEmail, String userPassword, EditText email, EditText password);

}
