package com.example.getfood.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
    TextView ordersHeadingTextView;

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
//        Initializations
        orderData = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order_data));
        orderRoot = FirebaseDatabase.getInstance().getReference().child(getString(R.string.order));

        ordersListView = findViewById(R.id.ordersListView);
        ordersHeadingTextView = findViewById(R.id.ordersHeadingTextView);

        orderID = new ArrayList<>();
        orderTime = new ArrayList<>();
        orderAmount = new ArrayList<>();

        data = getIntent();
        rollNo = data.getStringExtra(getString(R.string.i_roll_no));

//        fetch all the order IDs of the user first
        orderData.child(rollNo).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    orderID.clear();
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                        orderID.add(dsp.getKey());

                    }
                    getOrderData();

                } else {
                    ordersHeadingTextView.setText(getString(R.string.no_order));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(OrderListActivity.this, OrderActivity.class);
                i.putExtra(getString(R.string.i_order_id), orderID.get(position));
                i.putExtra(getString(R.string.i_total), orderAmount.get(position));
                i.putExtra(getString(R.string.i_roll_no), rollNo);
                startActivity(i);
            }
        });

    }

    private void getOrderData() {

//        fetch order data of corresponding order IDs
        orderRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String ID : orderID) {
                    if (dataSnapshot.child(ID).exists()) {
                        orderTime.add(dataSnapshot.child(ID).child(getString(R.string.time_to_deliver)).getValue().toString());
                        orderAmount.add(dataSnapshot.child(ID).child(getString(R.string.total_amount)).getValue().toString());

                    }
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
