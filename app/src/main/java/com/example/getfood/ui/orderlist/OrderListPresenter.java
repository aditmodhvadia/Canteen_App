package com.example.getfood.ui.orderlist;

import android.support.annotation.NonNull;

import com.example.getfood.R;
import com.example.getfood.models.FullOrder;
import com.example.getfood.ui.base.BasePresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListPresenter<V extends OrderListMvpView> extends BasePresenter<V> implements OrderListMvpPresenter<V> {
    private DatabaseReference orderData, orderRoot, userOrderData;
    private ArrayList<FullOrder> orderListItems;

    public OrderListPresenter() {
    }

    @Override
    public void fetchOrderList(String rollNo) {

        orderData = FirebaseDatabase.getInstance().getReference().child(getMvpView().getContext().getString(R.string.order_data));
        orderRoot = FirebaseDatabase.getInstance().getReference().child(getMvpView().getContext().getString(R.string.order));
        userOrderData = FirebaseDatabase.getInstance().getReference().child("UserOrderData");

        orderListItems = new ArrayList<>();

        userOrderData.child(rollNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    orderListItems.clear();
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        orderListItems.add(dsp.getValue(FullOrder.class));
                    }
                    getMvpView().bindListAdapter(orderListItems);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
