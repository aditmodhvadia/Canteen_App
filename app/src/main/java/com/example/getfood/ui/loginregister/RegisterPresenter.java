package com.example.getfood.ui.loginregister;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.getfood.R;
import com.example.getfood.ui.base.BasePresenter;
import com.example.getfood.utils.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterPresenter<V extends RegisterMvpView> extends BasePresenter<V> implements RegisterMvpPresenter<V> {

    public RegisterPresenter() {
    }

    @Override
    public void performRegistration(@NonNull final String userEmail, @NonNull final String password, String confirmPassword) {
        //Validating all entries First

        if (!AppUtils.isEmailValid(userEmail)) {
            getMvpView().valueEntryError("Please enter a valid Nirma University Email Address");
            return;
        }

        if (!password.equals(confirmPassword)) {
            getMvpView().valueEntryError("Both passwords should match");
            return;
        }

        if (!AppUtils.isValidPassword(password)) {
            getMvpView().valueEntryError("Please enter a valid password more than 8 characters");
            return;
        }

        apiManager.createNewUserWithEmailPassword(userEmail, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("##DebugData", String.valueOf(apiManager.isUserEmailVerified()));
                    Log.d("##DebugData", apiManager.getCurrentUserEmail());
                    getMvpView().onUserCreatedSuccessfully();
                } else {
                    if (task.getException() instanceof FirebaseNetworkException) {
                        getMvpView().valueEntryError(getMvpView().getContext().getString(R.string.no_internet));
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        getMvpView().valueEntryError(getMvpView().getContext().getString(R.string.email_in_use));
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        getMvpView().valueEntryError(getMvpView().getContext().getString(R.string.invalid_credentials));
                    } else {
                        Log.d("##DebugDataNew", task.getException().getMessage());
                        getMvpView().valueEntryError(getMvpView().getContext().getString(R.string.error_occurred));
                    }
                }
            }
        });
    }

    @Override
    public void verifyUserEmail() {
        if (apiManager.isUserEmailVerified()) {
            getMvpView().userVerifiedSuccessfully();
        } else {
            apiManager.sendEmailVerification(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        getMvpView().userVerifiedSuccessfully();
                    } else {
                        if (task.getException() instanceof FirebaseNetworkException) {
                            getMvpView().valueEntryError(getMvpView().getContext().getString(R.string.no_internet));
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            getMvpView().valueEntryError(getMvpView().getContext().getString(R.string.email_in_use));
                        } else {
                            Log.d("##DebugDataNew", task.getException().getMessage());
                            getMvpView().valueEntryError(getMvpView().getContext().getString(R.string.error_occurred));
                        }
                    }
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getMvpView().onUserEmailVerificationFailed();
                }
            });
        }
    }

    @Override
    public void signOutUser() {
        apiManager.forceSignOutUser();
    }
}
