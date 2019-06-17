package com.example.getfood.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.getfood.R;


public class DialogAddToCart extends Dialog {

    private ImageButton btnIncreaseQuantity, btnDecreaseQuantity;
    private TextView quantitySetTV;
    private Context mContext;
    private Button btnCancel, btnAddToCart;
    private String title, desc;
    private AddToCartDialogListener addToCartDialogListener;

    public DialogAddToCart(Context context, String title, String desc) {
        super(context);
        this.title = title;
        this.desc = desc;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initViews(context);
    }

    public AddToCartDialogListener getAddToCartDialogListener() {
        return addToCartDialogListener;
    }

    public void setAddToCartDialogListener(AddToCartDialogListener addToCartDialogListener) {
        this.addToCartDialogListener = addToCartDialogListener;
    }

    private void initViews(Context context) {
        mContext = context;

        setContentView(R.layout.adjust_quantity_display);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txtDialogTitle = findViewById(R.id.txtDialogTitle);
        TextView txtDialogDesc = findViewById(R.id.txtDialogMessage);

        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnCancel = findViewById(R.id.btnCancel);
        btnIncreaseQuantity = findViewById(R.id.btnIncreaseQuantity);
        btnDecreaseQuantity = findViewById(R.id.btnDecreaseQuantity);
        quantitySetTV = findViewById(R.id.quantitySetTextView);


        txtDialogTitle.setText(title);
        txtDialogDesc.setText(desc);


        btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToCartDialogListener != null) {
                    if (Integer.parseInt(quantitySetTV.getText().toString()) < 5) {
                        quantitySetTV.setText(String.valueOf(Integer.valueOf(quantitySetTV.getText().toString()) + 1));
                    }
                    addToCartDialogListener.onIncreaseQuantityClicked();
                }
            }
        });

        btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToCartDialogListener != null) {
                    if (Integer.parseInt(quantitySetTV.getText().toString()) > 0) {
                        quantitySetTV.setText(String.valueOf(Integer.valueOf(quantitySetTV.getText().toString()) - 1));
                    }
                    addToCartDialogListener.onDecreaseQuantityClicked();
                }
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (addToCartDialogListener != null) {
                    addToCartDialogListener.onAddToCartClicked(Integer.parseInt(quantitySetTV.getText().toString()));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (addToCartDialogListener != null) {
                    addToCartDialogListener.onCancelClicked();
                }
            }
        });


    }

    public void setPositiveButtonText(String positiveBtnText) {
        btnAddToCart.setText(positiveBtnText);
    }

    public void setNegativeButtonText(String negativeBtnText) {
        btnCancel.setText(negativeBtnText);
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

    public interface AddToCartDialogListener {
        void onAddToCartClicked(int quantity);

        void onCancelClicked();

        void onIncreaseQuantityClicked();

        void onDecreaseQuantityClicked();
    }
}

