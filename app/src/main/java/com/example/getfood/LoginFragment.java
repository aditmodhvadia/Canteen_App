package com.example.getfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment {

    Button userLoginButton;
    EditText userLoginEmailEditText, userLoginPasswordEditText;
    TextView forgotPasswordTextView;
    ProgressDialog progressDialog;
    CheckBox showPasswordCheckBox;
    private FirebaseAuth auth;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_login_curr_user, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        userLoginEmailEditText = v.findViewById(R.id.userLoginEmailEditText);
        userLoginPasswordEditText = (EditText) v.findViewById(R.id.userLoginPasswordEditText);
        showPasswordCheckBox = (CheckBox) v.findViewById(R.id.showPasswordCheckBox);

        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    userLoginPasswordEditText.setTransformationMethod(null);
                } else {
                    userLoginPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        userLoginButton = (Button) v.findViewById(R.id.userLoginButton);
        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        forgotPasswordTextView = (TextView) v.findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });
        return v;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

        progressDialog.setMessage("Logging In");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        auth.signInWithEmailAndPassword(officerEmail, officerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Succesfull login
                if (task.isSuccessful()) {
                    //officer is email verified, hence can proceed further
                    if (auth.getCurrentUser().isEmailVerified()) {
//                        login successful
                        progressDialog.hide();
                        userLoginEmailEditText.setText("");
                        userLoginPasswordEditText.setText("");
//                        new activity will be opened which will display the food items
                        Toast.makeText(getContext(),"Successful",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getContext(), FoodMenuDisplayActivity.class);
                        startActivity(i);
//                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                    }
                    //officer is not email verified, so verification email will be sent
                    else {
                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.hide();
                                //verification email sent successfully
                                if (task.isSuccessful()) {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Verify your Email first!");
                                    builder.setMessage("Verification Email sent to your account. Check your Email");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(getContext(), "Login again after verification", Toast.LENGTH_LONG).show();
                                            userLoginPasswordEditText.setText("");
                                            auth.getInstance().signOut();
                                        }
                                    });
                                    builder.show();

                                }
                                //sending of verification email failed
                                else {
                                    if (task.getException() instanceof FirebaseNetworkException) {
                                        Toast.makeText(getContext(), "Internet connectivity required", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                        Toast.makeText(getContext(), "Email ID not Registered", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Some error occurred. Try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
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
}
