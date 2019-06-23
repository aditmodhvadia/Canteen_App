package com.example.getfood.ui.orderdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.getfood.R;
import com.fazemeright.canteen_app_models.models.FullOrder;

public class OrderDetailRecyclerViewDisplayAdapter extends RecyclerView.Adapter<OrderDetailRecyclerViewDisplayAdapter.ViewHolder> {

    private Context context;
    private FullOrder orderDetailItems;
    private OnOrderItemClickListener onOrderItemClickListener;

    public OrderDetailRecyclerViewDisplayAdapter(FullOrder orderDetailItems, Context context, OnOrderItemClickListener onOrderItemClickListener) {
        this.orderDetailItems = orderDetailItems;
        this.context = context;
        this.onOrderItemClickListener = onOrderItemClickListener;
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

        if (orderDetailItems != null && orderDetailItems.getOrderItems().get(holder.getAdapterPosition()) != null) {
            if (orderDetailItems.getOrderItems().get(position).getItemName() != null) {
                holder.orderItemNameTextView.setText(orderDetailItems.getOrderItems().get(position).getItemName());
            }
            if (orderDetailItems.getOrderItems().get(position).getItemQuantity() != null) {
                holder.orderItemQuantityTextView.setText(String.valueOf(orderDetailItems.getOrderItems().get(position).getItemQuantity()));
            }
            if (orderDetailItems.getOrderItems().get(position).getItemStatus() != null) {
                holder.orderItemStatusTextView.setText(orderDetailItems.getOrderItems().get(position).getItemStatus());
            }

            if (orderDetailItems.getOrderItems().get(holder.getAdapterPosition()).getItemRating() != null) {
                holder.ratingBar.setRating(Float.parseFloat(orderDetailItems.getOrderItems()
                        .get(holder.getAdapterPosition()).getItemRating()));
                holder.ratingBar.setEnabled(false);
            } else {
                holder.ratingBar.setRating(0);
                holder.ratingBar.setEnabled(true);
            }

            holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Log.d("##DebugData", "Rating is " + ratingBar.getRating());
                        ratingBar.setEnabled(false);
                        onOrderItemClickListener.onRatingGiven(ratingBar.getRating(), holder.getAdapterPosition(), orderDetailItems);
                    }
                }
            });
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

    interface OnOrderItemClickListener {
        void onRatingGiven(float rating, int position, FullOrder order);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderItemQuantityTextView, orderItemNameTextView, orderItemStatusTextView;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            orderItemQuantityTextView = itemView.findViewById(R.id.orderItemQuantityTextView);
            orderItemNameTextView = itemView.findViewById(R.id.orderItemNameTextView);
            orderItemStatusTextView = itemView.findViewById(R.id.orderItemStatusTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
