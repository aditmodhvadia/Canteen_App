package com.example.getfood.ui.orderdetail;

import android.support.annotation.NonNull;

import com.example.getfood.models.OrderDetailItem;
import com.example.getfood.R;
import com.example.getfood.ui.base.BasePresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailPresenter<V extends OrderDetailMvpView> extends BasePresenter<V> implements OrderDetailMvpPresenter<V> {

    private DatabaseReference root;
    private ArrayList<OrderDetailItem> orderDetailItems;
    private String orderID, rollNo, orderTime, orderTotalPrice;

    public OrderDetailPresenter() {
    }


    @Override
    public void fetchOrderDetails(String orderID) {
        orderDetailItems = new ArrayList<>();
        root = FirebaseDatabase.getInstance().getReference().child(getMvpView().getContext().getString(R.string.order))
                .child(orderID).child(getMvpView().getContext().getString(R.string.items));
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                orderDetailItems.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if (dsp.getKey().equals(getMvpView().getContext().getString(R.string.time_to_deliver))) {
                        orderTime = dsp.getKey();
                    } else if (dsp.getKey().equals(getMvpView().getContext().getString(R.string.total_amount))) {
                        orderTotalPrice = dsp.getKey();
                    } else if (dsp.getKey().equals(getMvpView().getContext().getString(R.string.roll_no))) {

                    } else {
                        for (DataSnapshot dspInner : dsp.getChildren()) {
                            orderDetailItems.add(new OrderDetailItem(dspInner.getKey(), "",
                                    Integer.valueOf(dspInner.child(getMvpView().getContext()
                                            .getString(R.string.quantity)).getValue().toString()),
                                    dsp.getKey(), dspInner.child(getMvpView().getContext()
                                    .getString(R.string.status)).getValue().toString()));
                        }
                    }
                    getMvpView().bindOrderDetailAdapter(orderDetailItems, dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getMvpView().onDatabaseError(databaseError);
            }
        });
    }

    @Override
    public void setRatingValue(final String ratingValue, OrderDetailItem orderDetailItem) {
        root.child(orderDetailItem.getOrderItemCategory()).child(orderDetailItem.getOrderItemName())
                .child(getMvpView().getContext().getString(R.string.rating))
                .setValue(ratingValue);

        final DatabaseReference foodItems = FirebaseDatabase.getInstance().getReference()
                .child(getMvpView().getContext().getString(R.string.food)).child(orderDetailItem.getOrderItemCategory())
                .child(orderDetailItem.getOrderItemName());
        foodItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float currRating = Float.valueOf(dataSnapshot.child(getMvpView()
                        .getContext().getString(R.string.rating)).getValue().toString());
                int numberOfRating = Integer.valueOf(dataSnapshot.child(getMvpView().getContext()
                        .getString(R.string.no_of_rating)).getValue().toString());
                float newRating = (currRating * numberOfRating++ + Integer.parseInt(ratingValue)) / numberOfRating;
                foodItems.child(getMvpView().getContext().getString(R.string.rating)).setValue(newRating);
                foodItems.child(getMvpView().getContext().getString(R.string.no_of_rating)).setValue(numberOfRating);
                getMvpView().onRatingUpdatedSuccessfuly();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getMvpView().onRatingUpdateFailed(databaseError);
            }
        });
    }
}
