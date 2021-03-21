package com.example.getfood.ui.loginregister

import android.content.Intent
import android.util.Log
import com.example.firebase_api_library.listeners.OnDynamicLinkStatusListener
import com.example.firebase_api_library.listeners.OnTaskCompleteListener
import com.example.getfood.R
import com.example.getfood.ui.base.BasePresenter

class LoginActivityPresenter<V : LoginActivityMvpView?> constructor() : BasePresenter<V>(), LoginActivityMvpPresenter<V> {
    public override fun bindDynamicLinkEmailVerification(intent: Intent?) {
        apiManager!!.bindDynamicLinkEmailVerification(intent, mvpView?.context, object : OnDynamicLinkStatusListener {
            public override fun onDynamicLinkFound(link: String?) {
                val userEmail: String = link!!.split("email=".toRegex()).toTypedArray().get(1)
                //                            TODO: Sign in user with the email id and then call alert dialog
//                            TODO: Check if email is same as the one with currently logged in
                Log.d("##DebugData", userEmail)
                apiManager!!.reloadUserAuthState({ //                                User is signed in
                    if (apiManager?.userEmail != null && apiManager?.userEmail!!.contains(userEmail)) {
                        Log.d("##DebugData", "Current user email and dynamic link email match")
                        if (apiManager!!.isUserEmailVerified) {
                            Log.d("##DebugData", "current user now email verified")
                            mvpView?.onSuccessfulVerificationAndSignIn()
                        } else {
                            Log.d("##DebugData", "current user not email verified")
                            mvpView?.onFailedVerificationOrSignIn(Exception(
                                    mvpView?.context?.getString(R.string.common_err_msg)))
                        }
                    } else {
                        Log.d("##DebugData", "Current user email and dynamic link email do not match")
                        Log.d("##DebugData", userEmail)
                        Log.d("##DebugData", "${apiManager?.userEmail}")
                        mvpView?.onFailedVerificationOrSignIn(Exception(
                                mvpView?.context?.getString(R.string.common_err_msg)))
                    }
                }) { e -> mvpView?.onFailedVerificationOrSignIn(e) }
            }
        })
    }

    public override fun updateToken(token: String?) {
        apiManager!!.updateToken(token, object : OnTaskCompleteListener {
            public override fun onTaskSuccessful() {
                mvpView?.onTokenUpdatedSuccessfully()
            }

            public override fun onTaskCompleteButFailed(errMsg: String?) {}
            public override fun onTaskFailed(e: Exception?) {}
        })
    }

    override val isUserLoggedIn: Boolean
        get() {
            return apiManager!!.isUserLoggedIn
        }

    public override fun signOutUser() {
        apiManager!!.forceSignOutUser()
    }
}