package com.example.getfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<String> cartItemName;
    ArrayList<Integer> cartItemQuantity, cartItemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartItemName = new ArrayList<>();
        cartItemPrice = new ArrayList<>();
        cartItemQuantity = new ArrayList<>();

        Intent i = getIntent();
        cartItemName = (ArrayList<String>) i.getExtras().getStringArrayList("name").clone();
        cartItemPrice = (ArrayList<Integer>) i.getExtras().getStringArrayList("price").clone();
        cartItemQuantity = (ArrayList<Integer>) i.getExtras().getStringArrayList("quantity").clone();
        Toast.makeText(getApplicationContext(),cartItemName.toString(),Toast.LENGTH_LONG).show();
    }
}
