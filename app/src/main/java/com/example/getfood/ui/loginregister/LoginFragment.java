package com.example.getfood.ui.loginregister;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.AppUtils;
import com.example.getfood.utils.OnDialogButtonClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class LoginFragment extends Fragment implements LoginView{

    Button userLoginButton;
    EditText userLoginEmailEditText, userLoginPasswordEditText;
    TextView forgotPasswordTextView;
    ProgressDialog progressDialog;
//    CheckBox showPasswordCheckBox;
    private FirebaseAuth auth;
    private boolean timeout = false, success = false;

    LoginPresenter presenter;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_curr_user, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        presenter = new LoginPresenter(LoginFragment.this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        userLoginEmailEditText = v.findViewById(R.id.userLoginEmailEditText);
        userLoginPasswordEditText = v.findViewById(R.id.userLoginPasswordEditText);
//        showPasswordCheckBox = v.findViewById(R.id.showPasswordCheckBox);

        /*showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    userLoginPasswordEditText.setTransformationMethod(null);
                } else {
                    userLoginPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });*/

        userLoginButton = v.findViewById(R.id.userLoginButton);
        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        forgotPasswordTextView = v.findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });
        return v;
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

    public void loginClick(View view) {
        String userEmail = userLoginEmailEditText.getText().toString().trim().toLowerCase();
        String userPassword = userLoginPasswordEditText.getText().toString().trim();

        presenter.performLogin(userEmail, userPassword, userLoginEmailEditText, userLoginPasswordEditText);
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

        AppUtils.getInstance().hideKeyboard((Activity) getContext());

//        progressDialog.setMessage("Logging In");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        startProgressDialog();

        auth.signInWithEmailAndPassword(officerEmail, officerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Succesfull login
                if (timeout) {
                    success = false;
                    progressDialog.hide();
//                    Toast.makeText(getContext(), "Inside timeout check after successful login", Toast.LENGTH_SHORT).show();
                    return;
                }
                success = true;
                if (task.isSuccessful()) {
                    //user is email verified, hence can proceed further
                    if (auth.getCurrentUser().isEmailVerified()) {
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
                                    AlertUtils.openAlertDialog(getContext(), "Verify your Email first!", "Verification Email sent to your account. Check your Email",
                                            "Ok", null, new OnDialogButtonClickListener() {
                                                @Override
                                                public void onPositiveButtonClicked() {
                                                    Toast.makeText(getContext(), "Login again after verification", Toast.LENGTH_LONG).show();
                                                    userLoginPasswordEditText.setText("");
                                                    auth.getInstance().signOut();
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
    public void loginSuccess() {

    }

    @Override
    public void loginErrror(String errmsg, EditText editText) {
        editText.setError(errmsg);
        editText.requestFocus();
    }
}
