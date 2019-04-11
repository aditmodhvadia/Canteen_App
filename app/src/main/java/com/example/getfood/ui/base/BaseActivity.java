package com.example.getfood.ui.base;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.getfood.utils.ProgressHandler;
import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    public Context mContext;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mAuth = FirebaseAuth.getInstance();
        setContentView(getLayoutResId());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initViews();
        setListeners();
    }

    /**
     * To initialize views of activity
     */
    public abstract void initViews();

    /**
     * To set listeners of view or callback
     */
    public abstract void setListeners();

    /**
     * To get layout resource id
     */
    public abstract int getLayoutResId();

    @Override
    public void showLoading() {
        ProgressHandler.showProgress(mContext, "", "");
    }

    @Override
    public void hideLoading() {
        ProgressHandler.hideProgress();
    }

    @Override
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(mContext);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
