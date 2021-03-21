package com.example.getfood.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.getfood.R
import com.example.getfood.ui.orderdetail.OrderDetailActivity

class OrderNotificationService constructor() : Service() {
    //    Firebase Variables
    //    DatabaseReference currOrderRootChinese, currOrderRootSouthIndian, currOrderRootPizza;
    var inboxStyle: NotificationCompat.InboxStyle = NotificationCompat.InboxStyle()

    //    Variables
    private var ORDER_ID: String? = null
    public override fun onBind(intent: Intent): IBinder? {
//        throw new UnsupportedOperationException("Not yet implemented");
        return null
    }

    public override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

//        Toast.makeText(this, "Service for Get Food started", Toast.LENGTH_SHORT).show();
        val data: Intent = intent
        ORDER_ID = data.getStringExtra(getString(R.string.i_order_id))

//        currOrderRootChinese = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order)).child(ORDER_ID).child(getString(R.string.items)).child(getString(R.string.chinese));
//        currOrderRootSouthIndian = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order)).child(ORDER_ID).child(getString(R.string.items)).child(getString(R.string.south_indian));
//        currOrderRootPizza = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order)).child(ORDER_ID).child(getString(R.string.items)).child(getString(R.string.pizza_sandwich));

        /*currOrderRootChinese.addChildEventListener(new ChildEventListener() {
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
        });*/return START_STICKY
    }

    fun customNotification(item: String, status: String) {

//        todo: change the display messages to tell user that your order was updated
        val i: Intent = Intent(this, OrderDetailActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.putExtra(getString(R.string.i_order_id), ORDER_ID)
        val pi: PendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_channel_id))
                .setSmallIcon(R.drawable.ic_notif_icon_k)
                .setContentTitle(getString(R.string.your_order) + ORDER_ID)
                .setContentText(item + " is " + status)
                .setVibrate(longArrayOf(0, 400, 200, 400))
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(item + " is " + status + "\nBe ready to take your order when food is cooked!"))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true) //                todo: icon expands on lower devices
                .addAction(R.drawable.ic_open_notif, getString(R.string.open), pi)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setGroup(getString(R.string.group_notif_id))
                .setContentIntent(pi)
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        //        notificationManager.notify(ThreadLocalRandom.current().nextInt(), mBuilder.build());
//        notificationManager.notify(0, mBuilder.build());
        inboxStyle.setBigContentTitle(getString(R.string.order_updates))
        //        Group Notification
        val mBuilderGroup: NotificationCompat.Builder = NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_channel_id))
                .setSmallIcon(R.drawable.ic_notif_icon_k)
                .setContentTitle(getString(R.string.your_order) + ORDER_ID)
                .setContentText(getString(R.string.your_order_updates))
                .setVibrate(longArrayOf(0, 400, 200, 400))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColorized(true)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setGroup("group_item_notif")
                .setGroupSummary(true)
                .setContentIntent(pi)
        inboxStyle.addLine(item + " is " + status)
        mBuilderGroup.setStyle(inboxStyle)
        notificationManager.notify(0, mBuilderGroup.build())
    }
}