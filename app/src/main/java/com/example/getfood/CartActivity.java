package com.example.getfood;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

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

    DatabaseReference orderRoot, root;
    FirebaseAuth auth;
    String rollNo;
    String orderTime = null;

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
                        if (Integer.parseInt(quantitySetTV.getText().toString()) < 20) {
                            quantitySetTV.setText(String.valueOf(Integer.valueOf(quantitySetTV.getText().toString()) + 1));
                        }
                    }
                });
                alertMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Integer.parseInt(quantitySetTV.getText().toString()) > 0) {
                            quantitySetTV.setText(String.valueOf(Integer.valueOf(quantitySetTV.getText().toString()) - 1));
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
                        if (quant != 0) {
                            FoodMenuDisplayActivity.cartItemQuantity.set(position, quant);
                            cartDisplayAdapter.notifyDataSetChanged();
                            totalPriceTV.setText("Total: Rs. " + String.valueOf(calcTotal()));
                            makeText("Cart Adjusted");
                        } else {
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
                                    if (FoodMenuDisplayActivity.cartItemName.isEmpty()) {
                                        Toast.makeText(getBaseContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    cartDisplayAdapter.notifyDataSetChanged();
                                    totalPriceTV.setText("Total: Rs. " + String.valueOf(calcTotal()));
                                    makeText("Cart Adjusted");
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

//        TODO: Confirm timings with Canteen
        Calendar currTime;
        currTime = Calendar.getInstance();
//        check if it is a sunday, then ordering is closed TODO: confirm with docs for the value of Sunday in Calendar
        currTime.set(Calendar.DAY_OF_WEEK, currTime.get(Calendar.DAY_OF_WEEK) - 1);
        if (currTime.get(Calendar.DAY_OF_WEEK) == 7) {
            makeText("Cannot order on Sunday, Order Tomorrow" + currTime.get(Calendar.DAY_OF_WEEK));
            return;
        }
        int hour = currTime.get(Calendar.HOUR_OF_DAY);
        int mins = currTime.get(Calendar.MINUTE);

        if (hour < 8 || (hour == 8 && mins <= 20)) {
            Log.d("Debug", "Before Ordering time");
            makeText("Cannot place order now, Order after 08:20 AM");
            return;
        } else if (hour < 8 || (hour == 8 && mins <= 45)) {
            Log.d("Debug", "Between 8:20 and 8:45");
            nowButton.setEnabled(false);
        } else if (hour < 10 || (hour == 10 && mins <= 50)) {
            Log.d("Debug", "Before first break");
        } else if (hour < 13 || (hour == 13 && mins < 15)) {
            Log.d("Debug", "Before second break");
            firstBreakButton.setEnabled(false);
        } else if (hour < 16 || (hour == 16 && mins < 15)) {
            Log.d("Debug", " time");
            firstBreakButton.setEnabled(false);
            secondBreakButton.setEnabled(false);
        } else if (hour < 17 || (hour == 17 && mins <= 45)) {
            Log.d("Debug", "after 4:15");
            firstBreakButton.setEnabled(false);
            secondBreakButton.setEnabled(false);
            lastBreakButton.setEnabled(false);

        } else {
            makeText("Cannot place order now, Order tomorrow");
            return;
        }
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
        totalPriceTV.setText("Total: Rs. " + String.valueOf(calcTotal()));
    }

    private int calcTotal() {
        int i = 0;
        total = 0;
        for (Integer price : FoodMenuDisplayActivity.cartItemPrice) {
            total = total + price * FoodMenuDisplayActivity.cartItemQuantity.get(i++);
        }
        return total;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (FoodMenuDisplayActivity.cartItemName.isEmpty()) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        //Before placing the order, PayTM Gateway will be called and the code will be shifted into it's method

        placeOrder(v);

    }

    private void placeOrder(View v) {

        auth = FirebaseAuth.getInstance();
        orderRoot = FirebaseDatabase.getInstance().getReference().child("Order");
        String email = auth.getCurrentUser().getEmail();
        rollNo = email.substring(0, email.indexOf("@"));

        switch (v.getId()) {
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

        orderRoot.keepSynced(true);

//        TODO: generate unique Order ID and redirect user to Order Activity, before that accept the payment.

//        check for network connectivity and proceed with order
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            orderRoot.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long orderID = dataSnapshot.getChildrenCount();
                    generateOrder(++orderID);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            makeText("No Internet");
        }
    }

    private void generateOrder(long orderID) {

        for (int pos = 0; pos < FoodMenuDisplayActivity.cartItemName.size(); pos++) {
            orderRoot.child(String.valueOf(orderID)).child(rollNo).child(FoodMenuDisplayActivity.cartItemCategory.get(pos)).child(FoodMenuDisplayActivity.cartItemName.get(pos))
                    .child("Quantity").setValue(FoodMenuDisplayActivity.cartItemQuantity.get(pos));
        }
        orderRoot.child(String.valueOf(orderID)).child(rollNo).child("Total Amount").setValue(String.valueOf(calcTotal()));
        orderRoot.child(String.valueOf(orderID)).child(rollNo).child("Time to deliver").setValue(orderTime);

//        store value of orderID for future reference
        root = FirebaseDatabase.getInstance().getReference().child("OrderData");

        root.child(rollNo).child(String.valueOf(orderID)).child("Status").setValue("Ordered");
        chooseTimeDialog.hide();

        Intent orderIntent = new Intent(CartActivity.this, OrderActivity.class);
        orderIntent.putExtra("OrderID", String.valueOf(orderID));
        orderIntent.putExtra("RollNo", rollNo);
        orderIntent.putExtra("Total", calcTotal());


//        reseting the cart
        FoodMenuDisplayActivity.cartItemName.clear();
        FoodMenuDisplayActivity.cartItemCategory.clear();
        FoodMenuDisplayActivity.cartItemQuantity.clear();
        FoodMenuDisplayActivity.cartItemPrice.clear();


        startActivity(orderIntent);
//        launched order activity
    }



    public void makeText(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
