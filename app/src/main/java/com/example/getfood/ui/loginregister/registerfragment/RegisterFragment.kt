package com.example.getfood.ui.loginregister.registerfragment

import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.getfood.R
import com.example.getfood.ui.base.BaseFragment
import com.example.getfood.ui.terms.TermsActivity
import com.example.getfood.utils.alert.AlertUtils
import com.example.getfood.utils.alert.DialogSimple.AlertDialogListener

class RegisterFragment : BaseFragment(), RegisterMvpView, View.OnClickListener {
    private var userConPasswordEditText: EditText? = null
    private var userPasswordEditText: EditText? = null
    private var userEmailEditText: EditText? = null
    private var termsCheckBox: CheckBox? = null
    private var presenter: RegisterPresenter<RegisterFragment>? = null
    override val layoutResId: Int
        get() = R.layout.fragment_register

    override fun initViews(view: View) {
        activity!!.setTitle(R.string.register)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        presenter = RegisterPresenter()
        presenter!!.onAttach(this)
        userConPasswordEditText = view.findViewById(R.id.userConPasswordEditText)
        userPasswordEditText = view.findViewById(R.id.userPasswordEditText)
        userEmailEditText = view.findViewById(R.id.userLoginEmailEditText)
        termsCheckBox = view.findViewById(R.id.termsCheckBox)
    }

    override fun setListeners(view: View) {
        view.findViewById<View>(R.id.userAddButton).setOnClickListener(this)
        view.findViewById<View>(R.id.termsTextView).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.userAddButton -> if (presenter!!.isTermsAndConditionChecked(termsCheckBox!!.isChecked)) {
                showLoading()
                //                TODO: Hide Keyboard
//                userEmailEditText.setText("");
                val confirmPassword = userConPasswordEditText!!.text.toString().trim { it <= ' ' }
                val password = userPasswordEditText!!.text.toString().trim { it <= ' ' }
                userConPasswordEditText!!.setText("")
                userPasswordEditText!!.setText("")
                presenter!!.performRegistration(userEmailEditText!!.text.toString().trim { it <= ' ' },
                        password, confirmPassword)
                //                presenter.performRegistration("adit.modhvadia@gmail.com", "12345678", "12345678");
            } else {
                AlertUtils.showAlertBox(mContext, getString(R.string.warning),
                        getString(R.string.accept_terms_and_condition), getString(R.string.ok), object : AlertDialogListener {
                    override fun onButtonClicked() {
                        termsCheckBox!!.requestFocus()
                    }
                })
            }
            R.id.termsTextView -> startActivity(Intent(context, TermsActivity::class.java))
        }
    }

    override fun onUserCreatedSuccessfully() {
        showLoading()
        presenter!!.verifyUserEmail()
    }

    override fun userVerifiedSuccessfully() {
        hideLoading()
        AlertUtils.showAlertBox(mContext, getString(R.string.email_sent), "Verification Email sent to your account. Check your Email",
                getString(R.string.yes), object : AlertDialogListener {
            override fun onButtonClicked() {
                Toast.makeText(context, "Login again after verification", Toast.LENGTH_LONG).show()
                userEmailEditText!!.setText("")
                userPasswordEditText!!.setText("")
                userConPasswordEditText!!.setText("")

//                        presenter.signOutUser();
//                        todo: redirect to login fragment
            }
        })
    }

    override fun onUserEmailVerificationFailed() {
        hideLoading()
        //        todo: Show alert dialog for retry;
    }

    override fun valueEntryError(errorMsg: String?) {
        hideLoading()
        presenter!!.signOutUser()
        AlertUtils.showAlertBox(mContext, getString(R.string.error_occurred), errorMsg, getString(R.string.ok), object : AlertDialogListener {
            override fun onButtonClicked() {
                userEmailEditText!!.requestFocus()
            }
        })
    }
}