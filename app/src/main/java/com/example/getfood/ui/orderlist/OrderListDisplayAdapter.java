package com.example.getfood.ui.orderlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.getfood.models.OrderListItem;
import com.example.getfood.R;

import java.util.ArrayList;

public class OrderListDisplayAdapter extends BaseAdapter {

    private ArrayList<OrderListItem> orderListItems;
    private Context context;
    private LayoutInflater inflater;

    private TextView orderIDTextView, orderAmountTextView, orderTimeTextView;

    public OrderListDisplayAdapter(ArrayList<OrderListItem> orderListItems, Context context) {
        this.orderListItems = orderListItems;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderListItems.size();
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
        orderIDTextView.setText(String.format("Order ID: %s", orderListItems.get(position).getOrderID()));
        orderAmountTextView.setText(String.format("Amount: ₹ %s", orderListItems.get(position).getOrderAmount()));
        orderTimeTextView.setText(orderListItems.get(position).getOrderTime());

//        Start animation on individual list items
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setStartOffset(position * 10);
        v.startAnimation(animation);

        return v;
    }
}
