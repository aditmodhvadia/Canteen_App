package com.example.getfood.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.firebase_api_library.api.FireBaseApiManager.Companion.instance
import com.example.firebase_api_library.listeners.OnTaskCompleteListener
import com.example.firebase_api_library.utils.AppUtils.createNotificationChannel
import com.example.getfood.R
import com.example.getfood.ui.orderlist.OrderListActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFireBaseNotificationService constructor() : FirebaseMessagingService() {
    private val tag: String = "##Fcm"
    public override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...
//        createNotification()
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(tag, "From: " + remoteMessage.getFrom())

        // Check if message contains a data payload.
        if (remoteMessage.getData().size > 0) {
            Log.d(tag, "Message data payload: " + remoteMessage.getData())
            if ( /* Check if data needs to be processed by long running job */true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
                Log.d(tag, "Notification generated with size greater than 0")
                newSendNotification(remoteMessage)
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }
        } else {
            Log.d(tag, "Notification generated with not greater than 0")
            newSendNotification(remoteMessage)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(tag, "Message Notification Body: " + remoteMessage.getNotification()!!.getBody())
            //            Log.d(tag, "Notification generated");
//            newSendNotification(remoteMessage);
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    public override fun onNewToken(token: String) {
        Log.d(tag, "Refreshed token: " + token)
        instance!!.updateToken(token, object : OnTaskCompleteListener {
            public override fun onTaskSuccessful() {}
            public override fun onTaskCompleteButFailed(errMsg: String?) {}
            public override fun onTaskFailed(e: Exception?) {}
        })

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    private fun newSendNotification(remoteMessage: RemoteMessage) {
        createNotificationChannel(getApplicationContext())
        val configureIntent: Intent = Intent(getApplicationContext(), OrderListActivity::class.java)
        //        configureIntent.putExtra("data", remoteMessage.getData().get("testkey"));
        configureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        configureIntent.setFlags((Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        configureIntent.setAction("dummy_unique_action_identifyer" + "123123")
        val pendingIntent: PendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, configureIntent,
                PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this)
        notificationBuilder.setSmallIcon(R.drawable.mykanteenicon) // TODO: Replace with btn_clear
                .setContentTitle(remoteMessage.getNotification()!!.getTitle())
                .setContentText(remoteMessage.getNotification()!!.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}