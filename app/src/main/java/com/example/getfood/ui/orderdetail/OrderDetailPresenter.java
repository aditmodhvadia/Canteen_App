package com.example.getfood.ui.orderdetail;

import com.example.getfood.ui.base.BasePresenter;
import com.fazemeright.canteen_app_models.models.FullOrder;
import com.fazemeright.canteen_app_models.models.OrderDetailItem;
import com.fazemeright.firebase_api__library.listeners.DBValueEventListener;

public class OrderDetailPresenter<V extends OrderDetailMvpView> extends BasePresenter<V> implements OrderDetailMvpPresenter<V> {

    public OrderDetailPresenter() {
    }


    @Override
    public void fetchOrderDetails(FullOrder fullOrder) {
        apiManager.orderDetailListener(fullOrder.getOrderId(), new DBValueEventListener<FullOrder>() {
            @Override
            public void onDataChange(FullOrder data) {
                getMvpView().bindOrderDetailAdapter(data);
            }

            @Override
            public void onCancelled(Error error) {
                getMvpView().onDatabaseError(error);
            }
        });
    }

    @Override
    public void setRatingValue(final String ratingValue, OrderDetailItem orderDetailItem) {
//        root.child(orderDetailItem.getOrderItemCategory()).child(orderDetailItem.getOrderItemName())
//                .child(getMvpView().getContext().getString(R.string.rating))
//                .setValue(ratingValue);

        /*final DatabaseReference foodItems = FirebaseDatabase.getInstance().getReference()
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
                getMvpView().onRatingUpdatedSuccessfully();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getMvpView().onRatingUpdateFailed(new Error(databaseError.getMessage()));
            }
        });*/
    }
}
