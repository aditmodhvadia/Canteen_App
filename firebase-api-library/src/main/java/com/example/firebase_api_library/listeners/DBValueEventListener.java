package com.example.firebase_api_library.listeners;

public interface DBValueEventListener<T> {

    void onDataChange(T data);

    void onCancelled(Error error);
}
