package com.example.firebase_api_library.listeners

interface OnTaskCompleteListener {
    fun onTaskSuccessful()
    fun onTaskCompleteButFailed(errMsg: String?)
    fun onTaskFailed(e: Exception?)
}