package com.example.getfood.ui.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getfood.utils.ProgressHandler;

public abstract class BaseFragment extends Fragment implements BaseView {

    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        initViews(view);
        setListeners(view);
        return view;
    }

    /**
     * To get layout resource id
     */
    public abstract @LayoutRes
    int getLayoutResId();

    /**
     * To initialize views of activity
     */
    public abstract void initViews(View view);

    /**
     * To set listeners of view or callback
     *
     * @param view
     */
    public abstract void setListeners(View view);

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
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    @Nullable
    @Override
    public Context getContext() {
        return mContext;
    }
}
