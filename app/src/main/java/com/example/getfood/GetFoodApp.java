package com.example.getfood;

import android.app.Application;

import timber.log.Timber;

public class GetFoodApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
