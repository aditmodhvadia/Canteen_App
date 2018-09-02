package com.example.getfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView cartListView;
    CartDisplayAdapter cartDisplayAdapter;

    TextView totalPriceTV;
    Button orderButton;
//    alertdialog views
    Button alertPlus, alertMinus;
    TextView quantitySetTV;
    Boolean flag = false;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);

        totalPriceTV = findViewById(R.id.totalPriceTV);
        orderButton = findViewById(R.id.orderButton);

        setDisplayListView();

//        show dialog to adjust the quantity of items in the cart
        cartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder2 = new AlertDialog.Builder(CartActivity.this);
                View quantityAlert = getLayoutInflater().inflate(R.layout.adjust_quantity_display, null);
                alertPlus = quantityAlert.findViewById(R.id.alertPlus);
                alertMinus = quantityAlert.findViewById(R.id.alertMinus);
                quantitySetTV = quantityAlert.findViewById(R.id.quantitySetTextView);
                quantitySetTV.setText(FoodMenuDisplayActivity.cartItemQuantity.get(position).toString());
                alertPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Integer.parseInt(quantitySetTV.getText().toString())<20){
                            quantitySetTV.setText(String.valueOf(Integer.valueOf(quantitySetTV.getText().toString())+1));
                        }
                    }
                });
                alertMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Integer.parseInt(quantitySetTV.getText().toString())>0){
                            quantitySetTV.setText(String.valueOf(Integer.valueOf(quantitySetTV.getText().toString())-1));
                        }
                    }
                });

                builder2.setTitle("Select Quantity");
                builder2.setMessage(FoodMenuDisplayActivity.cartItemName.get(position));
                builder2.setView(quantityAlert);

                builder2.setPositiveButton("Adjust Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int quant = Integer.valueOf(quantitySetTV.getText().toString());
                        if(quant!=0){
                            FoodMenuDisplayActivity.cartItemQuantity.set(position,quant);
                            cartDisplayAdapter.notifyDataSetChanged();
                            totalPriceTV.setText("Total: Rs. " +String.valueOf(calcTotal()));
                            Toast.makeText(getApplicationContext(),"Cart adjusted",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            AlertDialog.Builder builderConfirm = new AlertDialog.Builder(CartActivity.this);
                            builderConfirm.setTitle("Are you sure you want to remove item?");

                            builderConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FoodMenuDisplayActivity.cartItemQuantity.remove(position);
                                    FoodMenuDisplayActivity.cartItemPrice.remove(position);
                                    FoodMenuDisplayActivity.cartItemName.remove(position);
                                    if(FoodMenuDisplayActivity.cartItemName.isEmpty()){
                                        Toast.makeText(getBaseContext(),"Cart is Empty",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    cartDisplayAdapter.notifyDataSetChanged();
                                    totalPriceTV.setText("Total: Rs. " +String.valueOf(calcTotal()));
                                    Toast.makeText(getApplicationContext(),"Cart adjusted",Toast.LENGTH_SHORT).show();
                                }
                            });

                            builderConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            AlertDialog dialogConfirm = builderConfirm.show();
                        }
                    }
                });

                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog2 = builder2.show();

            }
        });

    }

    private void setDisplayListView() {
        cartDisplayAdapter = new CartDisplayAdapter(FoodMenuDisplayActivity.cartItemName, FoodMenuDisplayActivity.cartItemQuantity,
                FoodMenuDisplayActivity.cartItemPrice, getApplicationContext());
        cartListView.setAdapter(cartDisplayAdapter);
//        calculate total of all the items in cart and display it
        totalPriceTV.setText("Total: Rs. " +String.valueOf(calcTotal()));
    }

    private int calcTotal() {
        int i=0;
        total=0;
        for (Integer price : FoodMenuDisplayActivity.cartItemPrice) {
            total = total + price*FoodMenuDisplayActivity.cartItemQuantity.get(i++);
        }
        return total;
    }
}
