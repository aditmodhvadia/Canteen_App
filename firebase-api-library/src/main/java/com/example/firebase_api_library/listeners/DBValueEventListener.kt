package com.example.firebase_api_library.listeners

interface DBValueEventListener<T> {
    fun onDataChange(data: T)
    fun onCancelled(error: Error?)
}