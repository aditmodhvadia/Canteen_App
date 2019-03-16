package com.example.getfood.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.getfood.R;

public class AppUtils {

    public static Snackbar getSnackbar(Context context, String msg) {
        View view = ((Activity) context).findViewById(R.id.CoordinatorLayoutParent);
        return Snackbar.make(view, msg,
                Snackbar.LENGTH_LONG);
    }
}
