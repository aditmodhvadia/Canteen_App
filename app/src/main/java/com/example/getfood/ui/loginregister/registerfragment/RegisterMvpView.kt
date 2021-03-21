package com.example.getfood.ui.loginregister.registerfragment

import com.example.getfood.ui.base.BaseView

interface RegisterMvpView : BaseView {

    fun valueEntryError(errorMsg: String?)
    fun onUserCreatedSuccessfully()
    fun userVerifiedSuccessfully()
    fun onUserEmailVerificationFailed()
}