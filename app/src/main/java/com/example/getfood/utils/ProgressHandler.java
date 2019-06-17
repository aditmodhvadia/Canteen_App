package com.example.getfood.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.example.getfood.R;

public class ProgressHandler {

    private static Dialog progressDialog;

    public static void showProgress(Context mContext, String title, String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new Dialog(mContext);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        if (progressDialog != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
