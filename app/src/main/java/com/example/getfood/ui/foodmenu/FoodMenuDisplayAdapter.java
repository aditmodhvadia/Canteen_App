package com.example.getfood.ui.foodmenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.callback.FoodItemTouchListener;
import com.example.getfood.utils.AppUtils;
import com.fazemeright.canteen_app_models.models.FoodItem;

import java.util.ArrayList;

public class FoodMenuDisplayAdapter extends ListAdapter<FoodItem, FoodMenuDisplayAdapter.ViewHolder> {

    private Context context;
    private FoodItemTouchListener itemTouchListener;

    FoodMenuDisplayAdapter(Context context, FoodItemTouchListener itemTouchListener) {
        super(new FoodMenuDiffCallBack());
        this.context = context;
        this.itemTouchListener = itemTouchListener;
    }

    @NonNull
    @Override
    public FoodMenuDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_menu_display_view_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final FoodMenuDisplayAdapter.ViewHolder holder, int pos) {

        holder.bind(getItem(holder.getAdapterPosition()));
    }


    void swapData(ArrayList<FoodItem> newList) {
        submitList(newList);
    }

    static class FoodMenuDiffCallBack extends DiffUtil.ItemCallback<FoodItem> {

        @Override
        public boolean areItemsTheSame(@NonNull FoodItem foodItem, @NonNull FoodItem t1) {
            return foodItem.getItemName().equals(t1.getItemName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodItem foodItem, @NonNull FoodItem t1) {
            return foodItem.equals(t1);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemNameTextView, itemPriceTextView, itemRatingTextView;
        ImageView ivRatingStar;

        ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            itemRatingTextView = itemView.findViewById(R.id.itemRatingTextView);
            ivRatingStar = itemView.findViewById(R.id.ivRatingStar);
        }

        void bind(final FoodItem item) {
            itemNameTextView.setText(item.getItemName());
            itemPriceTextView.setText(String.format("%s %s", context.getString(R.string.rupee_symbol),
                    item.getItemPrice()));
            if (item.getItemRating() != -1) {
                itemRatingTextView.setText(String.valueOf(item.getItemRating()));
                itemRatingTextView.setTextColor(AppUtils.getColorForRating(context, String.valueOf(item.getItemRating())));

            } else {
                itemRatingTextView.setVisibility(View.INVISIBLE);
                ivRatingStar.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemTouchListener.onItemClicked(getAdapterPosition(), item);
                }
            });
        }
    }
}
