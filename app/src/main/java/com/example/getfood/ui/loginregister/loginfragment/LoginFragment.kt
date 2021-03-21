package com.example.getfood.ui.loginregister.loginfragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import com.example.getfood.R
import com.example.getfood.ui.base.BaseFragment
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity
import com.example.getfood.utils.alert.AlertUtils
import com.example.getfood.utils.alert.DialogConfirmation.ConfirmationDialogListener
import com.example.getfood.utils.alert.DialogSimple
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class LoginFragment : BaseFragment(), LoginMvpView, View.OnClickListener {
    var userLoginButton: Button? = null
    var userLoginEmailEditText: EditText? = null
    var userLoginPasswordEditText: EditText? = null
    var forgotPasswordTextView: TextView? = null
    var progressDialog: ProgressDialog? = null
    private var presenter: LoginPresenter<LoginFragment>? = null
    override val layoutResId: Int
        get() = R.layout.fragment_login

    override fun initViews(view: View) {
        if (activity != null) {
            activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
        presenter = LoginPresenter()
        presenter!!.onAttach(this)
        progressDialog = ProgressDialog(context)
        progressDialog!!.setCanceledOnTouchOutside(false)
        userLoginEmailEditText = view.findViewById(R.id.userLoginEmailEditText)
        userLoginPasswordEditText = view.findViewById(R.id.userLoginPasswordEditText)
        userLoginButton = view.findViewById(R.id.userLoginButton)
        forgotPasswordTextView = view.findViewById(R.id.forgotPasswordTextView)
    }

    override fun setListeners(view: View) {
        userLoginButton!!.setOnClickListener(this)
        forgotPasswordTextView!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        showLoading()
        val userEmail = userLoginEmailEditText!!.text.toString().trim { it <= ' ' }
        when (v.id) {
            R.id.userLoginButton -> {
                //                TODO: Hide Keyboard
                val password = userLoginPasswordEditText!!.text.toString().trim { it <= ' ' }
                userLoginPasswordEditText!!.setText("")
                presenter!!.performLogin(userEmail, password)
            }
            R.id.forgotPasswordTextView -> presenter!!.forgotPasswordClicked(userEmail)
        }
    }

    override val fragmentContext: Context
        get() = context!!

    override fun valueEntryError(errMsg: String?) {
        hideLoading()
        presenter!!.performSignOut()
        AlertUtils.showAlertBox(fragmentContext, getString(R.string.error_occurred), errMsg, getString(R.string.ok), object : DialogSimple.AlertDialogListener {
            override fun onButtonClicked() {
                userLoginEmailEditText!!.requestFocus()
            }
        })
    }

    override fun userVerifiedSuccessfully() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d("##FCM", "getInstanceId failed", task.exception)
                        //                            TODO: redirect user to app from here as well
                        return@OnCompleteListener
                    }
                    // Get new Instance ID token
                    val token = task.result!!.token
                    presenter!!.updateToken(token)
                    // Log and toast
                    Log.d("##FCM", token)
                })
    }

    override fun onTokenUpdatedSuccessfully() {
        hideLoading()
        startActivity(Intent(fragmentContext, FoodMenuDisplayActivity::class.java))
    }

    override fun onUserEmailVerificationFailed() {
        hideLoading()
        AlertUtils.showConfirmationDialog(fragmentContext, "Verify your Email First!",
                fragmentContext.getString(R.string.verify_email_msg), getString(R.string.ok), getString(R.string.cancel),
                object : ConfirmationDialogListener {
                    override fun onPositiveButtonClicked() {
                        presenter!!.sendEmailForVerification()
                    }

                    override fun onNegativeButtonClicked() {}
                })
    }

    override fun onPasswordResetEmailSentSuccessfully() {
        AlertUtils.showAlertBox(fragmentContext, fragmentContext.getString(R.string.pwd_reset_sent), fragmentContext.getString(R.string.check_email), fragmentContext.getString(R.string.ok), object : DialogSimple.AlertDialogListener {
            override fun onButtonClicked() {
                userLoginPasswordEditText!!.requestFocus()
            }
        })
    }
}