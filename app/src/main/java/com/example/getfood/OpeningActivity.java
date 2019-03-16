package com.example.getfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.getfood.ui.loginregister.LoginActivity;

public class OpeningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

    }

    public void openingActivity(View view) {
        Intent in = new Intent(this, LoginActivity.class);
        startActivity(in);
    }
}
