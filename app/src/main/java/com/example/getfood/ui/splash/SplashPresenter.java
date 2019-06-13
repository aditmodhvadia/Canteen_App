package com.example.getfood.ui.splash;

import android.support.annotation.NonNull;
import android.util.Log;

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
}
