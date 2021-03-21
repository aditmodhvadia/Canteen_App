package com.example.firebase_api_library.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object AppUtils {

    fun getRollNoFromEmail(currentUserEmail: String?): String? {
        return currentUserEmail?.substring(0, currentUserEmail.indexOf("@"))
    }

    @JvmStatic
    fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Order Updates"
            val description = "Whenever Order is updated"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("order-update", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}