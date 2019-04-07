package com.example.getfood.utils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.getfood.R;

public class AppUtils {

    public static Snackbar getSnackbar(Context context, String msg) {
        View view = ((Activity) context).findViewById(R.id.CoordinatorLayoutParent);
        return Snackbar.make(view, msg,
                Snackbar.LENGTH_LONG);
    }

    public static void createNotificationChannel(Context context) {

//        create notification channel only for Builds greater than Oreo(8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.order_channel);
            String description = context.getString(R.string.primary_display_notif);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.notif_channel), name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
