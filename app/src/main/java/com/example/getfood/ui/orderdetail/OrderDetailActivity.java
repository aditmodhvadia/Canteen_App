package com.example.getfood.ui.orderdetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.ui.base.BaseActivity;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.AppUtils;
import com.example.getfood.utils.DialogSimple;
import com.fazemeright.canteen_app_models.models.FullOrder;

public class OrderDetailActivity extends BaseActivity implements OrderDetailMvpView, OrderDetailRecyclerViewDisplayAdapter.OnOrderItemClickListener {

    private TextView testTV, test;
    private RecyclerView orderRecyclerView;
    private OrderDetailRecyclerViewDisplayAdapter orderDisplayAdapter;
    private Intent orderData;
    private OrderDetailPresenter<OrderDetailActivity> presenter;
    private FullOrder fullOrder;

    @Override
    public void initViews() {
        testTV = findViewById(R.id.testTV);
        orderRecyclerView = findViewById(R.id.orderDetailRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        orderRecyclerView.addItemDecoration(new DividerItemDecoration(orderRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
        test = findViewById(R.id.test);

        presenter = new OrderDetailPresenter<>();
        presenter.onAttach(this);

//        Getting data from the calling activity/Intent
        orderData = getIntent();
        if (orderData.getExtras() == null || orderData.getExtras().isEmpty()) {
            AlertUtils.showAlertBox(mContext, getString(R.string.std_error_title), getString(R.string.std_error_msg),
                    getString(R.string.ok), new DialogSimple.AlertDialogListener() {
                        @Override
                        public void onButtonClicked() {
                            finish();
                        }
                    });
            return;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AppUtils.createNotificationChannel(mContext);
        showLoading();
//        Get data from Intent
        fullOrder = (FullOrder) getIntent().getSerializableExtra("TestOrderData");

//        orderDisplayAdapter = new OrderDetailRecyclerViewDisplayAdapter(fullOrder, mContext, this);
        orderDisplayAdapter = new OrderDetailRecyclerViewDisplayAdapter(this);

        orderRecyclerView.setAdapter(orderDisplayAdapter);
//        Log.d("##DebugData", fullOrder.toString());
        presenter.fetchOrderDetails(fullOrder);
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void bindOrderDetailAdapter(FullOrder updatedOrder) {
        this.fullOrder = updatedOrder;
        if (updatedOrder.getDisplayID() != null) {
            testTV.setText(String.format("%s %s", getString(R.string.order_id_is), updatedOrder.getDisplayID()));
        } else {
            testTV.setText(String.format("%s %s", getString(R.string.order_id_is), updatedOrder.getOrderId()));
        }
        Log.i("OrderDetailActivity", "Called submitlist");
        orderDisplayAdapter.swapData(updatedOrder.getOrderItems());
        hideLoading();
    }

    @Override
    public void onDatabaseError(Error error) {
        Toast.makeText(OrderDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRatingUpdatedSuccessfully() {
        Toast.makeText(OrderDetailActivity.this, getString(R.string.rating_saved), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRatingUpdateFailed(Error error) {
        Toast.makeText(OrderDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        orderData = intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRatingGiven(float rating, int position) {
        presenter.setRatingValueForOrderItem(rating, position, this.fullOrder.getOrderId());
    }
}
