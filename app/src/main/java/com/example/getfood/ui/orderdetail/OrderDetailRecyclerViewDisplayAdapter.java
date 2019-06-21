package com.example.getfood.ui.orderdetail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.utils.AppUtils;
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
                holder.itemRatingTextView.setText(orderDetailItems.getOrderItems().get(holder.getAdapterPosition()).getItemRating());
                holder.itemRatingTextView.setTextColor(AppUtils.getColorForRating(context,
                        orderDetailItems.getOrderItems().get(holder.getAdapterPosition()).getItemRating()));
                holder.ivRatingStar.setVisibility(View.VISIBLE);
                holder.itemRatingTextView.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderDetailItems.getOrderItems().get(holder.getAdapterPosition()).getItemRating() == null) {
                        AlertDialog.Builder giveRating = new AlertDialog.Builder(context);
                        giveRating.setTitle(R.string.give_rating);
                        View ratingView = ((Activity) context).getLayoutInflater().inflate(R.layout.choose_rating, null);
                        final RatingBar ratingBar = ratingView.findViewById(R.id.ratingBar);
                        giveRating.setView(ratingView);
                        giveRating.setPositiveButton(R.string.rate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("##DebugData", "Rating is " + ratingBar.getRating());
                                onOrderItemClickListener.onRatingGiven(ratingBar.getRating(), holder.getAdapterPosition(), orderDetailItems);
                            }
                        });

                        AlertDialog chooseTimeDialog = giveRating.create();
                        chooseTimeDialog.show();

                        Button nbutton = chooseTimeDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        nbutton.setTextColor(context.getResources().getColor(R.color.colorPrimary));

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

        TextView orderItemQuantityTextView, orderItemNameTextView, orderItemStatusTextView, itemRatingTextView;
        ImageView ivRatingStar;

        ViewHolder(View itemView) {
            super(itemView);
            orderItemQuantityTextView = itemView.findViewById(R.id.orderItemQuantityTextView);
            orderItemNameTextView = itemView.findViewById(R.id.orderItemNameTextView);
            orderItemStatusTextView = itemView.findViewById(R.id.orderItemStatusTextView);
            itemRatingTextView = itemView.findViewById(R.id.itemRatingTextView);
            ivRatingStar = itemView.findViewById(R.id.ivRatingStar);
        }
    }
}
