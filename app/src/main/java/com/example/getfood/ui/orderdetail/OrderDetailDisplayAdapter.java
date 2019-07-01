package com.example.getfood.ui.orderdetail;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.getfood.R;
import com.fazemeright.canteen_app_models.models.CartItem;

import java.util.List;

public class OrderDetailDisplayAdapter extends ListAdapter<CartItem, OrderDetailDisplayAdapter.ViewHolder> {

    private OnOrderItemClickListener onOrderItemClickListener;

    OrderDetailDisplayAdapter(OnOrderItemClickListener onOrderItemClickListener) {
        super(new OrderDetailDiffCallBack());
        this.onOrderItemClickListener = onOrderItemClickListener;
    }

    void swapData(List<CartItem> newList) {
        submitList(newList);
    }

    @NonNull
    @Override
    public OrderDetailDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_display_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailDisplayAdapter.ViewHolder holder, int position) {

        CartItem item = getItem(position);

        if (item != null) {
            holder.bind(item);
        }
    }

    interface OnOrderItemClickListener {
        void onRatingGiven(float rating, int position);
    }

    static class OrderDetailDiffCallBack extends DiffUtil.ItemCallback<CartItem> {

        @Override
        public boolean areItemsTheSame(@NonNull CartItem cartItem, @NonNull CartItem t1) {
            return cartItem.getItemName().equals(t1.getItemName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem cartItem, @NonNull CartItem t1) {
            return cartItem.equals(t1);
        }
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

        void bind(CartItem item) {
            if (item.getItemName() != null) {
                this.orderItemNameTextView.setText(item.getItemName());
            }
            if (item.getItemQuantity() != null) {
                orderItemQuantityTextView.setText(String.valueOf(item.getItemQuantity()));
            }
            if (item.getItemStatus() != null) {
                orderItemStatusTextView.setText(item.getItemStatus());
            }

            if (item.getItemRating() != 0) {
                ratingBar.setRating((float) item.getItemRating());
                ratingBar.setEnabled(false);
            } else {
                ratingBar.setRating(0);
                ratingBar.setEnabled(true);
            }

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Log.d("##DebugData", "Rating is " + ratingBar.getRating());
                        ratingBar.setEnabled(false);
                        onOrderItemClickListener.onRatingGiven(ratingBar.getRating(), getAdapterPosition());
                    }
                }
            });
        }
    }
}
