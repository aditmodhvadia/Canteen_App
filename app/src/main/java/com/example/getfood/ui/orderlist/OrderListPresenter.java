package com.example.getfood.ui.orderlist;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.example.getfood.models.OrderListItem;
import com.example.getfood.R;
import com.example.getfood.ui.base.BasePresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListPresenter<V extends OrderListMvpView> extends BasePresenter<V> implements OrderListMvpPresenter<V> {
    private DatabaseReference orderData, orderRoot;
    private ArrayList<String> orderID;
    private ArrayList<OrderListItem> orderListItems;

    public OrderListPresenter() {
    }

    @Override
    public void fetchOrderList(String rollNo) {
        orderData = FirebaseDatabase.getInstance().getReference().child(getMvpView().getContext().getString(R.string.order_data));
        orderRoot = FirebaseDatabase.getInstance().getReference().child(getMvpView().getContext().getString(R.string.order));

        orderID = new ArrayList<>();
        orderListItems = new ArrayList<>();

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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                        orderListItems.add(new OrderListItem(ID, dataSnapshot.child(ID)
                                .child(getMvpView().getContext().getString(R.string.time_to_deliver))
                                .getValue().toString(),
                                dataSnapshot.child(ID).child(getMvpView().getContext().getString(R.string.total_amount))
                                        .getValue().toString()));
                    }
                }
//                set adapter
                getMvpView().bindListAdapter(orderListItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
