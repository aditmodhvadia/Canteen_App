package com.example.getfood.ui.loginregister.loginfragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.ui.base.BaseFragment;
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.DialogConfirmation;
import com.example.getfood.utils.DialogSimple;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class LoginFragment extends BaseFragment implements LoginMvpView, View.OnClickListener {

    Button userLoginButton;
    EditText userLoginEmailEditText, userLoginPasswordEditText;
    TextView forgotPasswordTextView;
    ProgressDialog progressDialog;
    private LoginPresenter<LoginFragment> presenter;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void initViews(View view) {

        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        presenter = new LoginPresenter<>();
        presenter.onAttach(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        userLoginEmailEditText = view.findViewById(R.id.userLoginEmailEditText);
        userLoginPasswordEditText = view.findViewById(R.id.userLoginPasswordEditText);
        userLoginButton = view.findViewById(R.id.userLoginButton);
        forgotPasswordTextView = view.findViewById(R.id.forgotPasswordTextView);
    }

    @Override
    public void setListeners(View view) {
        userLoginButton.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showLoading();
        String userEmail = userLoginEmailEditText.getText().toString().trim();
        switch (v.getId()) {
            case R.id.userLoginButton:
//                TODO: Hide Keyboard
                String password = userLoginPasswordEditText.getText().toString().trim();
                userLoginPasswordEditText.setText("");

                presenter.performLogin(userEmail, password);
                break;
            case R.id.forgotPasswordTextView:
                presenter.forgotPasswordClicked(userEmail);
                break;
        }
    }

    @Override
    public void valueEntryError(String errorMsg) {
        hideLoading();
        presenter.performSignOut();
        AlertUtils.showAlertBox(mContext, getString(R.string.error_occurred), errorMsg, getString(R.string.ok),
                new DialogSimple.AlertDialogListener() {
                    @Override
                    public void onButtonClicked() {
                        userLoginEmailEditText.requestFocus();
                    }
                });
    }

    @Override
    public void userVerifiedSuccessfully() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("##FCM", "getInstanceId failed", task.getException());
//                            TODO: redirect user to app from here as well
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        presenter.updateToken(token);
                        // Log and toast
                        Log.d("##FCM", token);
                    }
                });

    }

    @Override
    public void onTokenUpdatedSuccessfully() {
        hideLoading();
        startActivity(new Intent(mContext, FoodMenuDisplayActivity.class));
    }

    @Override
    public void onUserEmailVerificationFailed() {
        hideLoading();
        AlertUtils.showConfirmationDialog(mContext, "Verify your Email First!",
                mContext.getString(R.string.verify_email_msg), getString(R.string.ok), getString(R.string.cancel),
                new DialogConfirmation.ConfirmationDialogListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        presenter.sendEmailForVerification();
                    }

                    @Override
                    public void onNegativeButtonClicked() {

                    }
                });
    }

    @Override
    public void onPasswordResetEmailSentSuccessfully() {
        AlertUtils.showAlertBox(mContext, mContext.getString(R.string.pwd_reset_sent), mContext.getString(R.string.check_email), mContext.getString(R.string.ok), new DialogSimple.AlertDialogListener() {
            @Override
            public void onButtonClicked() {
                userLoginPasswordEditText.requestFocus();
            }
        });
    }
}
