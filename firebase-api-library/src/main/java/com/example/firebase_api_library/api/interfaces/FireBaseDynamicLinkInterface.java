package com.example.firebase_api_library.api.interfaces;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

/**
 * All FireBase Dynamic Links related Interactions
 */
public interface FireBaseDynamicLinkInterface {

    /**
     * Call to listen to dynamic Link interceptions for the given intent
     *
     * @param intent            given intent
     * @param context           given context
     * @param onSuccessListener onSuccessListener
     * @param onFailureListener onFailureListener
     */
    void listenToDynamicLinks(Intent intent, Context context, OnSuccessListener<PendingDynamicLinkData> onSuccessListener, OnFailureListener onFailureListener);

}
