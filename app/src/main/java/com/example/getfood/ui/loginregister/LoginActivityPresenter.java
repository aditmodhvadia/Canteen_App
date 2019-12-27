package com.example.getfood.ui.loginregister;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.util.Log;

import com.example.firebase_api_library.listeners.OnDynamicLinkStatusListener;
import com.example.firebase_api_library.listeners.OnTaskCompleteListener;
import com.example.getfood.R;
import com.example.getfood.ui.base.BasePresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LoginActivityPresenter<V extends LoginActivityMvpView> extends BasePresenter<V> implements LoginActivityMvpPresenter<V> {

    public LoginActivityPresenter() {
    }

    @Override
    public void bindDynamicLinkEmailVerification(Intent intent) {
        apiManager.bindDynamicLinkEmailVerification(intent, getMvpView().getContext(), new OnDynamicLinkStatusListener() {
            @Override
            public void onDynamicLinkFound(String link) {
                final String userEmail = link.split("email=")[1];
//                            TODO: Sign in user with the email id and then call alert dialog
//                            TODO: Check if email is same as the one with currently logged in
                Log.d("##DebugData", userEmail);
                apiManager.reloadUserAuthState(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                                User is signed in
                        if (apiManager.getCurrentUserEmail() != null && apiManager.getCurrentUserEmail().contains(userEmail)) {
                            Log.d("##DebugData", "Current user email and dynamic link email match");
                            if (apiManager.isUserEmailVerified()) {
                                Log.d("##DebugData", "current user now email verified");
                                getMvpView().onSuccessfulVerificationAndSignIn();
                            } else {
                                Log.d("##DebugData", "current user not email verified");
                                getMvpView().onFailedVerificationOrSignIn(new Exception(
                                        getMvpView().getContext().getString(R.string.common_err_msg)));
                            }
                        } else {
                            Log.d("##DebugData", "Current user email and dynamic link email do not match");
                            Log.d("##DebugData", userEmail);
                            Log.d("##DebugData", apiManager.getCurrentUserEmail());
                            getMvpView().onFailedVerificationOrSignIn(new Exception(
                                    getMvpView().getContext().getString(R.string.common_err_msg)));
                        }
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getMvpView().onFailedVerificationOrSignIn(e);
                    }
                });
            }
        });
    }

    @Override
    public void updateToken(String token) {
        apiManager.updateToken(token, new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
                getMvpView().onTokenUpdatedSuccessfully();
            }

            @Override
            public void onTaskCompleteButFailed(String errMsg) {

            }

            @Override
            public void onTaskFailed(Exception e) {

            }
        });
    }

    @Override
    public boolean isUserLoggedIn() {
        return apiManager.isUserLoggedIn();
    }

    @Override
    public void signOutUser() {
        apiManager.forceSignOutUser();
    }
}
