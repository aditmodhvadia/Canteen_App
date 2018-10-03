package com.example.getfood.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.Activity.FoodMenuDisplayActivity;
import com.example.getfood.Activity.TermsActivity;
import com.example.getfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    EditText userConPasswordEditText, userPasswordEditText, userEmailEditText;
    TextView termsTextView;
    Button userAddButton;
    ProgressDialog progressDialog;
    int flag;

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

        userConPasswordEditText = (EditText) view.findViewById(R.id.userConPasswordEditText);
        userPasswordEditText = (EditText) view.findViewById(R.id.userPasswordEditText);
        userEmailEditText = (EditText) view.findViewById(R.id.userLoginEmailEditText);
        termsTextView = view.findViewById(R.id.termsTextView);


        userAddButton = (Button) view.findViewById(R.id.userAddButton);
//        verify all fields on button press
        userAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValues();
            }
        });

        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TermsActivity.class));

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}