package com.example.getfood.ui.orderdetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.Models.OrderDetailItem;
import com.example.getfood.R;
import com.example.getfood.Utils.AppUtils;
import com.example.getfood.service.OrderNotificationService;
import com.example.getfood.ui.base.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class OrderDetailActivity extends BaseActivity implements OrderDetailMvpView {

    private TextView testTV, test;
    private ListView orderListView;
    private OrderDisplayAdapter orderDisplayAdapter;
    private int orderTotal;
    private String orderID, rollNo, orderTime, orderTotalPrice;
    private Intent orderData;
    private OrderDetailPresenter<OrderDetailActivity> presenter;
    private ArrayList<OrderDetailItem> orderDetailItems;

    @Override
    public void initViews() {
        testTV = findViewById(R.id.testTV);
        orderListView = findViewById(R.id.orderListView);
        test = findViewById(R.id.test);

        presenter = new OrderDetailPresenter<>();
        presenter.onAttach(this);

//        Getting data from the calling activity/Intent
        orderData = getIntent();
        if (orderData.getExtras().isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_data_received), Toast.LENGTH_SHORT).show();
            return;
        }
        AppUtils.createNotificationChannel(mContext);
//        Get data from Intent
        orderID = orderData.getExtras().getString(getString(R.string.i_order_id));
        orderTotal = orderData.getExtras().getInt(getString(R.string.i_total));
        rollNo = orderData.getExtras().getString(getString(R.string.i_roll_no));

        orderDetailItems = new ArrayList<>();
        orderDisplayAdapter = new OrderDisplayAdapter(orderDetailItems, getApplicationContext());
        orderListView.setAdapter(orderDisplayAdapter);
        showLoading();
        presenter.fetchOrderDetails(orderID);


        testTV.setText(String.format("%s%s", getString(R.string.order_id_is), orderID));
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_detail;
    }

    /*private void updateRating(final float rating, String itemName, String itemCategory) {

        final DatabaseReference foodItems = FirebaseDatabase.getInstance().getReference().child(getString(R.string.food)).child(itemCategory).child(itemName);
        foodItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float currRating = Float.valueOf(dataSnapshot.child(getString(R.string.rating)).getValue().toString());
                int numberOfRating = Integer.valueOf(dataSnapshot.child(getString(R.string.no_of_rating)).getValue().toString());
                float newRating = (currRating * numberOfRating++ + rating) / numberOfRating;
                foodItems.child(getString(R.string.rating)).setValue(newRating);
                foodItems.child(getString(R.string.no_of_rating)).setValue(numberOfRating);
                Toast.makeText(OrderDetailActivity.this, getString(R.string.rating_saved), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void bindOrderDetailAdapter(final ArrayList<OrderDetailItem> orderDetailItems, final DataSnapshot dataSnapshot) {
        hideLoading();
        orderDisplayAdapter.updateOrderList(orderDetailItems);

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                        final String itemName = orderItemName.get(position);
//                final String itemName = orderDetailItems.get(position).getOrderItemName();
//                final String itemCategory = orderDetailItems.get(position).getOrderItemCategory();
                if (dataSnapshot.child(orderDetailItems.get(position).getOrderItemCategory())
                        .child(orderDetailItems.get(position).getOrderItemName()).child(getString(R.string.status))
                        .getValue().toString().equals(getString(R.string.ready))
                        && !dataSnapshot.child(orderDetailItems.get(position).getOrderItemCategory())
                        .child(orderDetailItems.get(position).getOrderItemName())
                        .child(getString(R.string.rating)).exists()) {
                    AlertDialog.Builder giveRating = new AlertDialog.Builder(OrderDetailActivity.this);
                    giveRating.setTitle(R.string.give_rating);
                    View ratingView = getLayoutInflater().inflate(R.layout.choose_rating, null);
                    final RatingBar ratingBar = ratingView.findViewById(R.id.ratingBar);
                    giveRating.setView(ratingView);
                    giveRating.setPositiveButton(R.string.rate, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.setRatingValue(String.valueOf(ratingBar.getRating()), orderDetailItems.get(position));
//                            updateRating(ratingBar.getRating(), itemName, itemCategory);
                        }
                    });

                    AlertDialog chooseTimeDialog = giveRating.create();
                    chooseTimeDialog.show();

                    Button nbutton = chooseTimeDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    nbutton.setTextColor(getResources().getColor(R.color.colorPrimary));

                } else if (dataSnapshot.child(orderDetailItems.get(position).getOrderItemCategory())
                        .child(orderDetailItems.get(position).getOrderItemName()).child(getString(R.string.rating)).exists()) {
                    Toast.makeText(OrderDetailActivity.this, getString(R.string.already_rated), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderDetailActivity.this, getString(R.string.rate_after_ready), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDatabaseError(DatabaseError databaseError) {
        Toast.makeText(OrderDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRatingUpdatedSuccessfuly() {
        Toast.makeText(OrderDetailActivity.this, getString(R.string.rating_saved), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRatingUpdateFailed(DatabaseError databaseError) {
        Toast.makeText(OrderDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent service = new Intent(OrderDetailActivity.this, OrderNotificationService.class);
        service.putExtra(getString(R.string.i_order_id), orderData.getStringExtra(getString(R.string.i_order_id)));
        startService(service);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        todo: call notification before starting service
//        customNotification();
        Intent service = new Intent(OrderDetailActivity.this, OrderNotificationService.class);
        service.putExtra(getString(R.string.i_order_id), orderData.getStringExtra(getString(R.string.i_order_id)));
        startService(service);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        stopService(new Intent(this, OrderNotificationService.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        orderData = intent;
    }

}
