package com.example.getfood.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.getfood.Activity.TermsActivity;
import com.example.getfood.R;
import com.example.getfood.Utils.AlertUtils;
import com.example.getfood.Utils.OnDialogButtonClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class RegisterFragment extends Fragment {

    EditText userConPasswordEditText, userPasswordEditText, userEmailEditText;
    TextView termsTextView;
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

        getActivity().setTitle(R.string.register);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());

        userConPasswordEditText = view.findViewById(R.id.userConPasswordEditText);
        userPasswordEditText = view.findViewById(R.id.userPasswordEditText);
        userEmailEditText = view.findViewById(R.id.userLoginEmailEditText);
        termsTextView = view.findViewById(R.id.termsTextView);


        userAddButton = view.findViewById(R.id.userAddButton);
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
            userEmailEditText.setError(getString(R.string.email_required));
            userEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            userEmailEditText.setError(getString(R.string.enter_valid_email));
            userEmailEditText.requestFocus();
            return;
        }

        if (!userEmail.contains(getString(R.string.nirma_domain))) {
            userEmailEditText.setError(getString(R.string.enter_valid_nirma_domain));
            userEmailEditText.requestFocus();
            return;
        }

        if (userPass.isEmpty()) {
            userPasswordEditText.setError(getString(R.string.password_required));
            userPasswordEditText.requestFocus();
            return;
        }
        if (userConPass.isEmpty()) {
            userConPasswordEditText.setError(getString(R.string.confirm_password_required));
            userConPasswordEditText.requestFocus();
            return;
        }

//        add pattern matcher for roll no
        if (userPass.length() < 8) {
            userPasswordEditText.setError(getString(R.string.password_more_than_8));
            userPasswordEditText.requestFocus();
            return;
        }

        if (userConPass.length() < 8) {
            userConPasswordEditText.setError(getString(R.string.password_more_than_8));
            userConPasswordEditText.requestFocus();
            return;
        }
        if (!userConPass.equals(userPass)) {
            userPasswordEditText.setError(getString(R.string.password_do_not_match));
            userPasswordEditText.requestFocus();
            userPasswordEditText.setText("");
            userConPasswordEditText.setText("");
            return;
        }

        progressDialog.setMessage(getString(R.string.registering));
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
                                AlertUtils.openAlertDialog(getContext(), getString(R.string.email_sent), "Verification Email sent to your account. Check your Email",
                                        getString(R.string.yes), getString(R.string.no), new OnDialogButtonClickListener() {
                                            @Override
                                            public void onPositiveButtonClicked() {
                                                Toast.makeText(getContext(), "Login again after verification", Toast.LENGTH_LONG).show();
                                                userEmailEditText.setText("");
                                                userPasswordEditText.setText("");
                                                userConPasswordEditText.setText("");
                                                auth.getInstance().signOut();
                                            }

                                            @Override
                                            public void onNegativeButtonClicked() {

                                            }
                                        });
                            } else {
                                if (task.getException() instanceof FirebaseNetworkException) {
                                    Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    userEmailEditText.setError(getString(R.string.email_in_use));
                                    userEmailEditText.requestFocus();
                                } else {
                                    Toast.makeText(getContext(), getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                } else {
                    progressDialog.hide();
                    if (task.getException() instanceof FirebaseNetworkException) {
                        Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        userEmailEditText.setError(getString(R.string.email_in_use));
                        userEmailEditText.requestFocus();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();
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