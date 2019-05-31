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
import com.example.getfood.models.FullOrder;

public class OrderDisplayAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    //    private ArrayList<OrderDetailItem> orderDetailItems;
    private FullOrder orderDetailItems;

    private TextView orderItemQuantityTextView, orderItemNameTextView, orderItemStatusTextView;

    public OrderDisplayAdapter(FullOrder orderDetailItems, Context context) {
        this.orderDetailItems = orderDetailItems;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderDetailItems.getCartItems().size();
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

        if (orderDetailItems != null) {
            if (orderDetailItems.getCartItems().get(position) != null && orderDetailItems.getCartItems().get(position).getCartItemName() != null) {
                orderItemNameTextView.setText(orderDetailItems.getCartItems().get(position).getCartItemName());
            }
            if (orderDetailItems.getCartItems().get(position) != null && orderDetailItems.getCartItems().get(position).getCartItemQuantity() != null) {
                orderItemQuantityTextView.setText(String.valueOf(orderDetailItems.getCartItems().get(position).getCartItemQuantity()));
            }
            if (orderDetailItems.getCartItems().get(position) != null && orderDetailItems.getCartItems().get(position).getItemStatus() != null) {
                orderItemStatusTextView.setText(orderDetailItems.getCartItems().get(position).getItemStatus());
            }
        }

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setStartOffset(position * 10);
        v.startAnimation(animation);

        return v;
    }

    /*public void updateOrderList(ArrayList<OrderDetailItem> orderDetailItems) {
        this.orderDetailItems = orderDetailItems;
        notifyDataSetChanged();
    }*/

    public void updateOrderListNew(FullOrder orderDetailItems) {
        this.orderDetailItems = orderDetailItems;
        notifyDataSetChanged();
    }

    void updateOrderData(FullOrder updatedOrder) {
        orderDetailItems = updatedOrder;
        notifyDataSetChanged();
    }
}
