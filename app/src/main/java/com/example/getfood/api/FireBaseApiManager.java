package com.example.getfood.api;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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

    /**
     * Call to fetch details of an Orders placed by a User
     *
     * @param rollNo        Roll Number of User
     * @param orderID       Unique Order ID
     * @param eventListener Callback for ValueEventListener
     */
    public void orderDetailListener(@NonNull String rollNo, @NonNull String orderID, final ValueEventListener eventListener) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child(BaseUrl.USER_ORDER).child(rollNo).child(orderID);

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

    /**
     * Call to fetch List of all Orders placed by a User
     *
     * @param rollNo        Roll Number of User
     * @param eventListener Callback for ValueEventListener
     */
    public void orderListListener(@NonNull String rollNo, final ValueEventListener eventListener) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child(BaseUrl.USER_ORDER).child(rollNo);

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

    public void foodMenuListener(@NonNull String category, final ValueEventListener eventListener) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child(BaseUrl.FOOD_MENU).child(category);

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

    public void forceSignOutUser() {
//        todo: log analytics event
        apiWrapper.signOutUser();
    }

    /**
     * To determine whether user has verifies their email address or not
     *
     * @return boolean value for user email verification
     */
    public boolean isUserEmailVerified() {
        return apiWrapper.isUserVerified();
    }

    public void createNewUserWithEmailPassword(String userEmail, String password, final OnCompleteListener<AuthResult> onCompleteListener) {
        apiWrapper.createNewUserWithEmailPassword(userEmail, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                onCompleteListener.onComplete(task);
            }
        });
    }

    public void signInWithEmailAndPassword(@NonNull String userEmail, @NonNull String password, final OnCompleteListener<AuthResult> onCompleteListener) {
        apiWrapper.signInWithEmailAndPassword(userEmail, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                onCompleteListener.onComplete(task);
            }
        });
    }

    public void sendEmailVerification(final OnCompleteListener<Void> onCompleteListener, final OnFailureListener onFailureListener) {
        /*ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("")
                .setIOSBundleId("")
                .build();*/
        apiWrapper.sendEmailVerification(null, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onCompleteListener.onComplete(task);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailureListener.onFailure(e);
            }
        });
    }

    public String getCurrentUserEmail() {
        return apiWrapper.getCurrentUserEmail();
    }


    public static class BaseUrl {
        // Declare the constants
        static final String USER_ORDER = "UserOrderData";
        static final String FOOD_MENU = "Food";

        @Retention(RetentionPolicy.SOURCE)
        @StringDef({USER_ORDER, FOOD_MENU})
        public @interface BaseUrlDef {
        }
    }

    public static class FoodMenuDetails {
        // Declare the constants
        public static final String AVAILABLE = "Available";
        public static final String PRICE = "Price";
        public static final String RATING = "Rating";

        @Retention(RetentionPolicy.SOURCE)
        @StringDef({AVAILABLE, PRICE, RATING})
        // Create an interface for validating String types
        public @interface FilterColorDef {
        }
    }
}
