package com.example.getfood.utils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.getfood.R;

public class AppUtils {

    private static AppUtils mInstance;

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

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static AppUtils getInstance() {
        if (mInstance == null) {
            mInstance = new AppUtils();
        }
        return mInstance;
    }
}
