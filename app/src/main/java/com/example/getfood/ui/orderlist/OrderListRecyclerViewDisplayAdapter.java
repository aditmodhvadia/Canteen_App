package com.example.getfood.ui.orderlist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.models.FullOrder;
import com.example.getfood.ui.orderdetail.OrderDetailActivity;

import java.util.ArrayList;

public class OrderListRecyclerViewDisplayAdapter extends RecyclerView.Adapter<OrderListRecyclerViewDisplayAdapter.ViewHolder> {

    private ArrayList<FullOrder> orderListItems;
    private Context context;

    public OrderListRecyclerViewDisplayAdapter(ArrayList<FullOrder> orderListItems, Context context) {
        this.orderListItems = orderListItems;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderListRecyclerViewDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_display_view_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final OrderListRecyclerViewDisplayAdapter.ViewHolder holder, final int position) {

        String orderId;
        if (orderListItems.get(holder.getAdapterPosition()).getDisplayID() != null &&
                !orderListItems.get(holder.getAdapterPosition()).getDisplayID().isEmpty()) {
            orderId = orderListItems.get(holder.getAdapterPosition()).getDisplayID();
        } else {
            orderId = orderListItems.get(holder.getAdapterPosition()).getOrderId();
        }
        holder.orderIDTextView.setText(String.format("Order ID: %s", orderId));
        holder.orderAmountTextView.setText(String.format("Amount: ₹ %s",
                orderListItems.get(holder.getAdapterPosition()).getOrderAmount()));
        holder.orderTimeTextView.setText(orderListItems.get(holder.getAdapterPosition()).getTimeToDeliver());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OrderDetailActivity.class);
                i.putExtra("TestOrderData", orderListItems.get(holder.getAdapterPosition()));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderListItems.size();
    }

    public Context getContext() {
        return context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderIDTextView, orderAmountTextView, orderTimeTextView;

        ViewHolder(View itemView) {
            super(itemView);
            orderIDTextView = itemView.findViewById(R.id.orderIDTextView);
            orderAmountTextView = itemView.findViewById(R.id.orderAmountTextView);
            orderTimeTextView = itemView.findViewById(R.id.orderTimeTextView);
        }
    }
}
