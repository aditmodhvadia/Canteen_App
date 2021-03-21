package com.example.getfood.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.getfood.utils.ProgressHandler

abstract class BaseFragment : Fragment(), BaseView {
    var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResId, container, false)
        initViews(view)
        setListeners(view)
        return view
    }

    /**
     * To get layout resource id
     */
    @get:LayoutRes
    abstract val layoutResId: Int

    /**
     * To initialize views of activity
     */
    abstract fun initViews(view: View)

    /**
     * To set listeners of view or callback
     *
     * @param view
     */
    abstract fun setListeners(view: View)
    override fun showLoading() {
        ProgressHandler.showProgress(mContext, "", "")
    }

    override fun hideLoading() {
        ProgressHandler.hideProgress()
    }

    override val isNetworkConnected: Boolean
        get() {
            val cm = mContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkInfo: NetworkInfo? = null
            if (cm != null) {
                networkInfo = cm.activeNetworkInfo
            }
            return networkInfo != null && networkInfo.isConnected
        }
}