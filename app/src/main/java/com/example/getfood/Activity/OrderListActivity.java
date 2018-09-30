package com.example.getfood.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.getfood.Adapter.OrderListDisplayAdapter;
import com.example.getfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {

    //    Views
    ListView ordersListView;

    //    Variables
    Intent data;
    String rollNo;
    ArrayList<String> orderID, orderTime, orderAmount;
    OrderListDisplayAdapter orderListDisplayAdapter;

    //    Firebase Varaiables
    DatabaseReference orderData, orderRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        orderData = FirebaseDatabase.getInstance().getReference().child("OrderData");
        orderRoot = FirebaseDatabase.getInstance().getReference().child("Order");

        ordersListView = findViewById(R.id.ordersListView);

        orderID = new ArrayList<>();
        orderTime = new ArrayList<>();
        orderAmount = new ArrayList<>();

        data = getIntent();
        rollNo = data.getStringExtra("RollNo");

//        fetch all the order IDs of the user first
        orderData.child(rollNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderID.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    orderID.add(dsp.getKey());

                }
                getOrderData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getOrderData(){

//        fetch order data of corresponding order IDs
        orderRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(String ID : orderID){
                    Log.d("Orderrrr", ID);
                    orderTime.add(dataSnapshot.child(ID).child("Time to deliver").getValue().toString());
                    orderAmount.add(dataSnapshot.child(ID).child("Total Amount").getValue().toString());
                }
//                set adapter
                orderListDisplayAdapter = new OrderListDisplayAdapter(orderID, orderTime, orderAmount, getApplicationContext());
                ordersListView.setAdapter(orderListDisplayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
