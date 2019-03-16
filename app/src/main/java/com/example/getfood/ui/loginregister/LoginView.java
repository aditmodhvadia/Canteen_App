package com.example.getfood.ui.loginregister;

import android.widget.EditText;

public interface LoginView {

    void loginSuccess();

    void loginErrror(String errmsg, EditText editText);

}
