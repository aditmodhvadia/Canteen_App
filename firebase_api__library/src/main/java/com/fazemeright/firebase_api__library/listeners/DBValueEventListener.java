package com.fazemeright.firebase_api__library.listeners;

public interface DBValueEventListener<T> {

    void onDataChange(T data);

    void onCancelled(Error error);
}
