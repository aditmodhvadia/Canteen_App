package com.example.getfood.ui.orderdetail;

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

public class OrderDisplayAdapter extends BaseAdapter {

    private ArrayList<String> orderItemName, orderItemStatus;
    private ArrayList<Integer> orderItemQuantity;
    private Context context;
    private LayoutInflater inflater;

    private TextView orderItemQuantityTextView, orderItemNameTextView, orderItemStatusTextView;

    public OrderDisplayAdapter(ArrayList<String> orderItemName, ArrayList<Integer> orderItemQuantity, ArrayList<String> orderItemStatus, Context context) {
        this.orderItemName = orderItemName;
        this.orderItemQuantity = orderItemQuantity;
        this.orderItemStatus = orderItemStatus;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderItemName.size();
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

        orderItemNameTextView.setText(orderItemName.get(position));
        orderItemQuantityTextView.setText(String.valueOf(orderItemQuantity.get(position)));
        orderItemStatusTextView.setText(orderItemStatus.get(position));

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setStartOffset(position * 10);
        v.startAnimation(animation);

        return v;
    }
}
