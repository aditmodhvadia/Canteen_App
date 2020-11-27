package com.example.getfood.ui.loginregister.loginfragment;

import androidx.annotation.NonNull;

import com.example.firebase_api_library.listeners.OnTaskCompleteListener;
import com.example.getfood.R;
import com.example.getfood.ui.base.BasePresenter;
import com.example.getfood.utils.AppUtils;

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    public LoginPresenter() {
    }

    @Override
    public void performLogin(@NonNull String userEmail, @NonNull String password) {
        if (!AppUtils.isEmailValid(userEmail)) {
            getMvpView().valueEntryError(getMvpView().getContext().getString(R.string.enter_valid_email));
            return;
        }

        if (!AppUtils.isValidPassword(password)) {
            getMvpView().valueEntryError("Please enter a valid password more than 8 characters");
            return;
        }

        apiManager.signInWithEmailAndPassword(userEmail, password, new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
                verifyUserEmail();
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
            getMvpView().onUserEmailVerificationFailed();
        }
    }

    @Override
    public void sendEmailForVerification() {
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

    @Override
    public void performSignOut() {
        apiManager.forceSignOutUser();
    }

    @Override
    public boolean isUserEmailVerified() {
        return apiManager.isUserEmailVerified();
    }

    @Override
    public void forgotPasswordClicked(String userEmail) {
        if (!AppUtils.isEmailValid(userEmail)) {
            getMvpView().valueEntryError(getMvpView().getContext().getString(R.string.enter_valid_email));
            return;
        }

        apiManager.sendPasswordResetEmail(userEmail, new OnTaskCompleteListener() {
            @Override
            public void onTaskSuccessful() {
                getMvpView().onPasswordResetEmailSentSuccessfully();
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
}
