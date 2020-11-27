package com.example.getfood.ui.loginregister.registerfragment;

import androidx.annotation.NonNull;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface RegisterMvpPresenter<V extends RegisterMvpView> extends BaseMvpPresenter<V> {
    void performRegistration(@NonNull String userEmail, @NonNull String password, String confirmPassword);

    void verifyUserEmail();

    void signOutUser();

    boolean isTermsAndConditionChecked(boolean checked);
}
