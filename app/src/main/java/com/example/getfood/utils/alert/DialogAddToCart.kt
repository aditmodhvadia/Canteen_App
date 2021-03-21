package com.example.getfood.utils.alert

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.getfood.R

class DialogAddToCart constructor(context: Context?, private val title: String?, private val desc: String?) : Dialog((context)!!) {
    private lateinit var btnIncreaseQuantity: ImageButton
    private lateinit var btnDecreaseQuantity: ImageButton
    private lateinit var quantitySetTV: TextView
    private var mContext: Context? = null
    private lateinit var btnCancel: Button
    private lateinit var btnAddToCart: Button
    lateinit var addToCartDialogListener: AddToCartDialogListener
    private fun initViews(context: Context?) {
        mContext = context
        setContentView(R.layout.adjust_quantity_display)
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val txtDialogTitle: TextView = findViewById(R.id.txtDialogTitle)
        val txtDialogDesc: TextView = findViewById(R.id.txtDialogMessage)
        btnAddToCart = findViewById(R.id.btnAddToCart)
        btnCancel = findViewById(R.id.btnCancel)
        btnIncreaseQuantity = findViewById(R.id.btnIncreaseQuantity)
        btnDecreaseQuantity = findViewById(R.id.btnDecreaseQuantity)
        quantitySetTV = findViewById(R.id.quantitySetTextView)
        txtDialogTitle.text = title
        txtDialogDesc.text = desc
        btnIncreaseQuantity.setOnClickListener {
            if (quantitySetTV.text.toString().toInt() < 5) {
                quantitySetTV.text = (Integer.valueOf(quantitySetTV.text.toString()) + 1).toString()
            }
            addToCartDialogListener.onIncreaseQuantityClicked()
        }
        btnDecreaseQuantity.setOnClickListener {
            if (quantitySetTV.text.toString().toInt() > 0) {
                quantitySetTV.text = (Integer.valueOf(quantitySetTV.text.toString()) - 1).toString()
            }
            addToCartDialogListener.onDecreaseQuantityClicked()
        }
        btnAddToCart.setOnClickListener {
            dismiss()
            addToCartDialogListener.onAddToCartClicked(quantitySetTV.text.toString().toInt())
        }
        btnCancel.setOnClickListener {
            dismiss()
            addToCartDialogListener.onCancelClicked()
        }
    }

    fun setPositiveButtonText(positiveBtnText: String?) {
        btnAddToCart.text = positiveBtnText
    }

    fun setNegativeButtonText(negativeBtnText: String?) {
        btnCancel.text = negativeBtnText
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

    interface AddToCartDialogListener {
        fun onAddToCartClicked(quantity: Int)
        fun onCancelClicked()
        fun onIncreaseQuantityClicked()
        fun onDecreaseQuantityClicked()
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        initViews(context)
    }
}