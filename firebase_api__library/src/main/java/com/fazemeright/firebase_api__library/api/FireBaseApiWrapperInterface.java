package com.fazemeright.firebase_api__library.api;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public interface FireBaseApiWrapperInterface {

    //    Database read functions
    void singleValueEventListener(@NonNull DatabaseReference mDatabaseReference, final ValueEventListener singleValueEventListener);

    void childEventListener(@NonNull DatabaseReference mDatabaseReference, final ChildEventListener childEventListener);

    void valueEventListener(@NonNull DatabaseReference mDatabaseReference, final ValueEventListener eventListener);

    //    FireBase Auth functions
    void signOutUser();

    boolean isUserVerified();

    void createNewUserWithEmailPassword(String userEmail, String password, OnCompleteListener<AuthResult> onCompleteListener);

    void signInWithEmailAndPassword(String userEmail, String password, OnCompleteListener<AuthResult> onCompleteListener);

    void sendEmailVerification(ActionCodeSettings actionCodeSettings, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener);

    String getCurrentUserEmail();

    void sendPasswordResetEmail(String userEmail, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener);

    void listenToDynamicLinks(Intent intent, Context context, OnSuccessListener<PendingDynamicLinkData> onSuccessListener, OnFailureListener onFailureListener);

    void reloadCurrentUserAuthState(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    String getKey(DatabaseReference reference);

    void setValue(DatabaseReference reference, Object object, OnCompleteListener<Void> onCompleteListener);
}
