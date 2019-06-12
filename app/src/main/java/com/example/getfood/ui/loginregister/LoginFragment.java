package com.example.getfood.ui.loginregister;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.ui.base.BaseFragment;
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.DialogConfirmation;
import com.example.getfood.utils.DialogSimple;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginFragment extends BaseFragment implements LoginMvpView, View.OnClickListener {

    Button userLoginButton;
    EditText userLoginEmailEditText, userLoginPasswordEditText;
    TextView forgotPasswordTextView;
    ProgressDialog progressDialog;
    private LoginPresenter<LoginFragment> presenter;

    //    CheckBox showPasswordCheckBox;
    private FirebaseAuth auth;
    private boolean timeout = false, success = false;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void initViews(View view) {
        auth = FirebaseAuth.getInstance();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

//    timeout progress dialog

    public void startProgressDialog() {
        timeout = false;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog = ProgressDialog.show(getContext(), "Please Wait", "Logging in", false,
                true, new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
//                        if (listAdapter.isEmpty())
                    }
                });
        progressDialog.setCancelable(false);

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    if (!success) {
                        Toast.makeText(getContext(), "Connection problem, try again", Toast.LENGTH_SHORT).show();
                        timeout = true;
                    }
                }
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 10000);
    }

    private void loginUser() {
        final String officerEmail, officerPassword;

        officerEmail = userLoginEmailEditText.getText().toString().trim().toLowerCase();
        officerPassword = userLoginPasswordEditText.getText().toString().trim();

        if (officerEmail.isEmpty()) {
            userLoginEmailEditText.setError("Email is required");
            userLoginEmailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(officerEmail).matches()) {
            userLoginEmailEditText.setError("Enter valid Email Address");
            userLoginEmailEditText.requestFocus();
            return;
        }

        if (!officerEmail.contains("nirmauni.ac.in")) {
            userLoginEmailEditText.setError("Enter valid Nirma University Domain Email Address");
            userLoginEmailEditText.requestFocus();
            return;
        }

        if (officerPassword.isEmpty()) {
            userLoginPasswordEditText.setError("Password is required");
            userLoginPasswordEditText.requestFocus();
            return;
        }

        if (officerPassword.length() < 6) {
            userLoginPasswordEditText.setError("Minimum length of password is 6");
            userLoginPasswordEditText.requestFocus();
            return;
        }

//        progressDialog.setMessage("Logging In");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        startProgressDialog();

        auth.signInWithEmailAndPassword(officerEmail, officerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Successful login
                if (timeout) {
                    success = false;
                    progressDialog.hide();
//                    Toast.makeText(getContext(), "Inside timeout check after successful login", Toast.LENGTH_SHORT).show();
                    return;
                }
                success = true;
                if (task.isSuccessful()) {
                    //user is email verified, hence can proceed further
                    if (presenter.isUserEmailVerified()) {
//                        login successful
//                        userLoginEmailEditText.setText("");
//                        userLoginPasswordEditText.setText("");
                        progressDialog.hide();
//                        new activity will be opened which will display the food items
                        startActivity(new Intent(getContext(), FoodMenuDisplayActivity.class));
//                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                    }
                    //user is not email verified, so verification email will be sent
                    else {
                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.hide();
                                //verification email sent successfully
                                if (task.isSuccessful()) {
                                    AlertUtils.showConfirmationDialog(getContext(), "Verify your Email first!", "Verification Email sent to your account. Check your Email",
                                            "Ok", null, new DialogConfirmation.ConfirmationDialogListener() {
                                                @Override
                                                public void onPositiveButtonClicked() {
                                                    Toast.makeText(getContext(), "Login again after verification", Toast.LENGTH_LONG).show();
                                                    userLoginPasswordEditText.setText("");
                                                    presenter.performSignOut();
                                                }

                                                @Override
                                                public void onNegativeButtonClicked() {

                                                }
                                            });
                                }
                                //sending of verification email failed
                                else {
                                    if (task.getException() instanceof FirebaseNetworkException) {
                                        Toast.makeText(getContext(), "Internet connectivity required", Toast.LENGTH_SHORT).show();
                                    } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                        Toast.makeText(getContext(), "Email ID not Registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Some error occurred. Try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.hide();
                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
                //unsuccessfull login
                else {
                    progressDialog.hide();
                    if (task.getException() instanceof FirebaseNetworkException) {
                        Toast.makeText(getContext(), "Internet connectivity required", Toast.LENGTH_SHORT).show();
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        userLoginPasswordEditText.setError("Incorrect Password");
                        userLoginPasswordEditText.requestFocus();
                        userLoginPasswordEditText.setText("");
                    } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        userLoginEmailEditText.setError("Email ID not registered");
                        userLoginEmailEditText.requestFocus();
                    } else {
                        Toast.makeText(getContext(), "Some error occurred. Try again", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void forgotPassword() {
        String userEmail = userLoginEmailEditText.getText().toString().trim().toLowerCase();

        if (userEmail.isEmpty()) {
            userLoginEmailEditText.setError("Enter Email ID");
            userLoginEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() || !userEmail.contains("nirmauni.ac.in")) {
            userLoginEmailEditText.setError("Enter valid Email Address");
            userLoginEmailEditText.requestFocus();
            return;
        }

        progressDialog.setMessage("Sending recovery Email to the Email ID");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), "Recovery Email sent", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.hide();
                    if (task.getException() instanceof FirebaseNetworkException) {
                        Toast.makeText(getContext(), "Internet connectivity required", Toast.LENGTH_LONG).show();
                    } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        userLoginEmailEditText.setError("Email ID not registered");
                        userLoginEmailEditText.requestFocus();
                    } else {
                        Toast.makeText(getContext(), "Some error occurred. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userLoginButton:
                showLoading();
//                TODO: Hide Keyboard
                String userEmail = userLoginEmailEditText.getText().toString().trim();
                String password = userLoginPasswordEditText.getText().toString().trim();
                userLoginPasswordEditText.setText("");

                presenter.performLogin(userEmail, password);

//                loginUser();
                break;
            case R.id.forgotPasswordTextView:
                forgotPassword();
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
}
