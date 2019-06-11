package com.example.getfood.ui.orderdetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.getfood.R;
import com.example.getfood.models.FullOrder;
import com.example.getfood.models.OrderDetailItem;
import com.example.getfood.ui.base.BasePresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetailPresenter<V extends OrderDetailMvpView> extends BasePresenter<V> implements OrderDetailMvpPresenter<V> {

    public OrderDetailPresenter() {
    }


    @Override
    public void fetchOrderDetails(FullOrder fullOrder) {
        apiManager.orderDetailListener(fullOrder.getRollNo(), fullOrder.getOrderId(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FullOrder updatedOrder = dataSnapshot.getValue(FullOrder.class);
                if (updatedOrder != null) {
                    Log.d("##DebugData", updatedOrder.toString());
                }
                getMvpView().bindOrderDetailAdapter(updatedOrder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getMvpView().onDatabaseError(databaseError);
            }
        });
    }

    @Override
    public void setRatingValue(final String ratingValue, OrderDetailItem orderDetailItem) {
//        root.child(orderDetailItem.getOrderItemCategory()).child(orderDetailItem.getOrderItemName())
//                .child(getMvpView().getContext().getString(R.string.rating))
//                .setValue(ratingValue);

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
