package com.example.getfood.ui.orderdetail;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.service.OrderNotificationService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {
    //    Layout Views
    TextView testTV, test;
    ListView orderListView;
    OrderDisplayAdapter orderDisplayAdapter;
    //    Variables
    ArrayList<String> orderItemName, orderItemCategory, orderItemStatus;
    ArrayList<Integer> orderItemPrice, orderItemQuantity;
    int orderTotal;
    String orderID, rollNo, orderTime, orderTotalPrice;
    Intent orderData;
    //    Firebase Variables
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        testTV = findViewById(R.id.testTV);
        orderListView = findViewById(R.id.orderListView);
        test = findViewById(R.id.test);
//        Initialization
        orderItemName = new ArrayList<>();
        orderItemQuantity = new ArrayList<>();
        orderItemPrice = new ArrayList<>();
        orderItemCategory = new ArrayList<>();
        orderItemStatus = new ArrayList<>();
//        Getting data from the calling activity/Intent
        orderData = getIntent();
        if (orderData.getExtras().isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_data_received), Toast.LENGTH_SHORT).show();
            return;
        }
        createNotificationChannel();
//        Get data from Intent
        orderID = orderData.getExtras().getString(getString(R.string.i_order_id));
        orderTotal = orderData.getExtras().getInt(getString(R.string.i_total));
        rollNo = orderData.getExtras().getString(getString(R.string.i_roll_no));

        root = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order)).child(orderID).child(getString(R.string.items));
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                orderItemCategory.clear();
                orderItemName.clear();
                orderItemQuantity.clear();
                orderItemStatus.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if (dsp.getKey().equals(getString(R.string.time_to_deliver))) {
                        orderTime = dsp.getKey();
                    } else if (dsp.getKey().equals(getString(R.string.total_amount))) {
                        orderTotalPrice = dsp.getKey();
                    } else if (dsp.getKey().equals(getString(R.string.roll_no))) {

                    } else {
                        for (DataSnapshot dspInner : dsp.getChildren()) {
                            orderItemCategory.add(dsp.getKey());
                            orderItemName.add(dspInner.getKey());
                            orderItemQuantity.add(Integer.valueOf(dspInner.child(getString(R.string.quantity)).getValue().toString()));
                            orderItemStatus.add(dspInner.child(getString(R.string.status)).getValue().toString());
                        }
                    }
                }
                orderDisplayAdapter = new OrderDisplayAdapter(orderItemName, orderItemQuantity, orderItemStatus, getApplicationContext());
                orderListView.setAdapter(orderDisplayAdapter);

                orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final String itemName = orderItemName.get(position);
                        final String itemCategory = orderItemCategory.get(position);
                        if (dataSnapshot.child(itemCategory).child(itemName).child(getString(R.string.status)).getValue().toString().equals(getString(R.string.ready))
                                && !dataSnapshot.child(itemCategory).child(itemName).child(getString(R.string.rating)).exists()) {
                            AlertDialog.Builder giveRating = new AlertDialog.Builder(OrderDetailActivity.this);
                            giveRating.setTitle(R.string.give_rating);
                            View ratingView = getLayoutInflater().inflate(R.layout.choose_rating, null);
                            final RatingBar ratingBar = ratingView.findViewById(R.id.ratingBar);
                            giveRating.setView(ratingView);
                            giveRating.setPositiveButton(R.string.rate, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    root.child(itemCategory).child(itemName).child(getString(R.string.rating)).setValue(String.valueOf(ratingBar.getRating()));
                                    updateRating(ratingBar.getRating(), itemName, itemCategory);
                                }
                            });

                            AlertDialog chooseTimeDialog = giveRating.create();
                            chooseTimeDialog.show();

                            Button nbutton = chooseTimeDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            nbutton.setTextColor(getResources().getColor(R.color.colorPrimary));

                        } else if (dataSnapshot.child(itemCategory).child(itemName).child(getString(R.string.rating)).exists()) {
                            Toast.makeText(OrderDetailActivity.this, getString(R.string.already_rated), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OrderDetailActivity.this, getString(R.string.rate_after_ready), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(OrderDetailActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        testTV.setText(String.format("%s%s", getString(R.string.order_id_is), orderID));
    }

    private void updateRating(final float rating, String itemName, String itemCategory) {

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

    private void createNotificationChannel() {

//        create notification channel only for Builds greater than Oreo(8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.order_channel);
            String description = getString(R.string.primary_display_notif);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.notif_channel), name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
