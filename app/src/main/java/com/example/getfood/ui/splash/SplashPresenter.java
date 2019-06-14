package com.example.getfood.ui.splash;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.getfood.R;
import com.example.getfood.ui.base.BasePresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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

        apiManager.determineIfUpdateNeededAtSplash(versionName, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null &&
                        dataSnapshot.getValue().toString().equals(getMvpView().getContext().getString(R.string.yes))) {
                    Log.d("##DebugData", "Update not required");
                    getMvpView().updateNotRequired();
                } else {
                    //deprecated versionName of app or version name not found
                    getMvpView().updateRequired();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    getMvpView().updateRequired();
            }
        });
    }
}
