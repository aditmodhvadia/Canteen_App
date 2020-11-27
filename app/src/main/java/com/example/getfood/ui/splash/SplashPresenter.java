package com.example.getfood.ui.splash;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import android.util.Log;

import com.example.firebase_api_library.listeners.DBValueEventListener;
import com.example.getfood.ui.base.BasePresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {
    public SplashPresenter() {
    }

    @Override
    public void determineIfUserLoggedIn() {
        apiManager.reloadUserAuthState(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("##DebugData", "User reload successful");
                getMvpView().userIsSignedIn();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("##DebugData", e.getMessage());
                getMvpView().userIsNotSignedIn();
            }
        });
    }

    @Override
    public String getVersionName() {
        PackageInfo pInfo;
        try {
            pInfo = getMvpView().getContext().getPackageManager().getPackageInfo(getMvpView().getContext().getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "beta-testing";
        }
    }

    @Override
    public void determineIfUpdateNeeded(String versionName) {

        apiManager.determineIfUpdateNeededAtSplash(versionName, new DBValueEventListener<String>() {
            @Override
            public void onDataChange(String data) {
                if (data != null) {
                    getMvpView().updateNotRequired();
                } else {
                    getMvpView().updateRequired();
                }
            }

            @Override
            public void onCancelled(Error error) {
                getMvpView().updateRequired();
            }
        });
    }
}
