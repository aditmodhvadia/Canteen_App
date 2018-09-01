package com.example.getfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<String> cartItemName;
    ArrayList<Integer> cartItemQuantity, cartItemPrice;
    ListView cartListView;
    CartDisplayAdapter cartDisplayAdapter;

    TextView totalPriceTV;
    Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);

        cartItemName = new ArrayList<>();
        cartItemPrice = new ArrayList<>();
        cartItemQuantity = new ArrayList<>();
        totalPriceTV = findViewById(R.id.totalPriceTV);
        orderButton = findViewById(R.id.orderButton);

        Intent i = getIntent();
        cartItemName = (ArrayList<String>) i.getExtras().getStringArrayList("name").clone();
        cartItemPrice = (ArrayList<Integer>) i.getExtras().getStringArrayList("price").clone();
        cartItemQuantity = (ArrayList<Integer>) i.getExtras().getStringArrayList("quantity").clone();

        cartDisplayAdapter = new CartDisplayAdapter(cartItemName, cartItemQuantity, cartItemPrice, getApplicationContext());
        cartListView.setAdapter(cartDisplayAdapter);

        totalPriceTV.setText("Total: Rs. " +String.valueOf(calcTotal()));
    }

    private int calcTotal() {
        int total = 0, i=0;
        for (Integer price : cartItemPrice) {
            total = total + price*cartItemQuantity.get(i++);
        }
        return total;
    }
}
