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


public class DialogConfirmation extends Dialog {

    private Context mContext;
    private Button btnDialogNegative, btnDialogPositive;
    private String title, desc;


    public interface ConfirmationDialogListener {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }

    private ConfirmationDialogListener confirmationDialogListener;

    public ConfirmationDialogListener getConfirmationDialogListener() {
        return confirmationDialogListener;
    }

    public void setConfirmationDialogListener(ConfirmationDialogListener confirmationDialogListener) {
        this.confirmationDialogListener = confirmationDialogListener;
    }

    public DialogConfirmation(Context context, String title, String desc) {
        super(context);
        this.title = title;
        this.desc = desc;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initViews(context);
    }

    private void initViews(Context context) {
        mContext = context;

        setContentView(R.layout.dialog_confirmation);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout linearLayoutBg = findViewById(R.id.linearDialogConformation);
        View divider = findViewById(R.id.dialogDivider);

        TextView txtDialogTitle = findViewById(R.id.txtDialogTitle);
        TextView txtDialogDesc = findViewById(R.id.txtDialogMessage);

        btnDialogPositive = findViewById(R.id.btnDialogPositive);
        btnDialogNegative = findViewById(R.id.btnDialogNegative);

        txtDialogTitle.setText(title);
        txtDialogDesc.setText(desc);

        btnDialogPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (confirmationDialogListener != null) {
                    confirmationDialogListener.onPositiveButtonClicked();
                }
            }
        });

        btnDialogNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (confirmationDialogListener != null) {
                    confirmationDialogListener.onNegativeButtonClicked();
                }
            }
        });


    }

    public void setPositiveButtonText(String positiveBtnText) {
        btnDialogPositive.setText(positiveBtnText);
    }

    public void setNegativeButtonText(String negativeBtnText) {
        btnDialogNegative.setText(negativeBtnText);
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
