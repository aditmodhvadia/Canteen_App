package com.example.getfood.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.Adapter.OrderDisplayAdapter;
import com.example.getfood.R;
import com.example.getfood.Service.OrderNotificationService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
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
        if(orderData.getExtras().isEmpty()){
            Toast.makeText(getApplicationContext(), "No Data Received", Toast.LENGTH_SHORT).show();
            return;
        }
        createNotificationChannel();
//        Get data from Intent
        orderID = orderData.getExtras().getString("OrderID");
        orderTotal = orderData.getExtras().getInt("Total");
        rollNo = orderData.getExtras().getString("RollNo");

        root = FirebaseDatabase.getInstance().getReference().child("Order").child(orderID).child("Items");
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderItemCategory.clear();
                orderItemName.clear();
                orderItemQuantity.clear();
                orderItemStatus.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp.getKey().equals("Time to deliver")){
                        orderTime = dsp.getKey();
                    }
                    else if(dsp.getKey().equals("Total Amount")){
                        orderTotalPrice = dsp.getKey();
                    }
                    else if(dsp.getKey().equals("Roll No")){

                    }
                    else{
                        for (DataSnapshot dspInner : dsp.getChildren()){
                            orderItemCategory.add(dsp.getKey());
                            orderItemName.add(dspInner.getKey());
                            orderItemQuantity.add(Integer.valueOf(dspInner.child("Quantity").getValue().toString()));
                            orderItemStatus.add(dspInner.child("Status").getValue().toString());
                        }
                    }
                }
                orderDisplayAdapter = new OrderDisplayAdapter(orderItemName, orderItemQuantity, orderItemStatus, getApplicationContext());
                orderListView.setAdapter(orderDisplayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(OrderActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        testTV.setText(String.format("Order ID is %s", orderID));
    }

    public void customNotification() {
        createNotificationChannel();

        Intent i = new Intent(this, OrderActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("OrderID",orderID);
        i.putExtra("Total", orderTotal);
        i.putExtra("RollNo",rollNo);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.drawable.mykanteenlogonotif)
                .setContentTitle("Your Order")
                .setContentText("Order is being cooked")
                .setVibrate(new long[]{0, 400, 200, 400})
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Be ready to take your order when your food is cooked!"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_person_add_black_48dp, "Open", pi)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(pi);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(0, mBuilder.build());


    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent service = new Intent(OrderActivity.this, OrderNotificationService.class);
        service.putExtra("OrderID", orderData.getStringExtra("OrderID"));
        startService(service);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        todo: call notification before starting service
//        customNotification();
        Intent service = new Intent(OrderActivity.this, OrderNotificationService.class);
        service.putExtra("OrderID", orderData.getStringExtra("OrderID"));
        startService(service);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        stopService(new Intent(this, OrderNotificationService.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customNotification();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        customNotification();
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        orderData = intent;
    }

    private void createNotificationChannel() {
//        create notification channel only for Builds greater than Oreo(8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Channel";
            String description = "Primary display";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
