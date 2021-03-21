package com.example.getfood.ui.splash

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.TextView
import com.example.getfood.R
import com.example.getfood.ui.base.BaseActivity
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity
import com.example.getfood.ui.loginregister.LoginActivity
import com.example.getfood.utils.alert.AlertUtils
import com.example.getfood.utils.alert.DialogConfirmation.ConfirmationDialogListener

class SplashActivity : BaseActivity(), SplashMvpView {
    private var presenter: SplashPresenter<SplashActivity>? = null
    override fun initViews() {
        presenter = SplashPresenter()
        presenter!!.onAttach(this)
        val tvVersionName = findViewById<TextView>(R.id.tvVersionName)
        val versionName = presenter?.versionName
        tvVersionName.text = versionName
        presenter!!.determineIfUpdateNeeded(versionName)
    }

    override fun updateNotRequired() {
        presenter!!.determineIfUserLoggedIn()
    }

    override fun updateRequired() {
        AlertUtils.showConfirmationDialog(this@SplashActivity, getString(R.string.warning), getString(R.string.outdated_version_msg),
                getString(R.string.update), "Exit", object : ConfirmationDialogListener {
            override fun onPositiveButtonClicked() {
                val url = getString(R.string.release_url)
                try {
                    val i = Intent(getString(R.string.main_action))
                    i.component = ComponentName.unflattenFromString(getString(R.string.chrome_intent))
                    i.addCategory(getString(R.string.launcher_category))
                    i.data = Uri.parse(url)
                    startActivity(i)
                    finish()
                } catch (e: ActivityNotFoundException) {
                    // Chrome is not installed
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(i)
                    finish()
                }
            }

            override fun onNegativeButtonClicked() {
                finish()
            }
        })
    }

    override val context: Context
        get() = this

    override fun userIsSignedIn() {
        startActivity(Intent(this@SplashActivity, FoodMenuDisplayActivity::class.java))
        Log.d("vcheck", "everything green")
        finish()
    }

    override fun userIsNotSignedIn() {
        //start login activity
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }

    override fun setListeners() {}
    override val layoutResId: Int
        get() = R.layout.activity_splash
}