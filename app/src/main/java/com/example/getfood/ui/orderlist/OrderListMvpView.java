package com.example.getfood.ui.orderlist;

import android.content.Context;

import com.example.getfood.models.FullOrder;
import com.example.getfood.ui.base.BaseView;

import java.util.ArrayList;

public interface OrderListMvpView extends BaseView {
    void bindListAdapter(ArrayList<FullOrder> orderListItems);

    Context getContext();
}
