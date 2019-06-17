package com.fazemeright.firebase_api__library.listeners;

public interface OnTaskCompleteListener {

    void onTaskSuccessful();

    void onTaskCompleteButFailed(String errMsg);

    void onTaskFailed(Exception e);
}
