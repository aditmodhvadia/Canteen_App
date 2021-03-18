package com.example.firebase_api_library.api

import android.content.Context
import android.content.Intent
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.dynamiclinks.PendingDynamicLinkData

interface FireBaseApiWrapperInterface {
    //    Database read functions
    fun singleValueEventListener(mDatabaseReference: DatabaseReference, singleValueEventListener: ValueEventListener?)
    fun childEventListener(mDatabaseReference: DatabaseReference, childEventListener: ChildEventListener?)
    fun valueEventListener(mDatabaseReference: DatabaseReference, eventListener: ValueEventListener?)

    //    FireBase Auth functions
    fun signOutUser()
    val isUserVerified: Boolean
    fun createNewUserWithEmailPassword(userEmail: String?, password: String?, onCompleteListener: OnCompleteListener<AuthResult?>?)
    fun signInWithEmailAndPassword(userEmail: String, password: String, onCompleteListener: OnCompleteListener<AuthResult?>?)
    fun sendEmailVerification(actionCodeSettings: ActionCodeSettings?, onCompleteListener: OnCompleteListener<Void?>?, onFailureListener: OnFailureListener)
    val currentUserEmail: String?
    fun sendPasswordResetEmail(userEmail: String?, onCompleteListener: OnCompleteListener<Void?>?, onFailureListener: OnFailureListener?)
    fun listenToDynamicLinks(intent: Intent?, context: Context?, onSuccessListener: OnSuccessListener<PendingDynamicLinkData?>?, onFailureListener: OnFailureListener?)
    fun reloadCurrentUserAuthState(onSuccessListener: OnSuccessListener<Void?>?, onFailureListener: OnFailureListener)
    fun getKey(reference: DatabaseReference): String?
    operator fun setValue(reference: DatabaseReference, `object`: Any?, onCompleteListener: OnCompleteListener<Void?>?)
}