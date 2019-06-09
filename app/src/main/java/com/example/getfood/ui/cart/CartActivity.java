package com.example.getfood.ui.cart;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.getfood.Paytm;
import com.example.getfood.R;
import com.example.getfood.callback.SwipeToDeleteCallback;
import com.example.getfood.models.CartItem;
import com.example.getfood.models.FullOrder;
import com.example.getfood.ui.base.BaseActivity;
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity;
import com.example.getfood.ui.orderdetail.OrderDetailActivity;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.AppUtils;
import com.example.getfood.utils.DialogConfirmation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends BaseActivity implements View.OnClickListener, PaytmPaymentTransactionCallback {

    public static Activity activity = null;
    public static int total;
    static TextView totalPriceTV;
    RecyclerView cartRecyclerView;
    RecyclerView.Adapter cartRecyclerDisplayAdapter;
    Button orderButton;
    //    choose time views
    Button firstBreakButton, secondBreakButton, lastBreakButton, nowButton;
    DatabaseReference orderRoot, userOrderData;
    String orderTime = null;
    private AlertDialog chooseTimeDialog;
    private TextView tvCurrentDate;

    public static void calcTotal() {
//        int i = 0;
        total = 0;
        for (CartItem item : FoodMenuDisplayActivity.cartItems) {
            total = total + Integer.parseInt(item.getItemPrice()) * item.getItemQuantity();
        }
//        set alpha of total price textview to zero, and then animate it to increase to 1.0
        totalPriceTV.setAlpha(0.0f);
        totalPriceTV.setText(String.format(activity.getString(R.string.total_rs), String.valueOf(total)));
        totalPriceTV.animate().alpha(1.0f).setDuration(250);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_cart;
    }

    @Override
    public void initViews() {
        activity = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.my_cart);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_down);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setHasFixedSize(true);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.addItemDecoration(new DividerItemDecoration(cartRecyclerView.getContext(), LinearLayoutManager.VERTICAL));


        totalPriceTV = findViewById(R.id.totalPriceTV);
        orderButton = findViewById(R.id.orderButton);

