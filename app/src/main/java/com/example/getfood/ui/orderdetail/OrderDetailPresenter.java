package com.example.getfood.ui.orderdetail;

import com.example.getfood.ui.base.BasePresenter;
import com.fazemeright.canteen_app_models.models.FullOrder;
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
    public void setRatingValueForOrderItem(String rating, int position, FullOrder order) {
        apiManager.setRatingValueForOrderItem(rating, position, order);
    }
}
