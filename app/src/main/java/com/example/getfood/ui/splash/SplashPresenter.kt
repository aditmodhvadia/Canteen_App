package com.example.getfood.ui.splash

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import com.example.firebase_api_library.listeners.DBValueEventListener
import com.example.getfood.ui.base.BasePresenter

class SplashPresenter<V : SplashMvpView?> : BasePresenter<V>(), SplashMvpPresenter<V> {
    override fun determineIfUserLoggedIn() {
        apiManager!!.reloadUserAuthState({
            Log.d("##DebugData", "User reload successful")
            mvpView?.userIsSignedIn()
        }) { e ->
            Log.d("##DebugData", e.message!!)
            mvpView?.userIsNotSignedIn()
        }
    }

    override val versionName: String
        get() {
            val pInfo: PackageInfo
            return try {
                pInfo = mvpView?.context?.packageName?.let { mvpView?.context?.packageManager?.getPackageInfo(it, 0) }!!
                pInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                "beta-testing"
            }
        }

    override fun determineIfUpdateNeeded(versionName: String?) {
        apiManager!!.determineIfUpdateNeededAtSplash(versionName!!, object : DBValueEventListener<String?> {
            override fun onDataChange(data: String?) {
                if (data != null) {
                    mvpView?.updateNotRequired()
                } else {
                    mvpView?.updateRequired()
                }
            }

            override fun onCancelled(error: Error?) {
                mvpView?.updateRequired()
            }
        })
    }
}