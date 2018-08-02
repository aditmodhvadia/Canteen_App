package com.example.getfood;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference rootFood;
    ProgressDialog progressDialog;

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = findViewById(R.id.test);

        progressDialog = new ProgressDialog(MainActivity.this);

        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Fetching data");
        progressDialog.show();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        rootFood = FirebaseDatabase.getInstance().getReference().child("Food");

        rootFood.keepSynced(true);

        rootFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                test.setText(dataSnapshot.child("Chinese").getKey().toString());

                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
