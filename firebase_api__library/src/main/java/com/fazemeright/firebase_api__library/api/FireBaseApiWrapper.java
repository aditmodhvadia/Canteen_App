package com.fazemeright.firebase_api__library.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

/**
 * Keep this as a Singleton Class
 */
class FireBaseApiWrapper implements FireBaseApiWrapperInterface {

    private static FireBaseApiWrapper apiWrapper;
    private static DatabaseReference mDatabase;
//    todo: create a boolean for logging and enable it for dev/debug build flavors or changeable

    /**
     * Call to initialize various variables
     *
     * @return return instance of @{@link FireBaseApiWrapper}
     */
    static FireBaseApiWrapper getInstance() {
        if (apiWrapper == null) {
            apiWrapper = new FireBaseApiWrapper();
        }
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return apiWrapper;
    }


    /**
     * Add a listener for child events occurring at this location. When child locations are added, removed, changed, or moved, the listener will be triggered for the appropriate event
     * Parameters
     * listener
     *
     * @param mDatabaseReference Path to which Child events to listen to
     * @param childEventListener The listener to be called with changes
     *                           Return A reference to the listener provided. Save this to remove the listener later.
     */
    @Override
    public void childEventListener(@NonNull DatabaseReference mDatabaseReference, ChildEventListener childEventListener) {
        mDatabaseReference.addChildEventListener(childEventListener);
    }

    @Override
    public void valueEventListener(@NonNull DatabaseReference mDatabaseReference, ValueEventListener eventListener) {
        mDatabaseReference.addValueEventListener(eventListener);

    }

    /**
     * @param mDatabaseReference
     * @param singleValueEventListener
     */
    @Override
    public void singleValueEventListener(@NonNull DatabaseReference mDatabaseReference, ValueEventListener singleValueEventListener) {
        mDatabaseReference.addListenerForSingleValueEvent(singleValueEventListener);
    }

    /**
     * Signs out user
     */
    @Override
    public void signOutUser() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void createNewUserWithEmailPassword(String userEmail, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(onCompleteListener);
    }

    @Override
    public void signInWithEmailAndPassword(@NonNull String userEmail, @NonNull String password, OnCompleteListener<AuthResult> onCompleteListener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(onCompleteListener);
    }

    @Override
    public String getCurrentUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getEmail() : null;
    }

    @Override
    public void sendEmailVerification(ActionCodeSettings actionCodeSettings,
                                      OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification(actionCodeSettings)
                    .addOnCompleteListener(onCompleteListener)
                    .addOnFailureListener(onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("User not logged in"));
        }
    }

    @Override
    public void sendPasswordResetEmail(String userEmail, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);
    }

    @Override
    public boolean isUserVerified() {
        return FirebaseAuth.getInstance().getCurrentUser() != null &&
                FirebaseAuth.getInstance().getCurrentUser().isEmailVerified();
    }

    // listen to FireBase dynamic links

    @Override
    public void listenToDynamicLinks(Intent intent, Context context, final OnSuccessListener<PendingDynamicLinkData> onSuccessListener, final OnFailureListener onFailureListener) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener((Activity) context, onSuccessListener)
                .addOnFailureListener((Activity) context, onFailureListener);
    }

    @Override
    public void reloadCurrentUserAuthState(final OnSuccessListener<Void> onSuccessListener, final OnFailureListener onFailureListener) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().getCurrentUser().reload()
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Some Error Occurred, Try again later"));
        }
    }

    @Override
    public String getKey(DatabaseReference reference) {
        return reference.push().getKey();
    }

    @Override
    public void setValue(DatabaseReference reference, Object object, OnCompleteListener<Void> onCompleteListener) {
        reference.setValue(object).addOnCompleteListener(onCompleteListener);
    }
}
