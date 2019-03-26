package com.example.getfood.ui.orderdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.getfood.Models.OrderDetailItem;
import com.example.getfood.R;

import java.util.ArrayList;

public class OrderDisplayAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OrderDetailItem> orderDetailItems;

    private TextView orderItemQuantityTextView, orderItemNameTextView, orderItemStatusTextView;

    public OrderDisplayAdapter(ArrayList<OrderDetailItem> orderDetailItems, Context context) {
        this.orderDetailItems = orderDetailItems;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderDetailItems.size();
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

        View v = inflater.inflate(R.layout.order_display_layout, null);

        orderItemQuantityTextView = v.findViewById(R.id.orderItemQuantityTextView);
        orderItemNameTextView = v.findViewById(R.id.orderItemNameTextView);
        orderItemStatusTextView = v.findViewById(R.id.orderItemStatusTextView);

        orderItemNameTextView.setText(orderDetailItems.get(position).getOrderItemName());
        orderItemQuantityTextView.setText(String.valueOf(orderDetailItems.get(position).getOrederItemQuantity()));
        orderItemStatusTextView.setText(orderDetailItems.get(position).getOrderItemStatus());

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setStartOffset(position * 10);
        v.startAnimation(animation);

        return v;
    }

    public void updateOrderList(ArrayList<OrderDetailItem> orderDetailItems) {
        this.orderDetailItems = orderDetailItems;
        notifyDataSetChanged();
    }
}
