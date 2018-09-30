package com.example.getfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.getfood.R;

import java.util.ArrayList;

public class OrderListDisplayAdapter extends BaseAdapter {

    ArrayList<String> orderID, orderTime, orderAmount;
    Context context;
    LayoutInflater inflater;

    TextView orderIDTextView, orderAmountTextView, orderTimeTextView;

    public OrderListDisplayAdapter(ArrayList<String> orderID, ArrayList<String> orderTime, ArrayList<String> orderAmount, Context context) {
        this.orderID = orderID;
        this.orderTime = orderTime;
        this.orderAmount = orderAmount;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderID.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = inflater.inflate(R.layout.order_list_display_customlistview, null);

        orderIDTextView = v.findViewById(R.id.orderIDTextView);
        orderAmountTextView = v.findViewById(R.id.orderAmountTextView);
        orderTimeTextView = v.findViewById(R.id.orderTimeTextView);


//        Set text for all the TextViews
        orderIDTextView.setText(orderID.get(position));
        orderAmountTextView.setText(orderAmount.get(position));
        orderTimeTextView.setText(orderTime.get(position));

//        Start animation on individual list items
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setStartOffset(position * 10);
        v.startAnimation(animation);

        return v;
    }
}
