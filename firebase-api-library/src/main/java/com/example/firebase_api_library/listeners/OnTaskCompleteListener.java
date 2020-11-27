package com.example.firebase_api_library.listeners;

public interface OnTaskCompleteListener {

    void onTaskSuccessful();

    void onTaskCompleteButFailed(String errMsg);

    void onTaskFailed(Exception e);
}
