package com.example.getfood.api;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public interface FireBaseApiWrapperInterface {

    void singleValueEventListener(@NonNull DatabaseReference mDatabaseReference, final ValueEventListener singleValueEventListener);

    void childEventListener(@NonNull DatabaseReference mDatabaseReference, final ChildEventListener childEventListener);

    void valueEventListener(@NonNull DatabaseReference mDatabaseReference, final ValueEventListener eventListener);
}
