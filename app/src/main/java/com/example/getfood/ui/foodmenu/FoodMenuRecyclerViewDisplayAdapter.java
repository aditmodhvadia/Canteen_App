package com.example.getfood.ui.foodmenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.callback.FoodItemTouchListener;
import com.example.getfood.models.FoodItem;
import com.example.getfood.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class FoodMenuRecyclerViewDisplayAdapter extends RecyclerView.Adapter<FoodMenuRecyclerViewDisplayAdapter.ViewHolder> {

    private List<FoodItem> foodItemList;
    private Context context;
    private FoodItemTouchListener itemTouchListener;

    public FoodMenuRecyclerViewDisplayAdapter(List<FoodItem> foodItemList, Context context, FoodItemTouchListener itemTouchListener) {
        this.foodItemList = foodItemList;
        this.context = context;
        this.itemTouchListener = itemTouchListener;
    }

    @NonNull
    @Override
    public FoodMenuRecyclerViewDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_menu_display_view_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final FoodMenuRecyclerViewDisplayAdapter.ViewHolder holder, int pos) {


        holder.itemNameTextView.setText(foodItemList.get(holder.getAdapterPosition()).getItemName());
        holder.itemPriceTextView.setText(String.format("%s %s", context.getString(R.string.rupee_symbol), foodItemList.get(holder.getAdapterPosition()).getItemPrice()));
        if (foodItemList.get(holder.getAdapterPosition()).getItemRating() != null) {
            holder.itemRatingTextView.setText(foodItemList.get(holder.getAdapterPosition()).getItemRating());
            holder.itemRatingTextView.setTextColor(AppUtils.getColorForRating(context, foodItemList.get(holder.getAdapterPosition()).getItemRating()));

        } else {
            holder.itemRatingTextView.setVisibility(View.INVISIBLE);
            holder.ivRatingStar.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTouchListener.onItemClicked(holder.getAdapterPosition(), foodItemList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public Context getContext() {
        return context;
    }

    void updateFoodItems(ArrayList<FoodItem> foodItems) {
        this.foodItemList = foodItems;
        notifyDataSetChanged();
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
    }
}
