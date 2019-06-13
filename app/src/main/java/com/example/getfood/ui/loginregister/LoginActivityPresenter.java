package com.example.getfood.ui.loginregister;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.getfood.R;
import com.example.getfood.ui.base.BasePresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class LoginActivityPresenter<V extends LoginActivityMvpView> extends BasePresenter<V> implements LoginActivityMvpPresenter<V> {

    public LoginActivityPresenter() {
    }

    @Override
    public void bindDynamicLinkEmailVerification(Intent intent) {
        apiManager.bindDynamicLinkEmailVerification(intent, getMvpView().getContext(), new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                // Get deep link from result (may be null if no link is found)
                Uri deepLink = null;
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.getLink();
                    if (deepLink.toString().contains("verify")) {
                        Log.d("##DebugData", "Dynamic link contains verify");
                        final String userEmail = deepLink.toString().split("email=")[1];
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

                    } else {
                        Log.d("##DebugData", "Dynamic link successful but not for verify email \nLink: " + deepLink.toString());
                    }
                }

                if (deepLink != null) {
                    Log.d("##DebugData", "Dynamic link successful" + deepLink.toString());
                } else {
                    Log.d("##DebugData", "Dynamic link successful and deep link null");
                }

                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...

                // ...
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("##DebugData", "getDynamicLink:onFailure", e);
            }
        });
    }
}
