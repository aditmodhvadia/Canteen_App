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

class DialogConfirmation constructor(context: Context?, private val title: String?, private val desc: String?) : Dialog((context)!!) {
    private var mContext: Context? = null
    private lateinit var btnDialogNegative: Button
    private lateinit var btnDialogPositive: Button

    interface ConfirmationDialogListener {
        fun onPositiveButtonClicked()
        fun onNegativeButtonClicked()
    }

    var confirmationDialogListener: ConfirmationDialogListener? = null
    private fun initViews(context: Context?) {
        mContext = context
        setContentView(R.layout.dialog_confirmation)
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val linearLayoutBg: LinearLayout = findViewById(R.id.linearDialogConformation)
        val divider: View = findViewById(R.id.dialogDivider)
        val txtDialogTitle: TextView = findViewById(R.id.txtDialogTitle)
        val txtDialogDesc: TextView = findViewById(R.id.txtDialogMessage)
        btnDialogPositive = findViewById(R.id.btnDialogPositive)
        btnDialogNegative = findViewById(R.id.btnDialogNegative)
        txtDialogTitle.text = title
        txtDialogDesc.text = desc
        btnDialogPositive.setOnClickListener {
            dismiss()
            if (confirmationDialogListener != null) {
                confirmationDialogListener!!.onPositiveButtonClicked()
            }
        }
        btnDialogNegative.setOnClickListener {
            dismiss()
            if (confirmationDialogListener != null) {
                confirmationDialogListener!!.onNegativeButtonClicked()
            }
        }
    }

    fun setPositiveButtonText(positiveBtnText: String?) {
        btnDialogPositive.text = positiveBtnText
    }

    fun setNegativeButtonText(negativeBtnText: String?) {
        btnDialogNegative.text = negativeBtnText
    }

    override fun show() {
        if (mContext != null && !(mContext as Activity).isDestroyed) {
            super.show()
        }
    }

    override fun cancel() {
        if (mContext != null && !(mContext as Activity).isDestroyed) {
            super.cancel()
        }
    }

    override fun dismiss() {
        if (mContext != null && !(mContext as Activity).isDestroyed) {
            super.dismiss()
        }
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        initViews(context)
    }
}