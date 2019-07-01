package com.example.getfood.utils.alert;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.getfood.R;


public class DialogSimple extends Dialog {

    private Context mContext;
    private String title, desc, buttonText;

    private AlertDialogListener alertDialogListener;

    public interface AlertDialogListener {
        void onButtonClicked();
    }

    public void setAlertDialogListener(AlertDialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }

    public DialogSimple(Context context, String title, String desc, String buttonText) {
        super(context);
        this.title = title;
        this.desc = desc;
        this.buttonText = buttonText;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initViews(context);
    }

    private void initViews(Context context) {
        mContext = context;

        setContentView(R.layout.dialog_simple);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout linearLayoutBg = findViewById(R.id.linearDialogSimple);
        View divider = findViewById(R.id.dialogDivider);

        TextView txtDialogTitle = findViewById(R.id.txtDialogTitle);
        TextView txtDialogDesc = findViewById(R.id.txtMessage);
        Button btnDialog = findViewById(R.id.btnDialogPositive);

        txtDialogTitle.setText(title);
        txtDialogDesc.setText(desc);
        btnDialog.setText(buttonText);

        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (alertDialogListener != null) {
                    alertDialogListener.onButtonClicked();
                }
            }
        });
    }

    @Override
    public void show() {
        if (mContext != null && !((Activity) mContext).isDestroyed()) {
            super.show();
        }
    }

    @Override
    public void cancel() {
        if (mContext != null && !((Activity) mContext).isDestroyed()) {
            super.cancel();
        }
    }

    @Override
    public void dismiss() {
        if (mContext != null && !((Activity) mContext).isDestroyed()) {
            super.dismiss();
        }
    }
}
