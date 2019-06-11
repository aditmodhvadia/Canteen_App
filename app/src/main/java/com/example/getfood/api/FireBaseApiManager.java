package com.example.getfood.api;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseApiManager {

    private static FireBaseApiManager apiManager;
    private static FireBaseApiWrapper apiWrapper;

    public static FireBaseApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new FireBaseApiManager();
        }
        if (apiWrapper == null) {
            apiWrapper = FireBaseApiWrapper.getInstance();
        }
        return apiManager;
    }

//    TODO: Implement common call/listener for all FireBase RealTime Database listeners first and
//     then write calls and all FireBase APIs subsequently


    public void orderDetailListener(@NonNull String rollNo, @NonNull String orderID, final ValueEventListener eventListener) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child(BaseUrl.USER_ORDER.url).child(rollNo).child(orderID);

        apiWrapper.valueEventListener(dbRef, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventListener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                eventListener.onCancelled(databaseError);
            }
        });
    }

    enum BaseUrl {
        USER_ORDER("UserOrderData"),
        DEV("https://dev.domain.com:21323/");

        private String url;

        BaseUrl(String path) {
            this.url = path;
        }

        public String getUrl() {
            return url;
        }
    }
}