//        setDisplayListView(getApplicationContext());
        CartRecyclerViewDisplayAdapter adapter = new CartRecyclerViewDisplayAdapter(FoodMenuDisplayActivity.cartItems, this);
        cartRecyclerDisplayAdapter = adapter;
        cartRecyclerView.setAdapter(cartRecyclerDisplayAdapter);
        calcTotal();
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(cartRecyclerView);
    }

    @Override
    public void setListeners() {
        orderButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        if (id == R.id.menu_clear_cart) {
            AlertUtils.showConfirmationDialog(mContext, getString(R.string.clear_cart),
                    getString(R.string.sure_clear_cart), getString(R.string.yes), getString(R.string.no), new DialogConfirmation.ConfirmationDialogListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            clearCart();
                        }

                        @Override
                        public void onNegativeButtonClicked() {

                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearCart() {
        FoodMenuDisplayActivity.cartItems.clear();
        makeText(getString(R.string.cart_cleared));
        onBackPressed();
    }

    private void chooseTime() {
        AlertDialog.Builder chooseTimeBuilder = new AlertDialog.Builder(CartActivity.this);
        chooseTimeBuilder.setTitle(R.string.choose_time);
        View chooseTimeView = getLayoutInflater().inflate(R.layout.choose_time_order, null);
        firstBreakButton = chooseTimeView.findViewById(R.id.firstBreakButton);
        secondBreakButton = chooseTimeView.findViewById(R.id.secondBreakButton);
        lastBreakButton = chooseTimeView.findViewById(R.id.lastBreakButton);
        nowButton = chooseTimeView.findViewById(R.id.nowButton);
        tvCurrentDate = chooseTimeView.findViewById(R.id.tvCurrentDate);

//        TODO: Confirm timings with Canteen
        Calendar currTime;
        currTime = Calendar.getInstance();
        tvCurrentDate.setText(AppUtils.getTodaysDate());
//        check if it is a sunday, then ordering is closed TODO: confirm with docs for the value of Sunday in Calendar
        currTime.set(Calendar.DAY_OF_WEEK, currTime.get(Calendar.DAY_OF_WEEK) - 1);
//        if (currTime.get(Calendar.DAY_OF_WEEK) == 7) {
//            makeText("Cannot order on Sunday, Order Tomorrow");
//            return;
//        }
        int hour = currTime.get(Calendar.HOUR_OF_DAY);
        int mins = currTime.get(Calendar.MINUTE);
        hour = 12;
        mins = 15;
        if (hour <= 8 && mins <= 20) {
            Log.d("Debug", "Before Ordering time");
            makeText("Cannot place order now, Order after 08:20 AM");
            return;
        } else if (hour <= 8 && mins <= 45) {
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

        chooseTimeBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        chooseTimeDialog = chooseTimeBuilder.show();

        Button nbutton = chooseTimeDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (FoodMenuDisplayActivity.cartItems.isEmpty()) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderButton:
                chooseTime();
                break;
            case R.id.firstBreakButton:
                orderTime = firstBreakButton.getText().toString();
                placeOrder();
                break;
            case R.id.secondBreakButton:
                orderTime = secondBreakButton.getText().toString();
                placeOrder();
                break;
            case R.id.lastBreakButton:
                orderTime = lastBreakButton.getText().toString();
                placeOrder();
                break;
            case R.id.nowButton:
                Date date = new Date();
                String strDateFormat = getString(R.string.date_format);
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.ENGLISH);
                orderTime = dateFormat.format(date);
                placeOrder();
                break;
            default:
                chooseTime();
                break;
        }
        //Before placing the order, PayTM Gateway will be called and the code will be shifted into it's method
    }

    private void placeOrder() {
        chooseTimeDialog.dismiss();
        showLoading();
        orderRoot = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order));
        userOrderData = FirebaseDatabase.getInstance().getReference().child("UserOrderData").child(mRollNo);

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
            hideLoading();
            makeText(getString(R.string.no_internet));
        }
    }

    private void generateOrder(final long orderID) {

//        TODO: make object of FullOrder with all the fields
        Collections.sort(FoodMenuDisplayActivity.cartItems, new Comparator<CartItem>() {
            @Override
            public int compare(CartItem o1, CartItem o2) {
                return o1.getItemCategory().length() - o2.getItemCategory().length();
            }
        });

        final String orderId = userOrderData.push().getKey();
        FullOrder fullOrder = new FullOrder(FoodMenuDisplayActivity.cartItems, String.valueOf(total), orderTime, mRollNo, orderId, null);

        if (orderId != null) {
            userOrderData.child(orderId).setValue(fullOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        userOrderData.child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FullOrder order = dataSnapshot.getValue(FullOrder.class);
                                if (order != null) {
                                    for (CartItem item : order.getOrderItems()) {
                                        Log.d("##DebugData", "\n" + item.getItemName() + " " + item.getItemQuantity() + " " + item.getItemCategory());
                                    }
                                    hideLoading();
                                    Intent orderIntent = new Intent(CartActivity.this, OrderDetailActivity.class);
                                    orderIntent.putExtra("TestOrderData", order);
                                    startActivity(orderIntent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                hideLoading();
                            }
                        });
                    } else {
                        hideLoading();
                    }
                }
            });
        }
        /*
        orderRoot.child(String.valueOf(orderID)).child(getString(R.string.total_amount)).setValue(String.valueOf(total));
        orderRoot.child(String.valueOf(orderID)).child(getString(R.string.time_to_deliver)).setValue(orderTime);
        orderRoot.child(String.valueOf(orderID)).child(getString(R.string.roll_no)).setValue(rollNo);
        for (int pos = 0; pos < FoodMenuDisplayActivity.cartItems.size(); pos++) {
            orderRoot.child(String.valueOf(orderID)).child(getString(R.string.items))
                    .child(FoodMenuDisplayActivity.cartItems.get(pos).getFoodItem().getItemCategory())
                    .child(FoodMenuDisplayActivity.cartItems.get(pos).getCartItemName())
                    .child(getString(R.string.quantity)).setValue(FoodMenuDisplayActivity.cartItems.get(pos).getItemQuantity());
            orderRoot.child(String.valueOf(orderID)).child(getString(R.string.items))
                    .child(FoodMenuDisplayActivity.cartItems.get(pos).getFoodItem().getItemCategory())
                    .child(FoodMenuDisplayActivity.cartItems.get(pos).getCartItemName())
                    .child(getString(R.string.status)).setValue(getString(R.string.received));
        }
//        orderRoot.child(String.valueOf(orderID)).child("Total Amount").setValue(String.valueOf(total));
//        orderRoot.child(String.valueOf(orderID)).child("Time to deliver").setValue(orderTime);
//        orderRoot.child(String.valueOf(orderID)).child("Roll No").setValue(rollNo);

//        store value of orderID for future reference
        root = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order_data));

        root.child(rollNo).child(String.valueOf(orderID)).child(getString(R.string.status)).setValue(getString(R.string.ordered));
        chooseTimeDialog.hide();
*/
        /*orderIntent = new Intent(CartActivity.this, OrderDetailActivity.class);
        orderIntent.putExtra(getString(R.string.i_order_id), String.valueOf(orderID));
        orderIntent.putExtra(getString(R.string.i_roll_no), rollNo);
        orderIntent.putExtra(getString(R.string.i_total), total);*/

