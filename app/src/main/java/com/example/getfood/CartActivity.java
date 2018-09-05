package com.example.getfood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CartActivity extends AppCompatActivity implements View.OnClickListener{

    ListView cartListView;
    CartDisplayAdapter cartDisplayAdapter;

    TextView totalPriceTV;
    Button orderButton;
//  alertdialog views
    Button alertPlus, alertMinus;
    TextView quantitySetTV;
    AlertDialog chooseTimeDialog;

//    choose time views
    Button firstBreakButton, secondBreakButton, lastBreakButton, nowButton;

    private int total;

    DatabaseReference orderRoot;
    FirebaseAuth auth;

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

                AlertDialog.Builder setQuantityBuilder = new AlertDialog.Builder(CartActivity.this);
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

                setQuantityBuilder.setTitle("Select Quantity");
                setQuantityBuilder.setMessage(FoodMenuDisplayActivity.cartItemName.get(position));
                setQuantityBuilder.setView(quantityAlert);

                setQuantityBuilder.setPositiveButton("Adjust Cart", new DialogInterface.OnClickListener() {
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
//                            quantity is set to 0, hence confirm before removing the item
                            AlertDialog.Builder confirmRemoveItemBuilder = new AlertDialog.Builder(CartActivity.this);
                            confirmRemoveItemBuilder.setTitle("Are you sure you want to remove item?");

                            confirmRemoveItemBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FoodMenuDisplayActivity.cartItemQuantity.remove(position);
                                    FoodMenuDisplayActivity.cartItemPrice.remove(position);
                                    FoodMenuDisplayActivity.cartItemName.remove(position);
                                    FoodMenuDisplayActivity.cartItemCategory.remove(position);
                                    if(FoodMenuDisplayActivity.cartItemName.isEmpty()){
                                        Toast.makeText(getBaseContext(),"Cart is Empty",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    cartDisplayAdapter.notifyDataSetChanged();
                                    totalPriceTV.setText("Total: Rs. " +String.valueOf(calcTotal()));
                                    Toast.makeText(getApplicationContext(),"Cart adjusted",Toast.LENGTH_SHORT).show();
                                }
                            });

                            confirmRemoveItemBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            AlertDialog dialogConfirmQuantitySet = confirmRemoveItemBuilder.show();
                        }
                    }
                });

                setQuantityBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog2 = setQuantityBuilder.show();
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 chooseTime();
            }
        });

    }

    private void chooseTime() {
        AlertDialog.Builder chooseTimeBuilder = new AlertDialog.Builder(CartActivity.this);
        chooseTimeBuilder.setTitle("Choose a Time!");
        View chooseTimeView = getLayoutInflater().inflate(R.layout.choose_time_order, null);

        firstBreakButton = chooseTimeView.findViewById(R.id.firstBreakButton);
        secondBreakButton = chooseTimeView.findViewById(R.id.secondBreakButton);
        lastBreakButton = chooseTimeView.findViewById(R.id.lastBreakButton);
        nowButton = chooseTimeView.findViewById(R.id.nowButton);
        firstBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        chooseTimeBuilder.setView(chooseTimeView);
        firstBreakButton.setOnClickListener(this);
        secondBreakButton.setOnClickListener(this);
        lastBreakButton.setOnClickListener(this);
        nowButton.setOnClickListener(this);

        chooseTimeBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        chooseTimeDialog = chooseTimeBuilder.show();

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

    @Override
    public void onClick(View v) {
//        TODO:check whether the times listed have passed with respect to the current time and disable those button/options
        auth = FirebaseAuth.getInstance();
        orderRoot = FirebaseDatabase.getInstance().getReference().child("Order");
        String email = auth.getCurrentUser().getEmail();
        String rollNo = email.substring(0,email.indexOf("@"));
        String orderTime = null;


        switch(v.getId()){
            case R.id.firstBreakButton:
                orderTime = firstBreakButton.getText().toString();
                break;
            case R.id.secondBreakButton:
                orderTime = secondBreakButton.getText().toString();
                break;
            case R.id.lastBreakButton:
                orderTime = lastBreakButton.getText().toString();
                break;
            case R.id.nowButton:
                Date date = new Date();
                String strDateFormat = "hh:mm a";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                orderTime = dateFormat.format(date);
                break;
        }

        Log.d("debug",rollNo);
        Log.d("debug",FoodMenuDisplayActivity.cartItemCategory.get(0));
        Log.d("debug",FoodMenuDisplayActivity.cartItemName.get(0));
        Log.d("debug",FoodMenuDisplayActivity.cartItemPrice.get(0).toString());
        Log.d("debug",FoodMenuDisplayActivity.cartItemQuantity.get(0).toString());
        Log.d("debug",orderTime);

        orderRoot.keepSynced(true);

//        TODO: generate unique Order ID and redirect user to Order Activity, before that accept the payment.

//        check for network connectivity and proceed with order
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            for(int pos = 0; pos < FoodMenuDisplayActivity.cartItemName.size(); pos++){
                orderRoot.child(rollNo).child(FoodMenuDisplayActivity.cartItemCategory.get(pos)).child(FoodMenuDisplayActivity.cartItemName.get(pos))
                        .child("Quantity").setValue(FoodMenuDisplayActivity.cartItemQuantity.get(pos));
            }
            orderRoot.child(rollNo).child("Total Amount").setValue(String.valueOf(calcTotal()));
            orderRoot.child(rollNo).child("Time to deliver").setValue(orderTime);

            chooseTimeDialog.hide();

            Toast.makeText(getApplicationContext(),"Order Placed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"No Internet", Toast.LENGTH_SHORT).show();
        }



    }
}
