package com.example.firebase_api_library.api.interfaces;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * All FireBase Real Time Database Interaction methods
 */
public interface FireBaseRealTimeDatabaseInterface {

    /**
     * Call to listen for changes in given database reference for once
     *
     * @param mDatabaseReference       given database reference
     * @param singleValueEventListener value event listener
     */
    void singleValueEventListener(@NonNull DatabaseReference mDatabaseReference, final ValueEventListener singleValueEventListener);

    /**
     * Call to listen to all events of the child of the given database reference
     *
     * @param mDatabaseReference given database reference
     * @param childEventListener value event listener
     */
    void childEventListener(@NonNull DatabaseReference mDatabaseReference, final ChildEventListener childEventListener);

    /**
     * Call to listen to all events for the given database reference
     *
     * @param mDatabaseReference given reference
     * @param eventListener      value event listener
     */
    void valueEventListener(@NonNull DatabaseReference mDatabaseReference, final ValueEventListener eventListener);

    /**
     * Call to get key of the given database reference
     *
     * @param reference given database reference
     * @return key for the database reference
     */
    String getKey(DatabaseReference reference);

    /**
     * Call to set the given value at the given database reference
     *
     * @param reference          given reference
     * @param object             given value
     * @param onCompleteListener onCompleteListener
     */
    void setValue(DatabaseReference reference, Object object, OnCompleteListener<Void> onCompleteListener);
}
