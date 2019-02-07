package com.example.getfood.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.getfood.Activity.OrderActivity;
import com.example.getfood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderNotificationService extends Service {

    //    Firebase Variables
    DatabaseReference currOrderRootChinese, currOrderRootSouthIndian, currOrderRootPizza;
    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    //    Variables
    private String ORDER_ID;

    public OrderNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Toast.makeText(this, "Service for Get Food started", Toast.LENGTH_SHORT).show();

        Intent data = intent;
        ORDER_ID = data.getStringExtra(getString(R.string.i_order_id));

        currOrderRootChinese = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order)).child(ORDER_ID).child(getString(R.string.items)).child(getString(R.string.chinese));
        currOrderRootSouthIndian = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order)).child(ORDER_ID).child(getString(R.string.items)).child(getString(R.string.south_indian));
        currOrderRootPizza = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order)).child(ORDER_ID).child(getString(R.string.items)).child(getString(R.string.pizza_sandwich));

        currOrderRootChinese.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                customNotification(dataSnapshot.getKey(), dataSnapshot.child(getString(R.string.status)).getValue().toString());
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
                customNotification(dataSnapshot.getKey(), dataSnapshot.child(getString(R.string.status)).getValue().toString());
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
                customNotification(dataSnapshot.getKey(), dataSnapshot.child(getString(R.string.status)).getValue().toString().toLowerCase());
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
        i.putExtra(getString(R.string.i_order_id), ORDER_ID);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_channel_id))
                .setSmallIcon(R.drawable.ic_notif_icon_k)
                .setContentTitle(getString(R.string.your_order) + ORDER_ID)
                .setContentText(item + " is " + status)
                .setVibrate(new long[]{0, 400, 200, 400})
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(item + " is " + status + "\nBe ready to take your order when food is cooked!"))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
//                todo: icon expands on lower devices
                .addAction(R.drawable.ic_open_notif, getString(R.string.open), pi)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setGroup(getString(R.string.group_notif_id))
                .setContentIntent(pi);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(ThreadLocalRandom.current().nextInt(), mBuilder.build());
//        notificationManager.notify(0, mBuilder.build());

        inboxStyle.setBigContentTitle(getString(R.string.order_updates));
//        Group Notification
        NotificationCompat.Builder mBuilderGroup = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_channel_id))
                .setSmallIcon(R.drawable.ic_notif_icon_k)
                .setContentTitle(getString(R.string.your_order) + ORDER_ID)
                .setContentText(getString(R.string.your_order_updates))
                .setVibrate(new long[]{0, 400, 200, 400})
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setGroup("group_item_notif")
                .setGroupSummary(true)
                .setContentIntent(pi);
        inboxStyle.addLine(item + " is " + status);
        mBuilderGroup.setStyle(inboxStyle);

        notificationManager.notify(0, mBuilderGroup.build());

    }
}
