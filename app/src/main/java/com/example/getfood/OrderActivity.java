package com.example.getfood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    TextView testTV;
    ListView orderListView;
    CartDisplayAdapter cartDisplayAdapter;

    ArrayList<String> orderItemName, orderItemCategory;
    ArrayList<Integer> orderItemPrice, orderItemQuantity;
    int orderTotal;
    String orderID, rollNo, orderTime, orderTotalPrice;


    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        testTV = findViewById(R.id.testTV);
        orderListView = findViewById(R.id.orderListView);

        orderItemName = new ArrayList<>();
        orderItemQuantity = new ArrayList<>();
        orderItemPrice = new ArrayList<>();
        orderItemCategory = new ArrayList<>();

        Intent orderData = getIntent();
        if(orderData.getExtras().isEmpty()){
            Toast.makeText(getApplicationContext(), "No Data Received", Toast.LENGTH_SHORT).show();
            return;
        }
        orderID = orderData.getExtras().getString("OrderID");
        orderTotal = orderData.getExtras().getInt("Total");
        rollNo = orderData.getExtras().getString("RollNo");
//        Arraylist returns null from the cart Activity
//        orderItemName = (ArrayList<String>) orderData.getExtras().get("ItemName");
//        orderItemPrice = (ArrayList<Integer>) orderData.getIntegerArrayListExtra("ItemPrice").clone();
//        orderItemQuantity = (ArrayList<Integer>) orderData.getIntegerArrayListExtra("ItemQuantity").clone();
//        Toast.makeText(getApplicationContext(), orderItemName.toString(), Toast.LENGTH_SHORT).show();
//        Bundle args = orderData.getBundleExtra("BUNDLE");
//        Bundle args = orderData.getExtras();
//        orderItemName = args.getStringArrayList("ItemName");

        root = FirebaseDatabase.getInstance().getReference().child("Order").child(orderID).child(rollNo);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp.getKey().equals("Time to deliver")){
                        orderTime = dsp.getKey();
                    }
                    else if(dsp.getKey().equals("Total Amount")){
                        orderTotalPrice = dsp.getKey();
                    }
                    else{
                        for (DataSnapshot dspInner : dsp.getChildren()){
                            orderItemCategory.add(dsp.getKey());
                            orderItemName.add(dspInner.getKey());
                            orderItemQuantity.add(Integer.valueOf(dspInner.child("Quantity").getValue().toString()));
                        }
                    }
                }
                cartDisplayAdapter = new CartDisplayAdapter(orderItemName, orderItemQuantity,
                        null, getApplicationContext());
                orderListView.setAdapter(cartDisplayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        testTV.setText("Order ID is " +orderID);

    }
}
