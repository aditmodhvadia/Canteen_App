package com.example.getfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderDisplayAdapter extends BaseAdapter {

    ArrayList<String>  orderItemName;
    ArrayList<Integer> orderItemQuantity;
    Context context;
    LayoutInflater inflater;

    TextView orderItemQuantityTextView, orderItemNameTextView;

    public OrderDisplayAdapter(ArrayList<String> orderItemName, ArrayList<Integer> orderItemQuantity, Context context) {
        this.orderItemName = orderItemName;
        this.orderItemQuantity = orderItemQuantity;
        this.context = context;
        this.inflater = (LayoutInflater) LayoutInflater.from(context);
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

        View v = inflater.inflate(R.layout.order_display_layout,null);

        orderItemQuantityTextView = v.findViewById(R.id.orderItemQuantityTextView);
        orderItemNameTextView = v.findViewById(R.id.orderItemNameTextView);

        orderItemNameTextView.setText(orderItemName.get(position));
        orderItemQuantityTextView.setText(orderItemQuantity.get(position).toString());

        return v;
    }
}
