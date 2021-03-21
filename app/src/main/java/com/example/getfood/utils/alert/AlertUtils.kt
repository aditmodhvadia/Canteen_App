package com.example.getfood.utils.alert

import android.content.Context
import android.view.View
import android.widget.Toast
import com.example.canteen_app_models.models.FoodItem
import com.example.getfood.R
import com.example.getfood.utils.alert.DialogAddToCart.AddToCartDialogListener
import com.example.getfood.utils.alert.DialogConfirmation.ConfirmationDialogListener
import com.example.getfood.utils.alert.DialogSimple.AlertDialogListener
import com.google.android.material.snackbar.Snackbar

/**
 * To show dialog, toast, snack-bar or any other alert
 */
object AlertUtils {
    /**
     * To get simple alert box
     *
     * @param context    App context
     * @param title      Title
     * @param desc       Desc
     * @param buttonText Button text
     * @param listener   callback
     * @return @[DialogSimple]
     */
    fun getAlertBox(context: Context?, title: String?, desc: String?, buttonText: String?, listener: AlertDialogListener?): DialogSimple {
        return DialogSimple(context, title, desc, buttonText).apply {
            setAlertDialogListener(listener)
            setCancelable(false)
        }
    }

    /**
     * To show @[DialogSimple]
     *
     * @param context    App context
     * @param title      Title
     * @param desc       Desc
     * @param buttonText Button text
     * @param listener   callback
     */
    fun showAlertBox(context: Context?, title: String?, desc: String?, buttonText: String?, listener: AlertDialogListener?) {
        val simpleDialog: DialogSimple = getAlertBox(context, title, desc, buttonText, listener)
        simpleDialog.show()
    }

    fun getConfirmationDialog(context: Context?, title: String?, desc: String?, positiveBtnText: String?, negativeBtnText: String?, listener: ConfirmationDialogListener?): DialogConfirmation {
        return DialogConfirmation(context, title, desc).apply {
            setPositiveButtonText(positiveBtnText)
            setNegativeButtonText(negativeBtnText)
            confirmationDialogListener = listener
            setCancelable(false)
        }
    }

    fun showConfirmationDialog(context: Context?, title: String?, desc: String?, positiveBtnText: String?, negativeBtnText: String?,
                               listener: ConfirmationDialogListener?) {
        val dialogConfirmation: DialogConfirmation = getConfirmationDialog(context, title, desc, positiveBtnText, negativeBtnText, listener)
        dialogConfirmation.show()
    }

    fun getAddToCartDialog(context: Context?, title: String?, desc: String?, positiveBtnText: String?, negativeBtnText: String?, listener: AddToCartDialogListener): DialogAddToCart {
        return DialogAddToCart(context, title, desc).apply {
            setPositiveButtonText(positiveBtnText)
            setNegativeButtonText(negativeBtnText)
            addToCartDialogListener = listener
            setCancelable(false)
        }
    }

    @Deprecated("")
    fun showAddToCartDialog(context: Context, foodItem: FoodItem?, listener: AddToCartDialogListener) {
        val dialogConfirmation: DialogAddToCart = getAddToCartDialog(context, context.getString(R.string.select_quantity),
                foodItem!!.itemName, context.getString(R.string.add_to_cart), context.getString(R.string.no), listener)
        dialogConfirmation.show()
    }

    fun displaySnackBar(view: View?, message: String?) {
        Snackbar.make((view)!!, (message)!!, Snackbar.LENGTH_LONG).show()
    }

    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getSnackBar(view: View?, message: String?, action: String?, clickListener: View.OnClickListener?): Snackbar {
        return Snackbar.make((view)!!, (message)!!, Snackbar.LENGTH_LONG).setAction(action, clickListener)
    }

    fun displaySnackBarWithTime(view: View?, message: String?, action: String?, timeMilliSecond: Int, clickListener: View.OnClickListener?) {
        Snackbar.make((view)!!, (message)!!, timeMilliSecond).setAction(action, clickListener).show()
    }
}