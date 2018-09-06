package com.example.getfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {

    TextView testTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        testTV = findViewById(R.id.testTV);
        Intent orderData = getIntent();

        testTV.setText(String.valueOf(orderData.getExtras().getLong("OrderID")));
    }
}
