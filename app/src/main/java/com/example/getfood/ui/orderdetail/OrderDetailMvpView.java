package com.example.getfood.ui.orderdetail;

import android.content.Context;

import com.example.getfood.models.OrderDetailItem;
import com.example.getfood.ui.base.BaseView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public interface OrderDetailMvpView extends BaseView {

    Context getContext();

    void bindOrderDetailAdapter(ArrayList<OrderDetailItem> orderDetailItems, DataSnapshot dataSnapshot);

    void onDatabaseError(DatabaseError databaseError);

    void onRatingUpdatedSuccessfuly();

    void onRatingUpdateFailed(DatabaseError databaseError);
}
