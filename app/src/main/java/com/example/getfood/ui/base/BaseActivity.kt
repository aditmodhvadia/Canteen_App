package com.example.getfood.ui.base

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.getfood.utils.ProgressHandler

abstract class BaseActivity : AppCompatActivity(), BaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        initViews()
        setListeners()
    }

    /**
     * To initialize views of activity
     */
    abstract fun initViews()

    /**
     * To set listeners of view or callback
     */
    abstract fun setListeners()

    /**
     * To get layout resource id
     */
    abstract val layoutResId: Int
    override fun showLoading() {
        ProgressHandler.showProgress(this, "", "")
    }

    override fun hideLoading() {
        ProgressHandler.hideProgress()
    }

    override val isNetworkConnected: Boolean
        get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkInfo: NetworkInfo? = null
            if (cm != null) {
                networkInfo = cm.activeNetworkInfo
            }
            return networkInfo != null && networkInfo.isConnected
        }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}