package com.example.getfood.utils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.example.getfood.R;
import com.fazemeright.canteen_app_models.models.CartItem;
import com.fazemeright.canteen_app_models.models.FoodItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AppUtils {

    private static AppUtils mInstance;

    /**
     * Call to initialize App Variables
     *
     * @param context
     * @return Instance of AppUtils class
     */
    public static synchronized AppUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppUtils();
        }
        return mInstance;
    }

    /**
     * Call to display a SnackBar in the bottom of the screen with the message.
     *
     * @param context Context over which SnackBar will be drawn
     * @param msg     Message to be shown
     * @return
     */
    public static Snackbar getSnackBar(@NonNull Context context, String msg) {
        View view = ((Activity) context).findViewById(R.id.CoordinatorLayoutParent);
        return Snackbar.make(view, msg,
                Snackbar.LENGTH_LONG);
    }

    /**
     * Call to create notification channel for Order Display
     *
     * @param context Current context
     */
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

    /**
     * Call to get today's date in full month format
     *
     * @return Current Date
     */
    public static String getTodaysDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy", Locale.US);
        return df.format(date);
    }

    public static int getColorForRating(Context context, String itemRating) {
        int ratingColor;
        if (Float.valueOf(itemRating) < 2.0) {
            ratingColor = context.getResources().getColor(R.color.colorBadRating);
        } else if (Float.valueOf(itemRating) < 3.5) {
            ratingColor = context.getResources().getColor(R.color.colorMediumRating);
        } else {
            ratingColor = context.getResources().getColor(R.color.colorGoodRating);
        }
        return ratingColor;
    }

    /**
     * Call to determine whether FoodItem is present in List of CartItems
     *
     * @param cartItems List of Cart Items where item is to be found
     * @param foodItem  Item to be found in the Cart List
     * @return position of the food item if found in cart or else -1
     */
    public static int isItemInCart(ArrayList<CartItem> cartItems, FoodItem foodItem) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getItemName().equals(foodItem.getItemName())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isEmailValid(String userEmail) {
        if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            return false;
        }
        return userEmail.split("@")[1].equals("nirmauni.ac.in");
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 8;
    }

    public static String getRollNoFromEmail(String currentUserEmail) {
        if (currentUserEmail == null) {
            return null;
        } else {
            return currentUserEmail.substring(0, currentUserEmail.indexOf("@"));
        }
    }

    public String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
