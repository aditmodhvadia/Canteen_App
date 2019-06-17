package com.example.getfood;

public class FirebasePersistance extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
