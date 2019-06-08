package com.example.getfood.ui.orderdetail;

import android.content.Context;

import com.example.getfood.models.FullOrder;
import com.example.getfood.ui.base.BaseView;
import com.google.firebase.database.DatabaseError;

public interface OrderDetailMvpView extends BaseView {

    Context getContext();

    void bindOrderDetailAdapter(FullOrder updatedOrder);

    void onDatabaseError(DatabaseError databaseError);

    void onRatingUpdatedSuccessfuly();

    void onRatingUpdateFailed(DatabaseError databaseError);
}
