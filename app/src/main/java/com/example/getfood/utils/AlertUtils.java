package com.example.getfood.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;


/**
 * To show dialog, toast, snack-bar or any other alert
 */
public class AlertUtils {


    /**
     * To get simple alert box
     *
     * @param context    App context
     * @param title      Title
     * @param desc       Desc
     * @param buttonText Button text
     * @param listener   callback
     * @return @{@link DialogSimple}
     */
    public static DialogSimple getAlertBox(Context context, String title, String desc, String buttonText, DialogSimple.AlertDialogListener listener) {
        DialogSimple simpleDialog = new DialogSimple(context, title, desc, buttonText);
        simpleDialog.setAlertDialogListener(listener);
        simpleDialog.setCancelable(false);
        return simpleDialog;
    }

    /**
     * To show @{@link DialogSimple}
     *
     * @param context    App context
     * @param title      Title
     * @param desc       Desc
     * @param buttonText Button text
     * @param listener   callback
     */
    public static void showAlertBox(Context context, String title, String desc, String buttonText, DialogSimple.AlertDialogListener listener) {
        DialogSimple simpleDialog = getAlertBox(context, title, desc, buttonText, listener);
        simpleDialog.show();
    }

    public static DialogConfirmation getConfirmationDialog(Context context, String title, String desc, String positiveBtnText, String negativeBtnText, DialogConfirmation.ConfirmationDialogListener listener) {
        DialogConfirmation dialogConfirmation = new DialogConfirmation(context, title, desc);
        dialogConfirmation.setPositiveButtonText(positiveBtnText);
        dialogConfirmation.setNegativeButtonText(negativeBtnText);
        dialogConfirmation.setConfirmationDialogListener(listener);
        dialogConfirmation.setCancelable(false);
        return dialogConfirmation;
    }

    public static void showConfirmationDialog(Context context, String title, String desc, String positiveBtnText, String negativeBtnText,
                                              DialogConfirmation.ConfirmationDialogListener listener) {
        DialogConfirmation dialogConfirmation = getConfirmationDialog(context, title, desc, positiveBtnText, negativeBtnText, listener);
        dialogConfirmation.show();
    }

    public static void displaySnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static Snackbar getSnackBar(View view, String message, String action, View.OnClickListener clickListener) {
        return Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(action, clickListener);

    }

    public static void displaySnackBarWithTime(View view, String message, String action, int timeMilliSecond, View.OnClickListener clickListener) {
        Snackbar.make(view, message, timeMilliSecond).setAction(action, clickListener).show();

    }

}
