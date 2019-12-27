package com.example.getfood.ui.loginregister.registerfragment;

import androidx.annotation.NonNull;
import android.util.Log;

import com.example.getfood.ui.base.BasePresenter;
import com.example.getfood.utils.AppUtils;
import com.fazemeright.firebase_api__library.listeners.OnTaskCompleteListener;

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

        apiManager.createNewUserWithEmailPassword(userEmail, password, new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
                Log.d("##DebugData", String.valueOf(apiManager.isUserEmailVerified()));
                Log.d("##DebugData", apiManager.getCurrentUserEmail());
                getMvpView().onUserCreatedSuccessfully();
            }

            @Override
            public void onTaskCompleteButFailed(String errMsg) {
                getMvpView().valueEntryError(errMsg);
            }

            @Override
            public void onTaskFailed(Exception e) {

            }
        });
    }

    @Override
    public void verifyUserEmail() {
        if (apiManager.isUserEmailVerified()) {
            getMvpView().userVerifiedSuccessfully();
        } else {
            apiManager.sendEmailVerification(new OnTaskCompleteListener() {
                @Override
                public void onTaskSuccessful() {
                    getMvpView().userVerifiedSuccessfully();
                }

                @Override
                public void onTaskCompleteButFailed(String errMsg) {
                    getMvpView().valueEntryError(errMsg);
                }

                @Override
                public void onTaskFailed(Exception e) {
                    getMvpView().onUserEmailVerificationFailed();
                }
            });
        }
    }

    @Override
    public boolean isTermsAndConditionChecked(boolean checked) {
        return checked;
    }

    @Override
    public void signOutUser() {
        apiManager.forceSignOutUser();
    }
}
