package com.example.getfood.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.getfood.OrderActivity;
import com.example.getfood.R;

public class OrderNotificationService extends Service {

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

        Toast.makeText(this, "Service for Get Food started", Toast.LENGTH_SHORT).show();

        Intent data = intent;
        ORDER_ID = data.getStringExtra("OrderID");
        customNotification();


        return START_STICKY;
    }

    public void customNotification() {

        Intent i = new Intent(this, OrderActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("OrderID",ORDER_ID);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.drawable.btn_clear)
                .setContentTitle("Your Order "+ORDER_ID)
                .setContentText("Order is being cooked")
                .setVibrate(new long[]{0, 400, 200, 400})
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Be ready to take your order when your food is cooked!"))
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