//        FoodMenuDisplayActivity.cartItemName.clear();
//        FoodMenuDisplayActivity.cartItemCategory.clear();
//        FoodMenuDisplayActivity.cartItemQuantity.clear();
//        FoodMenuDisplayActivity.cartItemPrice.clear();
        FoodMenuDisplayActivity.cartItems.clear();

//        startActivity(orderIntent);

//        generateCheckSumVoley();
//        generate checksum from server and pass all details to paytm
//        launched order activity
    }

    private void generateCheckSumVoley() {

        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> map = new HashMap<>();
        map.put(getString(R.string.mid), "GetFoo88084336099945");
        map.put(getString(R.string.order_id), "osltry");
        map.put(getString(R.string.cust_id), "15bce001");
        map.put(getString(R.string.industry_id), getString(R.string.retail));
        map.put(getString(R.string.channel_id), getString(R.string.wap));
        map.put(getString(R.string.txn_amt), String.valueOf(total));
        map.put(getString(R.string.website), getString(R.string.staging));
        map.put(getString(R.string.callback), "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=osltry");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://adit-canteen-alay1012.c9users.io/paytm/generateChecksum.php", new JSONObject(map), new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            makeText(response.getString("CHECKSUMHASH"));
                            Log.d("response", "Our server Checksum was " + response.getString(getString(R.string.checksum)));
//                            call paytm activity with the checksum received
                            initializePaytmPayment(response.getString(getString(R.string.checksum)), null);
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
        paramMap.put(getString(R.string.mid), getString(R.string.mid_value));
        paramMap.put(getString(R.string.order_id), "osltry");
        paramMap.put(getString(R.string.cust_id), "15bce001");
        paramMap.put(getString(R.string.industry_id), getString(R.string.retail));
        paramMap.put(getString(R.string.channel_id), getString(R.string.wap));
        paramMap.put(getString(R.string.txn_amt), String.valueOf(total));
        paramMap.put(getString(R.string.website), getString(R.string.staging));
        paramMap.put(getString(R.string.callback), "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=osltry");
        paramMap.put(getString(R.string.checksum), checksumHash);


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
        Log.d("response", "Checksum from Paytm server was " + bundle.getString(getString(R.string.checksum)));

//        reseting the cart
//        FoodMenuDisplayActivity.cartItemName.clear();
//        FoodMenuDisplayActivity.cartItemCategory.clear();
//        FoodMenuDisplayActivity.cartItemQuantity.clear();
//        FoodMenuDisplayActivity.cartItemPrice.clear();
        FoodMenuDisplayActivity.cartItems.clear();

//        startActivity(orderIntent);
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_LONG).show();
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
        Toast.makeText(this, getString(R.string.back_pressed), Toast.LENGTH_LONG).show();

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
