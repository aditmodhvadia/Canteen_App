package com.example.firebase_api_library.api.interfaces;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;

/**
 * All FireBase User related Interactions
 */
public interface FireBaseUserInterface {

    /**
     * Sign out the current FireBase User
     */
    void signOutUser();

    /**
     * Check whether the current FireBase User has email verified or not
     *
     * @return <code>true</code> if email verified, else <code>false</code>
     */
    boolean isUserVerified();

    /**
     * Call to create new user with given email and password in FireBase Auth and listen for completion events
     *
     * @param userEmail          given email
     * @param password           given password
     * @param onCompleteListener onCompleteListener
     */
    void createNewUserWithEmailPassword(String userEmail, String password, OnCompleteListener<AuthResult> onCompleteListener);

    /**
     * Call to sign in user with given email and passwor and listen for completion events
     *
     * @param userEmail          given email
     * @param password           given password
     * @param onCompleteListener onCompleteListener
     */
    void signInWithEmailAndPassword(String userEmail, String password, OnCompleteListener<AuthResult> onCompleteListener);

    /**
     * Call to reload the current Auth state of the current FireBase User
     *
     * @param onSuccessListener onSuccessListener
     * @param onFailureListener onFailureListener
     */
    void reloadCurrentUserAuthState(OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    /**
     * Call to send email verification to current FireBase User with a dynamic link with the given action code settings
     *
     * @param actionCodeSettings given action code settings
     * @param onCompleteListener onCompleteListener
     * @param onFailureListener  onFailureListener
     */
    void sendEmailVerification(ActionCodeSettings actionCodeSettings, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener);

    /**
     * Call to get email of the current FireBase User
     *
     * @return email of the current FireBase User
     */
    String getCurrentUserEmail();

    /**
     * Call to send password reset email to the given email
     *
     * @param userEmail          given email
     * @param onCompleteListener onCompleteListener
     * @param onFailureListener  onFailureListener
     */
    void sendPasswordResetEmail(String userEmail, OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener);

}
