package com.example.getfood.ui.orderdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.models.FullOrder;

public class OrderDetailRecyclerViewDisplayAdapter extends RecyclerView.Adapter<OrderDetailRecyclerViewDisplayAdapter.ViewHolder> {

    private Context context;
    private FullOrder orderDetailItems;

    public OrderDetailRecyclerViewDisplayAdapter(FullOrder orderDetailItems, Context context) {
        this.orderDetailItems = orderDetailItems;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailRecyclerViewDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_display_layout, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailRecyclerViewDisplayAdapter.ViewHolder holder, final int position) {

        if (orderDetailItems != null) {
            if (orderDetailItems.getOrderItems().get(position) != null && orderDetailItems.getOrderItems().get(position).getItemName() != null) {
                holder.orderItemNameTextView.setText(orderDetailItems.getOrderItems().get(position).getItemName());
            }
            if (orderDetailItems.getOrderItems().get(position) != null && orderDetailItems.getOrderItems().get(position).getItemQuantity() != null) {
                holder.orderItemQuantityTextView.setText(String.valueOf(orderDetailItems.getOrderItems().get(position).getItemQuantity()));
            }
            if (orderDetailItems.getOrderItems().get(position) != null && orderDetailItems.getOrderItems().get(position).getItemStatus() != null) {
                holder.orderItemStatusTextView.setText(orderDetailItems.getOrderItems().get(position).getItemStatus());
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailItems.getOrderItems().size();
    }

    public Context getContext() {
        return context;
    }

    void updateOrderData(FullOrder updatedOrder) {
        orderDetailItems = updatedOrder;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderItemQuantityTextView, orderItemNameTextView, orderItemStatusTextView;

        ViewHolder(View itemView) {
            super(itemView);
            orderItemQuantityTextView = itemView.findViewById(R.id.orderItemQuantityTextView);
            orderItemNameTextView = itemView.findViewById(R.id.orderItemNameTextView);
            orderItemStatusTextView = itemView.findViewById(R.id.orderItemStatusTextView);
        }
    }
}
