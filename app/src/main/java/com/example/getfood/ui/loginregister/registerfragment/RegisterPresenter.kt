package com.example.getfood.ui.loginregister.registerfragment

import android.util.Log
import com.example.firebase_api_library.listeners.OnTaskCompleteListener
import com.example.getfood.ui.base.BasePresenter
import com.example.getfood.utils.AppUtils

class RegisterPresenter<V : RegisterMvpView?> : BasePresenter<V>(), RegisterMvpPresenter<V> {
    override fun performRegistration(userEmail: String, password: String, confirmPassword: String) {
        //Validating all entries First
        if (!AppUtils.isEmailValid(userEmail)) {
            mvpView?.valueEntryError("Please enter a valid Nirma University Email Address")
            return
        }
        if (!(password == confirmPassword)) {
            mvpView?.valueEntryError("Both passwords should match")
            return
        }
        if (!AppUtils.isValidPassword(password)) {
            mvpView?.valueEntryError("Please enter a valid password more than 8 characters")
            return
        }
        apiManager!!.createNewUserWithEmailPassword(userEmail, password, object : OnTaskCompleteListener {
            override fun onTaskSuccessful() {
                Log.d("##DebugData", apiManager!!.isUserEmailVerified.toString())
                Log.d("##DebugData", "${apiManager?.userEmail}")
                mvpView?.onUserCreatedSuccessfully()
            }

            override fun onTaskCompleteButFailed(errMsg: String?) {
                mvpView?.valueEntryError(errMsg)
            }

            override fun onTaskFailed(e: Exception?) {}
        })
    }

    override fun verifyUserEmail() {
        if (apiManager!!.isUserEmailVerified) {
            mvpView?.userVerifiedSuccessfully()
        } else {
            apiManager!!.sendEmailVerification(object : OnTaskCompleteListener {
                override fun onTaskSuccessful() {
                    mvpView?.userVerifiedSuccessfully()
                }

                override fun onTaskCompleteButFailed(errMsg: String?) {
                    mvpView?.valueEntryError(errMsg)
                }

                override fun onTaskFailed(e: Exception?) {
                    mvpView?.onUserEmailVerificationFailed()
                }
            })
        }
    }

    override fun isTermsAndConditionChecked(checked: Boolean): Boolean {
        return checked
    }

    override fun signOutUser() {
        apiManager!!.forceSignOutUser()
    }
}