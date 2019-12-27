package com.example.getfood.ui.cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.canteen_app_models.models.CartItem;
import com.example.canteen_app_models.models.FullOrder;
import com.example.getfood.Paytm;
import com.example.getfood.R;
import com.example.getfood.callback.CartItemTouchListener;
import com.example.getfood.callback.SwipeToDeleteCallback;
import com.example.getfood.ui.base.BaseActivity;
import com.example.getfood.ui.orderdetail.OrderDetailActivity;
import com.example.getfood.utils.AppUtils;
import com.example.getfood.utils.alert.AlertUtils;
import com.example.getfood.utils.alert.DialogConfirmation;
import com.example.getfood.utils.alert.DialogSimple;
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
import java.util.Locale;
import java.util.Map;

public class CartActivity extends BaseActivity implements CartMvpView, View.OnClickListener, CartItemTouchListener, PaytmPaymentTransactionCallback {

    public static int total;
    private TextView totalPriceTV;
    private RecyclerView cartRecyclerView;
    private Button orderButton;
    private CartDisplayAdapter adapter;
    private Button firstBreakButton, secondBreakButton, lastBreakButton, nowButton;
    private String orderTime = null;
    private AlertDialog chooseTimeDialog;
    private TextView tvCurrentDate;
    private CartPresenter<CartActivity> presenter;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_cart;
    }

    @Override
    public void initViews() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

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

        presenter = new CartPresenter<>();
        presenter.onAttach(this);

        adapter = new CartDisplayAdapter(this, this);
        adapter.swapData(presenter.getCartItems());
        cartRecyclerView.setAdapter(adapter);
        updateCartTotal();

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(cartRecyclerView);
    }

    private void updateCartTotal() {
        totalPriceTV.setAlpha(0.0f);
        totalPriceTV.setText(String.valueOf(presenter.getCartTotal()));
        totalPriceTV.animate().alpha(1.0f).setDuration(250);

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
                            makeShortText(getString(R.string.cart_cleared));
                            onBackPressed();
                        }

                        @Override
                        public void onNegativeButtonClicked() {

                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearCart() {
        presenter.clearCartItems();
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
//            makeShortText("Cannot order on Sunday, Order Tomorrow");
//            return;
//        }
        int hour = currTime.get(Calendar.HOUR_OF_DAY);
        int mins = currTime.get(Calendar.MINUTE);
        hour = 12;
        mins = 15;
        if (hour <= 8 && mins <= 20) {
            Log.d("Debug", "Before Ordering time");
            makeShortText("Cannot place order now, Order after 08:20 AM");
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
            makeShortText("Cannot place order now, Order tomorrow");
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
        if (presenter.getCartItems().size() == 0) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.orderButton:
            default:
                chooseTime();
                break;
        }
        //Before placing the order, PayTM Gateway will be called and the code will be shifted into it's method
    }

    private void placeOrder() {
        chooseTimeDialog.dismiss();
        showLoading();

//        TODO: generate unique Order ID and redirect user to Order Activity, before that accept the payment.

//        check for network connectivity and proceed with order

        if (isNetworkConnected()) {
            generateOrder();
        } else {
            hideLoading();
            makeShortText(getString(R.string.no_internet));
        }
    }

    private void generateOrder() {
//        TODO: make object of FullOrder with all the fields
        presenter.sortCartItems();

        final String orderId = presenter.getNewOrderKey();

        if (orderId != null) {

            FullOrder fullOrder = new FullOrder(presenter.getCartItems(), String.valueOf(presenter.getCartTotal()),
                    orderTime, presenter.getRollNo(), orderId, null);
            presenter.setNewOrder(fullOrder);

        } else {
            hideLoading();
            AlertUtils.showAlertBox(mContext, "Some Error occurred!",
                    "Please try again later", getString(R.string.ok), new DialogSimple.AlertDialogListener() {
                        @Override
                        public void onButtonClicked() {

                        }
                    });
        }
    }

    @Override
    public void onIncreaseClicked(int position) {
        presenter.increaseCartQuantity(position);
        updateCartTotal();
        adapter.itemChanged(position);
        Toast.makeText(mContext, getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDecreaseClicked(int position) {
        presenter.decreaseCartItemQuantity(position);
        updateCartTotal();
        Toast.makeText(mContext, getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
        adapter.itemChanged(position);
    }

    @Override
    public void onItemRemoved(int position) {
        presenter.removeCartItem(position);
        updateCartTotal();
        adapter.itemRemoved(position);
        if (adapter.getCartItemsCount() == 0) {
            presenter.clearCartItems();
            finish();
        }
    }

    @Override
    public void onItemRemoveUndo(CartItem removedItem, int mRecentlyDeletedItemPosition) {
        presenter.undoCartItemRemove(removedItem, mRecentlyDeletedItemPosition);
        updateCartTotal();
        adapter.itemInserted(mRecentlyDeletedItemPosition);
    }

    @Override
    public void onOrderPlacedSuccessfully(String orderId) {
        presenter.getOrderData(orderId);
    }

    @Override
    public void onOrderDataFetchedSuccessfully(FullOrder fullOrder) {
        hideLoading();
        if (fullOrder != null) {
            hideLoading();
            Intent orderIntent = new Intent(CartActivity.this, OrderDetailActivity.class);
            orderIntent.putExtra("TestOrderData", fullOrder);
            startActivity(orderIntent);
        }
    }

    @Override
    public void onOrderFailed(Error error) {
        AlertUtils.showAlertBox(mContext, "Some error occurred!", error.getMessage(),
                getString(R.string.ok), new DialogSimple.AlertDialogListener() {
                    @Override
                    public void onButtonClicked() {

                    }
                });
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
//                            makeShortText(response.getString("CHECKSUMHASH"));
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
                        makeShortText("Did not work");
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

//        Toast.makeShortText(this, bundle.toString(), Toast.LENGTH_LONG).show();
        Log.d("PayTM", bundle.toString());
        Log.d("response", "Checksum from Paytm server was " + bundle.getString(getString(R.string.checksum)));

        clearCart();
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

    public void makeShortText(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
