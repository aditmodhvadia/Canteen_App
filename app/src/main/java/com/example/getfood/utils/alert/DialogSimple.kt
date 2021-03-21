package com.example.getfood.utils.alert

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.getfood.R

class DialogSimple constructor(context: Context?, private val title: String?, private val desc: String?, private val buttonText: String?) : Dialog((context)!!) {
    private var mContext: Context? = null
    private var alertDialogListener: AlertDialogListener? = null

    interface AlertDialogListener {
        fun onButtonClicked()
    }

    fun setAlertDialogListener(alertDialogListener: AlertDialogListener?) {
        this.alertDialogListener = alertDialogListener
    }

    private fun initViews(context: Context?) {
        mContext = context
        setContentView(R.layout.dialog_simple)
        getWindow()!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val linearLayoutBg: LinearLayout = findViewById(R.id.linearDialogSimple)
        val divider: View = findViewById(R.id.dialogDivider)
        val txtDialogTitle: TextView = findViewById(R.id.txtDialogTitle)
        val txtDialogDesc: TextView = findViewById(R.id.txtMessage)
        val btnDialog: Button = findViewById(R.id.btnDialogPositive)
        txtDialogTitle.setText(title)
        txtDialogDesc.setText(desc)
        btnDialog.setText(buttonText)
        btnDialog.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                dismiss()
                if (alertDialogListener != null) {
                    alertDialogListener!!.onButtonClicked()
                }
            }
        })
    }

    public override fun show() {
        if (mContext != null && !(mContext as Activity).isDestroyed()) {
            super.show()
        }
    }

    public override fun cancel() {
        if (mContext != null && !(mContext as Activity).isDestroyed()) {
            super.cancel()
        }
    }

    public override fun dismiss() {
        if (mContext != null && !(mContext as Activity).isDestroyed()) {
            super.dismiss()
        }
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        initViews(context)
    }
}