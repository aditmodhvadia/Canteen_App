package com.example.getfood.ui.base;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

public interface BaseView {

    void showLoading();

    void hideLoading();

    boolean isNetworkConnected();

    Context getContext();

    FirebaseAuth getFirebaseAuth();
}
