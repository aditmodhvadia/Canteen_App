package com.example.getfood.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.getfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterFragment extends Fragment {


    EditText userConPasswordEditText, userPasswordEditText, userEmailEditText;
    CheckBox termsCheckBox;
    Button userAddButton;
    ProgressDialog progressDialog;

    private FirebaseAuth auth;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Register");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());

        userConPasswordEditText = view.findViewById(R.id.userConPasswordEditText);
        userPasswordEditText = view.findViewById(R.id.userPasswordEditText);
        userEmailEditText = view.findViewById(R.id.userLoginEmailEditText);

        termsCheckBox = view.findViewById(R.id.termsCheckBox);


        userAddButton = view.findViewById(R.id.userAddButton);
//        verify all fields on button press
        userAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValues();
            }
        });
    }

    private void checkValues() {
        final String userPass, userConPass, userEmail;

        userPass = userPasswordEditText.getText().toString().trim();
        userConPass = userConPasswordEditText.getText().toString().trim();
        userEmail = userEmailEditText.getText().toString().trim().toLowerCase();

        //Validating all entries First

        if (userEmail.isEmpty()) {
            userEmailEditText.setError("Email ID Required");
            userEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            userEmailEditText.setError("Enter valid Email Address");
            userEmailEditText.requestFocus();
            return;
        }

        if (!userEmail.contains("nirmauni.ac.in")) {
            userEmailEditText.setError("Enter valid Nirma University Domain Email Address");
            userEmailEditText.requestFocus();
            return;
        }

        if (userPass.isEmpty()) {
            userPasswordEditText.setError("Password Required");
            userPasswordEditText.requestFocus();
            return;
        }
        if (userConPass.isEmpty()) {
            userConPasswordEditText.setError("Password Confirmation Required");
            userConPasswordEditText.requestFocus();
            return;
        }

//        add pattern matcher for roll no
        if (userPass.length() < 8) {
            userPasswordEditText.setError("Password should be of 10 digits");
            userPasswordEditText.requestFocus();
            return;
        }

        if (userConPass.length() < 8) {
            userConPasswordEditText.setError("Password should be of 10 digits");
            userConPasswordEditText.requestFocus();
            return;
        }
        if (!userConPass.equals(userPass)) {
            userPasswordEditText.setError("Password do not match");
            userPasswordEditText.requestFocus();
            userPasswordEditText.setText("");
            userConPasswordEditText.setText("");
            return;
        }

        if(!termsCheckBox.isChecked()){
            Toast.makeText(getContext(), "You must agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

//        now register user with createuserwithemailpassword method call of firebase auth and the toast the message

        auth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.hide();
                    auth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth.getCurrentUser().sendEmailVerification();
//                    Toast.makeText(getContext(),"Email sent for Verification",Toast.LENGTH_LONG).show();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Email Sent!");
                                builder.setMessage("Verification Email sent to your account. Check your Email");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getContext(), "Login again after verification", Toast.LENGTH_LONG).show();
                                        userEmailEditText.setText("");
                                        userPasswordEditText.setText("");
                                        userConPasswordEditText.setText("");
                                        auth.getInstance().signOut();
                                    }
                                });
                                builder.show();

                            }
                            else{
                                if (task.getException() instanceof FirebaseNetworkException) {
                                    Toast.makeText(getContext(), "Internet connectivity required", Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    userEmailEditText.setError("Email ID is already in use");
                                    userEmailEditText.requestFocus();
                                } else {
                                    Toast.makeText(getContext(), "Some error occurred. Try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                } else {
                    progressDialog.hide();
                    if (task.getException() instanceof FirebaseNetworkException) {
                        Toast.makeText(getContext(), "Internet connectivity required", Toast.LENGTH_SHORT).show();
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        userEmailEditText.setError("Email ID is already in use");
                        userEmailEditText.requestFocus();
                    } else {
                        Toast.makeText(getContext(), "Some error occurred. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public interface OnFragmentInteractionListener {
    }
}