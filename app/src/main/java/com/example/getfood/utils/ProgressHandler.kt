package com.example.getfood.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.getfood.R

object ProgressHandler {
    private var progressDialog: Dialog? = null
    fun showProgress(mContext: Context?, title: String?, message: String?) {
        if (progressDialog != null && progressDialog!!.isShowing()) {
            return
        }
        progressDialog = Dialog((mContext)!!)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setCancelable(false)
        if (progressDialog != null) {
            if (progressDialog!!.getWindow() != null) {
                progressDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            progressDialog!!.setContentView(R.layout.dialog_progress)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    fun hideProgress() {
        if (progressDialog != null && progressDialog!!.isShowing()) progressDialog!!.dismiss()
    }
}