package com.example.getfood.ui.base;

import android.content.Context;

public interface BaseView {

    void showLoading();

    void hideLoading();

    boolean isNetworkConnected();

    Context getContext();
}
