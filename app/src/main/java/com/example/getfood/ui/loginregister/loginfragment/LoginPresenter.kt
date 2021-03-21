package com.example.getfood.ui.loginregister.loginfragment

import com.example.firebase_api_library.listeners.OnTaskCompleteListener
import com.example.getfood.R
import com.example.getfood.ui.base.BasePresenter
import com.example.getfood.utils.AppUtils

class LoginPresenter<V : LoginMvpView?> : BasePresenter<V>(), LoginMvpPresenter<V> {
    override fun performLogin(userEmail: String, password: String) {
        if (!AppUtils.isEmailValid(userEmail)) {
            mvpView?.valueEntryError(mvpView?.fragmentContext?.getString(R.string.enter_valid_email))
            return
        }
        if (!AppUtils.isValidPassword(password)) {
            mvpView?.valueEntryError("Please enter a valid password more than 8 characters")
            return
        }
        apiManager!!.signInWithEmailAndPassword(userEmail, password, object : OnTaskCompleteListener {
            override fun onTaskSuccessful() {
                verifyUserEmail()
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
            mvpView?.onUserEmailVerificationFailed()
        }
    }

    override fun sendEmailForVerification() {
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

    override fun performSignOut() {
        apiManager!!.forceSignOutUser()
    }

    override val isUserEmailVerified: Boolean
        get() = apiManager!!.isUserEmailVerified

    override fun forgotPasswordClicked(userEmail: String) {
        if (!AppUtils.isEmailValid(userEmail)) {
            mvpView?.valueEntryError(mvpView?.fragmentContext?.getString(R.string.enter_valid_email))
            return
        }
        apiManager!!.sendPasswordResetEmail(userEmail, object : OnTaskCompleteListener {
            override fun onTaskSuccessful() {
                mvpView?.onPasswordResetEmailSentSuccessfully()
            }

            override fun onTaskCompleteButFailed(errMsg: String?) {
                mvpView?.valueEntryError(errMsg)
            }

            override fun onTaskFailed(e: Exception?) {}
        })
    }

    override fun updateToken(token: String?) {
        apiManager!!.updateToken(token, object : OnTaskCompleteListener {
            override fun onTaskSuccessful() {
                mvpView?.onTokenUpdatedSuccessfully()
            }

            override fun onTaskCompleteButFailed(errMsg: String?) {}
            override fun onTaskFailed(e: Exception?) {}
        })
    }
}