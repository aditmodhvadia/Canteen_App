package com.example.firebase_api_library.api

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.canteen_app_models.helpers.FoodMenuDetails
import com.example.canteen_app_models.models.FoodItem
import com.example.canteen_app_models.models.FullOrder
import com.example.firebase_api_library.listeners.DBValueEventListener
import com.example.firebase_api_library.listeners.OnDynamicLinkStatusListener
import com.example.firebase_api_library.listeners.OnTaskCompleteListener
import com.example.firebase_api_library.utils.AppUtils
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.DynamicLink.IosParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import java.util.*

class FireBaseApiManager {
    //    TODO: Implement common call/listener for all FireBase RealTime Database listeners first and
    //     then write calls and all FireBase APIs subsequently
    /**
     * Call to fetch details of an Orders placed by a User
     *
     * @param orderID              Unique Order ID
     * @param dbValueEventListener Callback for ValueEventListener
     */
    fun orderDetailListener(orderID: String, dbValueEventListener: DBValueEventListener<FullOrder?>) {
        val dbRef = FirebaseDatabase.getInstance().reference
                .child(BaseUrl.USER_ORDER).child(AppUtils.getRollNoFromEmail(apiWrapper?.currentUserEmail)!!).child(orderID)
        apiWrapper!!.valueEventListener(dbRef, object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dbValueEventListener.onDataChange(dataSnapshot.getValue(FullOrder::class.java))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                dbValueEventListener.onCancelled(Error(databaseError.message))
            }
        })
    }

    /**
     * Call to fetch List of all Orders placed by a User
     *
     * @param eventListener Callback for ValueEventListener
     */
    fun orderListListener(eventListener: DBValueEventListener<ArrayList<FullOrder?>?>) {
        val dbRef = FirebaseDatabase.getInstance().reference
                .child(BaseUrl.USER_ORDER).child(AppUtils.getRollNoFromEmail(apiWrapper?.currentUserEmail)!!)
        apiWrapper!!.valueEventListener(dbRef, object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val orderList = ArrayList<FullOrder?>()
                for (dsp in dataSnapshot.children) {
                    orderList.add(dsp.getValue(FullOrder::class.java))
                }
                eventListener.onDataChange(orderList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                eventListener.onCancelled(Error(databaseError.message))
            }
        })
    }

    fun foodMenuListener(category: String, eventListener: DBValueEventListener<ArrayList<FoodItem?>?>) {
        val dbRef = FirebaseDatabase.getInstance().reference
                .child(BaseUrl.FOOD_MENU).child(category)
        apiWrapper!!.valueEventListener(dbRef, object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val foodItems = ArrayList<FoodItem?>()
                for (dsp in dataSnapshot.children) {
                    val foodItem = FoodItem.fromMap(dsp.value as MutableMap<String, Any>?, category, dsp.key)
                    if (dsp.hasChild(FoodMenuDetails.AVAILABLE) && dsp.child(FoodMenuDetails.AVAILABLE).value.toString() == "Yes") {
                        foodItems.add(foodItem)
                    }
                }
                eventListener.onDataChange(foodItems)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                eventListener.onCancelled(Error(databaseError.message))
            }
        })
    }

    fun forceSignOutUser() {
//        todo: log analytics event
        apiWrapper!!.signOutUser()
    }

    /**
     * To determine whether user has verifies their email address or not
     *
     * @return boolean value for user email verification
     */
    val isUserEmailVerified: Boolean
        get() = apiWrapper!!.isUserVerified

    fun createNewUserWithEmailPassword(userEmail: String?, password: String?, onTaskCompleteListener: OnTaskCompleteListener) {
        apiWrapper!!.createNewUserWithEmailPassword(userEmail, password) { task ->
            if (task.isSuccessful) {
                onTaskCompleteListener.onTaskSuccessful()
            } else {
                if (task.exception is FirebaseNetworkException) {
                    onTaskCompleteListener.onTaskCompleteButFailed("No Internet")
                } else if (task.exception is FirebaseAuthUserCollisionException) {
                    onTaskCompleteListener.onTaskCompleteButFailed("Email ID is already in use")
                } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    onTaskCompleteListener.onTaskCompleteButFailed("Invalid Credentials")
                } else {
                    onTaskCompleteListener.onTaskCompleteButFailed("Error Occurred")
                }
            }
        }
    }

    fun signInWithEmailAndPassword(userEmail: String, password: String, onCompleteListener: OnTaskCompleteListener) {
        apiWrapper!!.signInWithEmailAndPassword(userEmail, password) { task ->
            if (task.isSuccessful) {
                onCompleteListener.onTaskSuccessful()
            } else {
                if (task.exception is FirebaseNetworkException) {
                    onCompleteListener.onTaskCompleteButFailed("No Internet")
                } else if (task.exception is FirebaseAuthUserCollisionException) {
                    onCompleteListener.onTaskCompleteButFailed("Email ID is already in use")
                } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    onCompleteListener.onTaskCompleteButFailed("Invalid Credentials")
                } else {
                    onCompleteListener.onTaskCompleteButFailed("Error Occurred")
                }
            }
        }
    }

    fun sendEmailVerification(onTaskCompleteListener: OnTaskCompleteListener) {
        val url = "http://getfood.page.link/verify/?email=$currentUserEmail"
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(url))
                .setDomainUriPrefix("https://getfood.page.link") // Open links with this app on Android
                .setAndroidParameters(AndroidParameters.Builder().build()) // Open links with com.example.ios on iOS
                .setIosParameters(IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink()
        val dynamicLinkUri = dynamicLink.uri
        val actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(dynamicLinkUri.toString())
                .setHandleCodeInApp(true)
                .setDynamicLinkDomain("getfood.page.link")
                .build()
        apiWrapper!!.sendEmailVerification(actionCodeSettings, { task ->
            if (task.isSuccessful) {
                onTaskCompleteListener.onTaskSuccessful()
            } else {
                if (task.exception is FirebaseNetworkException) {
                    onTaskCompleteListener.onTaskCompleteButFailed("No Internet")
                } else if (task.exception is FirebaseAuthUserCollisionException) {
                    onTaskCompleteListener.onTaskCompleteButFailed("Email ID is already in use")
                } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    onTaskCompleteListener.onTaskCompleteButFailed("Invalid Credentials")
                } else {
                    onTaskCompleteListener.onTaskCompleteButFailed("Error Occurred")
                }
            }
        }) { e -> onTaskCompleteListener.onTaskFailed(e) }
    }

    fun bindDynamicLinkEmailVerification(intent: Intent?, context: Context?,
                                         onDynamicLinkStatusListener: OnDynamicLinkStatusListener) {
        apiWrapper!!.listenToDynamicLinks(intent, context, { pendingDynamicLinkData -> // Get deep link from result (may be null if no link is found)
            var deepLink: Uri? = null
            if (pendingDynamicLinkData != null) {
                deepLink = pendingDynamicLinkData.link
                if (deepLink.toString().contains("verify")) {
                    Log.d("##DebugData", "Dynamic link contains verify")
                    onDynamicLinkStatusListener.onDynamicLinkFound(deepLink.toString())
                } else {
                    Log.d("##DebugData", """
     Dynamic link successful but not for verify email 
     Link: ${deepLink.toString()}
     """.trimIndent())
                }
            }
        }) { e -> Log.d("##DebugData", e.message!!) }
    }

    val currentUserEmail: String?
        get() = apiWrapper?.currentUserEmail

    fun sendPasswordResetEmail(userEmail: String?, onTaskCompleteListener: OnTaskCompleteListener) {
        apiWrapper!!.sendPasswordResetEmail(userEmail, { task ->
            if (task.isSuccessful) {
                onTaskCompleteListener.onTaskSuccessful()
            } else {
                if (task.exception is FirebaseNetworkException) {
                    onTaskCompleteListener.onTaskCompleteButFailed("No Internet")
                } else if (task.exception is FirebaseAuthUserCollisionException) {
                    onTaskCompleteListener.onTaskCompleteButFailed("Email ID is already in use")
                } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    onTaskCompleteListener.onTaskCompleteButFailed("Invalid Credentials")
                } else {
                    onTaskCompleteListener.onTaskCompleteButFailed("Error Occurred")
                }
            }
        }) { e -> onTaskCompleteListener.onTaskFailed(e) }
    }

    fun reloadUserAuthState(onSuccessListener: OnSuccessListener<Void?>?, onFailureListener: OnFailureListener) {
        apiWrapper!!.reloadCurrentUserAuthState(onSuccessListener, onFailureListener)
    }

    fun determineIfUpdateNeededAtSplash(versionName: String, eventListener: DBValueEventListener<String?>) {
        val versionCheck = FirebaseDatabase.getInstance().reference.child(BaseUrl.VERSION_CHECK).child(versionName)
        apiWrapper!!.singleValueEventListener(versionCheck, object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                eventListener.onDataChange(dataSnapshot.value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                eventListener.onCancelled(Error(databaseError.message))
            }
        })
    }

    val newOrderKey: String?
        get() = apiWrapper!!.getKey(FirebaseDatabase.getInstance().reference.child(BaseUrl.USER_ORDER)
                .child(AppUtils.getRollNoFromEmail(apiWrapper?.currentUserEmail)!!))

    fun setOrderValue(fullOrder: FullOrder, onCompleteListener: OnTaskCompleteListener) {
        val orderReference = FirebaseDatabase.getInstance().reference.child(BaseUrl.USER_ORDER)
                .child(AppUtils.getRollNoFromEmail(apiWrapper?.currentUserEmail)!!).child(fullOrder.orderId!!)
        apiWrapper!!.setValue(orderReference, fullOrder) { task ->
            if (task.isSuccessful) {
                onCompleteListener.onTaskSuccessful()
            } else {
                if (task.exception is FirebaseNetworkException) {
                    onCompleteListener.onTaskCompleteButFailed("No Internet")
                } else if (task.exception is FirebaseAuthUserCollisionException) {
                    onCompleteListener.onTaskCompleteButFailed("Email ID is already in use")
                } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    onCompleteListener.onTaskCompleteButFailed("Invalid Credentials")
                } else {
                    onCompleteListener.onTaskCompleteButFailed("Error Occurred")
                }
            }
        }
    }

    fun getNewOrderData(orderId: String?, eventListener: ValueEventListener?) {
        val orderReference = FirebaseDatabase.getInstance().reference.child(BaseUrl.USER_ORDER)
                .child(AppUtils.getRollNoFromEmail(apiWrapper?.currentUserEmail)!!).child(orderId!!)
        apiWrapper!!.singleValueEventListener(orderReference, eventListener)
    }

    val isUserLoggedIn: Boolean
        get() = currentUserEmail != null

    fun updateToken(token: String?, onTaskCompleteListener: OnTaskCompleteListener) {
        val tokenReference = FirebaseDatabase.getInstance().reference.child(BaseUrl.USER_DATA)
                .child(AppUtils.getRollNoFromEmail(currentUserEmail)!!).child(BaseUrl.TOKEN)
        apiWrapper!!.setValue(tokenReference, token) { task ->
            if (task.isSuccessful) {
                Log.d("##FCM", "Token updated")
                onTaskCompleteListener.onTaskSuccessful()
            } else {
                Log.d("##FCM", "Failed to update token")
                onTaskCompleteListener.onTaskCompleteButFailed(task.exception.toString())
            }
        }
    }

    fun setRatingValueForOrderItem(rating: Float, position: Int, orderId: String?) {
        val orderItem = FirebaseDatabase.getInstance().reference
                .child(BaseUrl.USER_ORDER)
                .child(AppUtils.getRollNoFromEmail(currentUserEmail)!!)
                .child(orderId!!)
                .child("orderItems")
                .child(position.toString())
                .child("itemRating")
        apiWrapper!!.setValue(orderItem, rating) { task ->
            if (task.isSuccessful) {
                Log.d("##DebugData", "Rating set")
            } else {
                Log.d("##DebugData", "Rating not set" + task.exception!!.message)
            }
        }
    }

    object BaseUrl {
        // Declare the constants
        const val USER_ORDER = "UserOrderData"
        const val FOOD_MENU = "Food"
        const val VERSION_CHECK = "version-check"
        const val USER_DATA = "UserData"
        const val TOKEN = "Token"
    }

    companion object {
        private var apiManager: FireBaseApiManager? = null
        private var apiWrapper: FireBaseApiWrapper? = null

        @JvmStatic
        val instance: FireBaseApiManager?
            get() {
                if (apiManager == null) {
                    apiManager = FireBaseApiManager()
                }
                if (apiWrapper == null) {
                    apiWrapper = FireBaseApiWrapper.instance
                }
                return apiManager
            }
    }
}