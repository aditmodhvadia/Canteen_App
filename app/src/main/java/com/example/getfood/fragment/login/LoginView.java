package com.example.getfood.fragment.login;

import android.widget.EditText;

public interface LoginView {

    void loginSuccess();

    void loginErrror(String errmsg, EditText editText);

}
