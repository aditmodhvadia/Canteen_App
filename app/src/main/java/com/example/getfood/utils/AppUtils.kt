package com.example.getfood.utils

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import com.example.canteen_app_models.models.CartItem
import com.example.canteen_app_models.models.FoodItem
import com.example.getfood.R
import com.google.android.material.snackbar.Snackbar
import java.lang.Float
import java.text.SimpleDateFormat
import java.util.*

class AppUtils constructor() {
    fun generateString(): String {
        val uuid: String = UUID.randomUUID().toString()
        return uuid.replace("-".toRegex(), "")
    }

    companion object {
        private var mInstance: AppUtils? = null

        /**
         * Call to initialize App Variables
         *
         * @param context
         * @return Instance of AppUtils class
         */
        @Synchronized
        fun getInstance(context: Context?): AppUtils? {
            if (mInstance == null) {
                mInstance = AppUtils()
            }
            return mInstance
        }

        /**
         * Call to display a SnackBar in the bottom of the screen with the message.
         *
         * @param context Context over which SnackBar will be drawn
         * @param msg     Message to be shown
         * @return
         */
        fun getSnackBar(context: Context, msg: String?): Snackbar {
            val view: View = (context as Activity).findViewById(R.id.CoordinatorLayoutParent)
            return Snackbar.make(view, (msg)!!,
                    Snackbar.LENGTH_LONG)
        }

        /**
         * Call to create notification channel for Order Display
         *
         * @param context Current context
         */
        fun createNotificationChannel(context: Context) {

//        create notification channel only for Builds greater than Oreo(8.0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = context.getString(R.string.order_channel)
                val description: String = context.getString(R.string.primary_display_notif)
                val importance: Int = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(context.getString(R.string.notif_channel), name, importance)
                channel.setDescription(description)
                val notificationManager: NotificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }

        /**
         * Call to get today's date in full month format
         *
         * @return Current Date
         */
        val todaysDate: String
            get() {
                val date: Date = Calendar.getInstance().getTime()
                val df = SimpleDateFormat("dd-MMMM-yyyy", Locale.US)
                return df.format(date)
            }

        fun getColorForRating(context: Context, itemRating: String?): Int {
            return when {
                Float.valueOf(itemRating) < 2.0 -> {
                    context.resources.getColor(R.color.colorBadRating)
                }
                Float.valueOf(itemRating) < 3.5 -> {
                    context.resources.getColor(R.color.colorMediumRating)
                }
                else -> {
                    context.resources.getColor(R.color.colorGoodRating)
                }
            }
        }

        /**
         * Call to determine whether FoodItem is present in List of CartItems
         *
         * @param cartItems List of Cart Items where item is to be found
         * @param foodItem  Item to be found in the Cart List
         * @return position of the food item if found in cart or else -1
         */
        fun isItemInCart(cartItems: MutableList<CartItem>, foodItem: FoodItem?): Int {
            for (i in cartItems.indices) {
                if ((cartItems.get(i).itemName == foodItem!!.itemName)) {
                    return i
                }
            }
            return -1
        }

        fun isEmailValid(userEmail: String): Boolean {
            if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                return false
            }
            return (userEmail.split("@".toRegex()).toTypedArray().get(1) == "nirmauni.ac.in")
        }

        fun isValidPassword(password: String): Boolean {
            return !TextUtils.isEmpty(password) && password.length >= 8
        }

        fun getRollNoFromEmail(currentUserEmail: String?): String? {
            if (currentUserEmail == null) {
                return null
            } else {
                return currentUserEmail.substring(0, currentUserEmail.indexOf("@"))
            }
        }
    }
}