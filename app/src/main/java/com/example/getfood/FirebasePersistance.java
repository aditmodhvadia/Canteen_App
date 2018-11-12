package com.example.getfood;

import com.google.firebase.database.FirebaseDatabase;

public class FirebasePersistance extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
