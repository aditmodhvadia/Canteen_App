package com.example.getfood.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.getfood.Activity.OrderActivity;
import com.example.getfood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderNotificationService extends Service {

//    Variables
    private String ORDER_ID;
//    Firebase Variables
    DatabaseReference currOrderRootChinese, currOrderRootSouthIndian, currOrderRootPizza;
    public OrderNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service for Get Food started", Toast.LENGTH_SHORT).show();

        Intent data = intent;
        ORDER_ID = data.getStringExtra("OrderID");

        currOrderRootChinese = FirebaseDatabase.getInstance().getReference().child("Order").child(ORDER_ID).child("Items").child("Chinese");
        currOrderRootSouthIndian = FirebaseDatabase.getInstance().getReference().child("Order").child(ORDER_ID).child("Items").child("South Indian");
        currOrderRootPizza = FirebaseDatabase.getInstance().getReference().child("Order").child(ORDER_ID).child("Items").child("Pizza Sandwich");

        currOrderRootChinese.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customNotification(dataSnapshot.getKey(), dataSnapshot.child("Status").getValue().toString());
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        currOrderRootSouthIndian.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customNotification(dataSnapshot.getKey(), dataSnapshot.child("Status").getValue().toString());
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        currOrderRootPizza.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customNotification(dataSnapshot.getKey(), dataSnapshot.child("Status").getValue().toString().toLowerCase());
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        return START_STICKY;
    }

    public void customNotification(String item, String status) {

//        todo: change the display messages to tell user that your order was updated

        Intent i = new Intent(this, OrderActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("OrderID",ORDER_ID);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.drawable.mykanteenlogonotif)
                .setContentTitle("Your Order "+ORDER_ID)
                .setContentText(item +" is " +status)
                .setVibrate(new long[]{0, 400, 200, 400})
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(item +" is " +status +"\nBe ready to take your order when food is cooked!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_person_add_black_48dp, "Open", pi)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(pi);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build());
    }
}
