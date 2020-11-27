package com.example.getfood.ui.orderdetail;

import com.example.canteen_app_models.models.FullOrder;
import com.example.firebase_api_library.listeners.DBValueEventListener;
import com.example.getfood.ui.base.BasePresenter;

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
    public void setRatingValueForOrderItem(float rating, int position, String orderId) {
        apiManager.setRatingValueForOrderItem(rating, position, orderId);
    }
}
