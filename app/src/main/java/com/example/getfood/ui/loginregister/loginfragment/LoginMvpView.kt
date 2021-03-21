package com.example.getfood.ui.loginregister.loginfragment

import android.content.Context
import com.example.getfood.ui.base.BaseView

interface LoginMvpView : BaseView {
    val fragmentContext: Context
    fun valueEntryError(errMsg: String?)
    fun userVerifiedSuccessfully()
    fun onUserEmailVerificationFailed()
    fun onPasswordResetEmailSentSuccessfully()
    fun onTokenUpdatedSuccessfully()
}