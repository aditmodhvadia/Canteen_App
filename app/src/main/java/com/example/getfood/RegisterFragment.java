package com.example.getfood;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

    EditText userFNameEditText, userLNameEditText, userPhoneNoEditText, userEmailEditText, userAddressEditText;
    EditText userRollNoEditText;
    Button userAddButton;
    ProgressDialog progressDialog;
    int flag;

    private FirebaseAuth auth;
    private FirebaseUser newOfficer;
    private DatabaseReference dbnewofficer;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Register");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());

        userFNameEditText = (EditText) view.findViewById(R.id.userFNameEditText);
        userLNameEditText = (EditText) view.findViewById(R.id.userLNameEditText);
        userPhoneNoEditText = (EditText) view.findViewById(R.id.userPhoneNoEditText);
        userEmailEditText = (EditText) view.findViewById(R.id.userLoginEmailEditText);
        userAddressEditText = (EditText) view.findViewById(R.id.userAddressEditText);
        userRollNoEditText = (EditText) view.findViewById(R.id.userRollNoEditText);


        userAddButton = (Button) view.findViewById(R.id.userAddButton);
//        verify all fields on button press
//        userAddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkValues();
//            }
//        });
    }

    private void checkValues() {
        final String userFName, userLName, userPhoneNo, userEmail, userAddress, userRollNo;

        userFName = userFNameEditText.getText().toString().trim();
        userLName = userLNameEditText.getText().toString().trim();
        userPhoneNo = userPhoneNoEditText.getText().toString().trim();
        userEmail = userEmailEditText.getText().toString().trim().toLowerCase();
        userAddress = userAddressEditText.getText().toString().trim();
        userRollNo = userRollNoEditText.getText().toString().trim();

        //Validating all entries First

        if (userFName.isEmpty()) {
            userFNameEditText.setError("First Name Required");
            userFNameEditText.requestFocus();
            return;
        }
        if (userLName.isEmpty()) {
            userLNameEditText.setError("Last Name Required");
            userLNameEditText.requestFocus();
            return;
        }
        if (userRollNo.isEmpty()) {
            userRollNoEditText.setError("Aadhaar Number Required");
            userRollNoEditText.requestFocus();
            return;
        }

//        add pattern matcher for roll no

        if (userPhoneNo.isEmpty()) {
            userPhoneNoEditText.setError("Phone Number Required");
            userPhoneNoEditText.requestFocus();
            return;
        }
        if (userPhoneNo.length() != 10) {
            userPhoneNoEditText.setError("Phone Number should be of 10 digits");
            userPhoneNoEditText.requestFocus();
            return;
        }
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

        if (userAddress.isEmpty()) {
            userAddressEditText.setError("Address Required");
            userAddressEditText.requestFocus();
            return;
        }

        progressDialog.setMessage("Adding Officer");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        //now register user with createuserwithemailpassword method call of firebase auth and the toast the message

        flag = 1;

        if (flag == 0) {
            return;
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
