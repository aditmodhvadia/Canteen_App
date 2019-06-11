package com.example.getfood.api;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

public interface FireBaseApiWrapperInterface {

    void singleValueEventListener(@NonNull String path, final ValueEventListener singleValueEventListener);

    void childEventListener(@NonNull String path, final ChildEventListener childEventListener);

    void valueEventListener(@NonNull String path, final ValueEventListener eventListener);
}
