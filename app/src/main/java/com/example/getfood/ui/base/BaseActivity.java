package com.example.getfood.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.getfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    public Context mContext;
    public FirebaseAuth mAuth;
    private Dialog loading;
    private AVLoadingIndicatorView avl;

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
        loading = new Dialog(mContext);
        loading.setContentView(R.layout.layout_loading_dialog);
        loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loading.setCancelable(false);
        avl = loading.findViewById(R.id.avl);
        avl.smoothToShow();
        loading.show();
    }

    @Override
    public void hideLoading() {
        if (loading != null && loading.isShowing()) {
            avl.smoothToHide();
            loading.hide();
        }
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
}
