package com.example.getfood.ui.splash;

import com.example.getfood.ui.base.BaseView;

public interface SplashMvpView extends BaseView {
    void userIsSignedIn();

    void userIsNotSignedIn();

    void updateNotRequired();

    void updateRequired();
}
