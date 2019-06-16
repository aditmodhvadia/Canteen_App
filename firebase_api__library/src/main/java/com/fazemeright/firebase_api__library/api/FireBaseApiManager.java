package com.fazemeright.firebase_api__library.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.util.Log;

import com.fazemeright.canteen_app_models.helpers.FoodMenuDetails;
import com.fazemeright.canteen_app_models.models.FoodItem;
import com.fazemeright.canteen_app_models.models.FullOrder;
import com.fazemeright.firebase_api__library.listeners.DBValueEventListener;
import com.fazemeright.firebase_api__library.listeners.OnDynamicLinkStatusListener;
import com.fazemeright.firebase_api__library.listeners.OnTaskCompleteListener;
import com.fazemeright.firebase_api__library.utils.AppUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

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
     * @param orderID              Unique Order ID
     * @param dbValueEventListener Callback for ValueEventListener
     */
    public void orderDetailListener(@NonNull String orderID, final DBValueEventListener<FullOrder> dbValueEventListener) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child(BaseUrl.USER_ORDER).child(AppUtils.getRollNoFromEmail(apiWrapper.getCurrentUserEmail())).child(orderID);

        apiWrapper.valueEventListener(dbRef, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbValueEventListener.onDataChange(dataSnapshot.getValue(FullOrder.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dbValueEventListener.onCancelled(new Error(databaseError.getMessage()));
            }
        });
    }

    /**
     * Call to fetch List of all Orders placed by a User
     *
     * @param eventListener Callback for ValueEventListener
     */
    public void orderListListener(final DBValueEventListener<ArrayList<FullOrder>> eventListener) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child(BaseUrl.USER_ORDER).child(AppUtils.getRollNoFromEmail(apiWrapper.getCurrentUserEmail()));

        apiWrapper.valueEventListener(dbRef, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<FullOrder> orderList = new ArrayList<>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    orderList.add(dsp.getValue(FullOrder.class));
                }
                eventListener.onDataChange(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                eventListener.onCancelled(new Error(databaseError.getMessage()));
            }
        });
    }

    public void foodMenuListener(@NonNull final String category, final DBValueEventListener<ArrayList<FoodItem>> eventListener) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child(BaseUrl.FOOD_MENU).child(category);

        apiWrapper.valueEventListener(dbRef, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<FoodItem> foodItems = new ArrayList<>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    FoodItem foodItem = FoodItem.fromMap(dsp.getValue(), category, dsp.getKey());

                    if (dsp.hasChild(FoodMenuDetails.AVAILABLE) &&
                            dsp.child(FoodMenuDetails.AVAILABLE).getValue().toString().equals("Yes")) {
                        foodItems.add(foodItem);
                    }
                }

                eventListener.onDataChange(foodItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                eventListener.onCancelled(new Error(databaseError.getMessage()));
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

    public void createNewUserWithEmailPassword(String userEmail, String password, final OnTaskCompleteListener onTaskCompleteListener) {
        apiWrapper.createNewUserWithEmailPassword(userEmail, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onTaskCompleteListener.onTaskSuccessful();
                } else {
                    if (task.getException() instanceof FirebaseNetworkException) {
                        onTaskCompleteListener.onTaskCompleteButFailed("No Internet");
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        onTaskCompleteListener.onTaskCompleteButFailed("Email ID is already in use");
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        onTaskCompleteListener.onTaskCompleteButFailed("Invalid Credentials");
                    } else {
                        onTaskCompleteListener.onTaskCompleteButFailed("Error Occurred");
                    }
                }
            }
        });
    }

    public void signInWithEmailAndPassword(@NonNull String userEmail, @NonNull String password, final OnTaskCompleteListener onCompleteListener) {
        apiWrapper.signInWithEmailAndPassword(userEmail, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onCompleteListener.onTaskSuccessful();
                } else {
                    if (task.getException() instanceof FirebaseNetworkException) {
                        onCompleteListener.onTaskCompleteButFailed("No Internet");
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        onCompleteListener.onTaskCompleteButFailed("Email ID is already in use");
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        onCompleteListener.onTaskCompleteButFailed("Invalid Credentials");
                    } else {
                        onCompleteListener.onTaskCompleteButFailed("Error Occurred");
                    }
                }
            }
        });
    }

    public void sendEmailVerification(final OnTaskCompleteListener onTaskCompleteListener) {

        String url = "http://getfood.page.link/verify/?email=" + getCurrentUserEmail();
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(url))
                .setDomainUriPrefix("https://getfood.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(dynamicLinkUri.toString())
                .setHandleCodeInApp(true)
                .setDynamicLinkDomain("getfood.page.link")
                .build();
        apiWrapper.sendEmailVerification(actionCodeSettings, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onTaskCompleteListener.onTaskSuccessful();
                } else {
                    if (task.getException() instanceof FirebaseNetworkException) {
                        onTaskCompleteListener.onTaskCompleteButFailed("No Internet");
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        onTaskCompleteListener.onTaskCompleteButFailed("Email ID is already in use");
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        onTaskCompleteListener.onTaskCompleteButFailed("Invalid Credentials");
                    } else {
                        onTaskCompleteListener.onTaskCompleteButFailed("Error Occurred");
                    }
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onTaskCompleteListener.onTaskFailed(e);
            }
        });
    }

    public void bindDynamicLinkEmailVerification(Intent intent, Context context,
                                                 final OnDynamicLinkStatusListener onDynamicLinkStatusListener) {
        apiWrapper.listenToDynamicLinks(intent, context, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                // Get deep link from result (may be null if no link is found)
                Uri deepLink = null;
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.getLink();
                    if (deepLink.toString().contains("verify")) {
                        Log.d("##DebugData", "Dynamic link contains verify");
                        onDynamicLinkStatusListener.onDynamicLinkFound(deepLink.toString());
                    } else {
                        Log.d("##DebugData", "Dynamic link successful but not for verify email \nLink: " + deepLink.toString());
                    }
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("##DebugData", e.getMessage());
            }
        });
    }

    public String getCurrentUserEmail() {
        return apiWrapper.getCurrentUserEmail();
    }

    public void sendPasswordResetEmail(String userEmail, final OnTaskCompleteListener onTaskCompleteListener) {
        apiWrapper.sendPasswordResetEmail(userEmail, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onTaskCompleteListener.onTaskSuccessful();
                } else {
                    if (task.getException() instanceof FirebaseNetworkException) {
                        onTaskCompleteListener.onTaskCompleteButFailed("No Internet");
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        onTaskCompleteListener.onTaskCompleteButFailed("Email ID is already in use");
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        onTaskCompleteListener.onTaskCompleteButFailed("Invalid Credentials");
                    } else {
                        onTaskCompleteListener.onTaskCompleteButFailed("Error Occurred");
                    }
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onTaskCompleteListener.onTaskFailed(e);
            }
        });
    }

    public void reloadUserAuthState(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        apiWrapper.reloadCurrentUserAuthState(onSuccessListener, onFailureListener);
    }

    public void determineIfUpdateNeededAtSplash(@NonNull String versionName, final DBValueEventListener<String> eventListener) {
        DatabaseReference versionCheck = FirebaseDatabase.getInstance().getReference().child(BaseUrl.VERSION_CHECK).child(versionName);

        apiWrapper.singleValueEventListener(versionCheck, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventListener.onDataChange(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                eventListener.onCancelled(new Error(databaseError.getMessage()));
            }
        });
    }

    public String getNewOrderKey() {
        return apiWrapper.getKey(FirebaseDatabase.getInstance().getReference().child(BaseUrl.USER_ORDER)
                .child(AppUtils.getRollNoFromEmail(apiWrapper.getCurrentUserEmail())));
    }

    public void setOrderValue(FullOrder fullOrder, final OnTaskCompleteListener onCompleteListener) {
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child(BaseUrl.USER_ORDER)
                .child(AppUtils.getRollNoFromEmail(apiWrapper.getCurrentUserEmail())).child(fullOrder.getOrderId());
        apiWrapper.setValue(orderReference, fullOrder, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    onCompleteListener.onTaskSuccessful();
                } else {
                    if (task.getException() instanceof FirebaseNetworkException) {
                        onCompleteListener.onTaskCompleteButFailed("No Internet");
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        onCompleteListener.onTaskCompleteButFailed("Email ID is already in use");
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        onCompleteListener.onTaskCompleteButFailed("Invalid Credentials");
                    } else {
                        onCompleteListener.onTaskCompleteButFailed("Error Occurred");
                    }
                }
            }
        });
    }

    public void getNewOrderData(String orderId, ValueEventListener eventListener) {
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child(BaseUrl.USER_ORDER)
                .child(AppUtils.getRollNoFromEmail(apiWrapper.getCurrentUserEmail())).child(orderId);
        apiWrapper.singleValueEventListener(orderReference, eventListener);
    }

    public boolean isUserLoggedIn() {
        return getCurrentUserEmail() != null;
    }


    public static class BaseUrl {
        // Declare the constants
        static final String USER_ORDER = "UserOrderData";
        static final String FOOD_MENU = "Food";
        static final String VERSION_CHECK = "version-check";

        @Retention(RetentionPolicy.SOURCE)
        @StringDef({USER_ORDER, FOOD_MENU})
        public @interface BaseUrlDef {
        }
    }

}
