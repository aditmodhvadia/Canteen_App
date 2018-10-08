package com.example.getfood.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.getfood.Adapter.CartRecyclerViewDisplayAdapter;
import com.example.getfood.Paytm;
import com.example.getfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, PaytmPaymentTransactionCallback {

    RecyclerView cartRecyclerView;
    RecyclerView.Adapter cartRecyclerDisplayAdapter;

    public static Activity activity = null;

    static TextView totalPriceTV;
    Button orderButton;
    Intent orderIntent;

    //    choose time views
    Button firstBreakButton, secondBreakButton, lastBreakButton, nowButton;
    AlertDialog chooseTimeDialog;

    public static int total;

    DatabaseReference orderRoot, root;
    FirebaseAuth auth;
    String rollNo;
    String orderTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        activity = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setHasFixedSize(true);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        totalPriceTV = findViewById(R.id.totalPriceTV);
        orderButton = findViewById(R.id.orderButton);

//        setDisplayListView(getApplicationContext());

        cartRecyclerDisplayAdapter = new CartRecyclerViewDisplayAdapter(FoodMenuDisplayActivity.cartItemName,FoodMenuDisplayActivity.cartItemQuantity, FoodMenuDisplayActivity.cartItemPrice, this);
        cartRecyclerView.setAdapter(cartRecyclerDisplayAdapter);
        calcTotal();

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
//        if (currTime.get(Calendar.DAY_OF_WEEK) == 7) {
//            makeText("Cannot order on Sunday, Order Tomorrow");
//            return;
//        }
        int hour = currTime.get(Calendar.HOUR_OF_DAY);
        int mins = currTime.get(Calendar.MINUTE);
        hour = 12; mins =15;
        if (hour < 8 || (hour == 8 && mins <= 20)) {
            Log.d("Debug", "Before Ordering time");
            makeText("Cannot place order now, Order after 08:20 AM");
            return;
        } else if (hour < 8 || (hour == 8 && mins <= 45)) {
            Log.d("Debug", "Between 8:20 and 8:45");
            nowButton.setEnabled(false);
            nowButton.setBackgroundResource(R.drawable.button_disabled);
        } else if (hour < 10 || (hour == 10 && mins <= 50)) {
            Log.d("Debug", "Before first break");
        } else if (hour < 13 || (hour == 13 && mins < 15)) {
            Log.d("Debug", "Before second break");
            firstBreakButton.setEnabled(false);
            firstBreakButton.setBackgroundResource(R.drawable.button_disabled);
        } else if (hour < 16 || (hour == 16 && mins < 15)) {
            Log.d("Debug", " time");
            firstBreakButton.setEnabled(false);
            secondBreakButton.setEnabled(false);
            firstBreakButton.setBackgroundResource(R.drawable.button_disabled);
            secondBreakButton.setBackgroundResource(R.drawable.button_disabled);
        } else if (hour < 17 || (hour == 17 && mins <= 45)) {
            Log.d("Debug", "after 4:15");
            firstBreakButton.setEnabled(false);
            secondBreakButton.setEnabled(false);
            lastBreakButton.setEnabled(false);
            firstBreakButton.setBackgroundResource(R.drawable.button_disabled);
            secondBreakButton.setBackgroundResource(R.drawable.button_disabled);
            lastBreakButton.setBackgroundResource(R.drawable.button_disabled);

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

    public static void calcTotal() {
        int i = 0;
        total = 0;
        for (Integer price : FoodMenuDisplayActivity.cartItemPrice) {
            total = total + price * FoodMenuDisplayActivity.cartItemQuantity.get(i++);
        }
//        set alpha of total price textview to zero, and then animate it to increase to 1.0
        totalPriceTV.setAlpha(0.0f);
        totalPriceTV.setText(String.format("Total: ₹ %s", String.valueOf(total)));
        totalPriceTV.animate().alpha(1.0f).setDuration(250);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

        orderRoot.child(String.valueOf(orderID)).child("Total Amount").setValue(String.valueOf(total));
        orderRoot.child(String.valueOf(orderID)).child("Time to deliver").setValue(orderTime);
        orderRoot.child(String.valueOf(orderID)).child("Roll No").setValue(rollNo);
        for (int pos = 0; pos < FoodMenuDisplayActivity.cartItemName.size(); pos++) {
            orderRoot.child(String.valueOf(orderID)).child("Items").child(FoodMenuDisplayActivity.cartItemCategory.get(pos)).child(FoodMenuDisplayActivity.cartItemName.get(pos))
                    .child("Quantity").setValue(FoodMenuDisplayActivity.cartItemQuantity.get(pos));
            orderRoot.child(String.valueOf(orderID)).child("Items").child(FoodMenuDisplayActivity.cartItemCategory.get(pos)).child(FoodMenuDisplayActivity.cartItemName.get(pos))
                    .child("Status").setValue("Received");
        }
//        orderRoot.child(String.valueOf(orderID)).child("Total Amount").setValue(String.valueOf(total));
//        orderRoot.child(String.valueOf(orderID)).child("Time to deliver").setValue(orderTime);
//        orderRoot.child(String.valueOf(orderID)).child("Roll No").setValue(rollNo);

//        store value of orderID for future reference
        root = FirebaseDatabase.getInstance().getReference().child("OrderData");

        root.child(rollNo).child(String.valueOf(orderID)).child("Status").setValue("Ordered");
        chooseTimeDialog.hide();

        orderIntent = new Intent(CartActivity.this, OrderActivity.class);
        orderIntent.putExtra("OrderID", String.valueOf(orderID));
        orderIntent.putExtra("RollNo", rollNo);
        orderIntent.putExtra("Total", total);

        FoodMenuDisplayActivity.cartItemName.clear();
        FoodMenuDisplayActivity.cartItemCategory.clear();
        FoodMenuDisplayActivity.cartItemQuantity.clear();
        FoodMenuDisplayActivity.cartItemPrice.clear();

//        startActivity(orderIntent);
//
        generateCheckSumVoley();
//        generate checksum from server and pass all details to paytm
//        launched order activity
    }

    private void generateCheckSumVoley() {

        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("MID", "GetFoo88084336099945");
        map.put("ORDER_ID", "review2finanl");
        map.put("CUST_ID", "15bce001");
        map.put("INDUSTRY_TYPE_ID", "Retail");
        map.put("CHANNEL_ID", "WAP");
        map.put("TXN_AMOUNT", String.valueOf(total));
        map.put("WEBSITE", "APPSTAGING");
        map.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=review2finanl");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://adit-canteen-alay1012.c9users.io/paytm/generateChecksum.php", new JSONObject(map), new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            makeText(response.getString("CHECKSUMHASH"));
                            Log.d("response", "Our server Checksum was " + response.getString("CHECKSUMHASH"));
//                            call paytm activity with the checksum received
                            initializePaytmPayment(response.getString("CHECKSUMHASH"), null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        makeText("Did not work");
//                        Log.d("newerror", error.networkResponse.toString());
                    }
                });

        queue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
//        dummy values as of now for testing purposes
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", "GetFoo88084336099945");
        paramMap.put("ORDER_ID", "review2finanl");
        paramMap.put("CUST_ID", "15bce001");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", String.valueOf(total));
        paramMap.put("WEBSITE", "APPSTAGING");
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=review2finanl");
        paramMap.put("CHECKSUMHASH", checksumHash);


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {

//        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();
        Log.d("PayTM", bundle.toString());
        Log.d("response", "Checksum from Paytm server was " + bundle.getString("CHECKSUMHASH"));

//        reseting the cart
        FoodMenuDisplayActivity.cartItemName.clear();
        FoodMenuDisplayActivity.cartItemCategory.clear();
        FoodMenuDisplayActivity.cartItemQuantity.clear();
        FoodMenuDisplayActivity.cartItemPrice.clear();

        startActivity(orderIntent);
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        Log.d("PayTM", s);
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        Log.d("PayTM", s);
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        Log.d("PayTM", s + " " + s1);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
        Log.d("PayTM", bundle.toString());
    }

    public void makeText(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
