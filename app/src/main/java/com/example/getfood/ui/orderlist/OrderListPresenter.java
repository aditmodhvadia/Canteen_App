package com.example.getfood.ui.orderlist;

import com.example.getfood.ui.base.BasePresenter;
import com.example.getfood.utils.AppUtils;
import com.fazemeright.canteen_app_models.models.FullOrder;
import com.fazemeright.firebase_api__library.listeners.DBValueEventListener;

import java.util.ArrayList;

public class OrderListPresenter<V extends OrderListMvpView> extends BasePresenter<V> implements OrderListMvpPresenter<V> {

    public OrderListPresenter() {
    }

    @Override
    public void fetchOrderList() {
        String userRollNo = AppUtils.getRollNoFromEmail(apiManager.getCurrentUserEmail());
        if (userRollNo == null) {
            getMvpView().onRollNumberNull();
            return;
        }

        apiManager.orderListListener(new DBValueEventListener<ArrayList<FullOrder>>() {
            @Override
            public void onDataChange(ArrayList<FullOrder> data) {
                getMvpView().bindListAdapter(data);
            }

            @Override
            public void onCancelled(Error error) {

            }
        });
    }
}
