package com.example.firebase_api_library.api

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData

/**
 * Keep this as a Singleton Class
 */
internal class FireBaseApiWrapper : FireBaseApiWrapperInterface {
    /**
     * Add a listener for child events occurring at this location. When child locations are added, removed, changed, or moved, the listener will be triggered for the appropriate event
     * Parameters
     * listener
     *
     * @param mDatabaseReference Path to which Child events to listen to
     * @param childEventListener The listener to be called with changes
     * Return A reference to the listener provided. Save this to remove the listener later.
     */
    override fun childEventListener(mDatabaseReference: DatabaseReference, childEventListener: ChildEventListener?) {
        mDatabaseReference.addChildEventListener(childEventListener!!)
    }

    override fun valueEventListener(mDatabaseReference: DatabaseReference, eventListener: ValueEventListener?) {
        mDatabaseReference.addValueEventListener(eventListener!!)
    }

    /**
     * @param mDatabaseReference
     * @param singleValueEventListener
     */
    override fun singleValueEventListener(mDatabaseReference: DatabaseReference, singleValueEventListener: ValueEventListener?) {
        mDatabaseReference.addListenerForSingleValueEvent(singleValueEventListener!!)
    }

    /**
     * Signs out user
     */
    override fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun createNewUserWithEmailPassword(userEmail: String?, password: String?, onCompleteListener: OnCompleteListener<AuthResult?>?) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(onCompleteListener!!)
    }

    override fun signInWithEmailAndPassword(userEmail: String, password: String, onCompleteListener: OnCompleteListener<AuthResult?>?) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(onCompleteListener!!)
    }

    override val currentUserEmail: String?
        get() = if (FirebaseAuth.getInstance().currentUser != null) FirebaseAuth.getInstance().currentUser.email else null

    override fun sendEmailVerification(actionCodeSettings: ActionCodeSettings?,
                                       onCompleteListener: OnCompleteListener<Void?>?, onFailureListener: OnFailureListener) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().currentUser.sendEmailVerification(actionCodeSettings)
                    .addOnCompleteListener(onCompleteListener!!)
                    .addOnFailureListener(onFailureListener)
        } else {
            onFailureListener.onFailure(Exception("User not logged in"))
        }
    }

    override fun sendPasswordResetEmail(userEmail: String?, onCompleteListener: OnCompleteListener<Void?>?, onFailureListener: OnFailureListener?) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(onCompleteListener!!)
                .addOnFailureListener(onFailureListener!!)
    }

    override val isUserVerified: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null &&
                FirebaseAuth.getInstance().currentUser.isEmailVerified

    // listen to FireBase dynamic links
    override fun listenToDynamicLinks(intent: Intent?, context: Context?, onSuccessListener: OnSuccessListener<PendingDynamicLinkData?>?, onFailureListener: OnFailureListener?) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent!!)
                .addOnSuccessListener((context as Activity?)!!, onSuccessListener!!)
                .addOnFailureListener(context!!, onFailureListener!!)
    }

    override fun reloadCurrentUserAuthState(onSuccessListener: OnSuccessListener<Void?>?, onFailureListener: OnFailureListener) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().currentUser.reload()
                    .addOnSuccessListener(onSuccessListener!!)
                    .addOnFailureListener(onFailureListener)
        } else {
            onFailureListener.onFailure(Exception("Some Error Occurred, Try again later"))
        }
    }

    override fun getKey(reference: DatabaseReference): String? {
        return reference.push().key
    }

    override fun setValue(reference: DatabaseReference, `object`: Any?, onCompleteListener: OnCompleteListener<Void?>?) {
        reference.setValue(`object`).addOnCompleteListener(onCompleteListener!!)
    }

    companion object {
        private var apiWrapper: FireBaseApiWrapper? = null
        private var mDatabase: DatabaseReference? = null
        //    todo: create a boolean for logging and enable it for dev/debug build flavors or changeable
        /**
         * Call to initialize various variables
         *
         * @return return instance of @[FireBaseApiWrapper]
         */
        val instance: FireBaseApiWrapper?
            get() {
                if (apiWrapper == null) {
                    apiWrapper = FireBaseApiWrapper()
                }
                if (mDatabase == null) {
                    mDatabase = FirebaseDatabase.getInstance().reference
                }
                return apiWrapper
            }
    }
}