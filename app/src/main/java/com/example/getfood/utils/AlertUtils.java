package com.example.getfood.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

public class AlertUtils {

    /**
     * Return an alert dialog
     *
     * @param message         message for the alert dialog
     * @param listener        listener to trigger selection methods
     * @param title           title for the alert dialog
     * @param context         context to display the alert dialog
     * @param positiveBtnText positiveBtnText to display positive button text
     * @param positiveBtnText negativeBtnText to display negative button text
     */
    public static void openAlertDialog(Context context, String title, String message, String positiveBtnText, String negativeBtnText,
                                       final OnDialogButtonClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) {
            builder.setTitle(title);
        }
        if (message != null) {
            builder.setMessage(message);
        }
        if (positiveBtnText != null) {
            builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    listener.onPositiveButtonClicked();

                }
            });
        }

        if (negativeBtnText != null) {
            builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    listener.onNegativeButtonClicked();

                }
            });
        }
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        button.setBackgroundColor(context.getResources().getColor(R.color.colorBadRating)   );

    }
}
