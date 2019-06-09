package com.example.getfood.ui.foodmenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.models.CartItem;
import com.example.getfood.models.FoodItem;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.AppUtils;
import com.example.getfood.utils.DialogAddToCart;

import java.util.List;

public class FoodMenuRecyclerViewDisplayAdapter extends RecyclerView.Adapter<FoodMenuRecyclerViewDisplayAdapter.ViewHolder> {

    private List<FoodItem> foodItemList;
    private LayoutInflater inflater;
    private Context context;

    public FoodMenuRecyclerViewDisplayAdapter(List<FoodItem> foodItemList, Context context) {
        this.foodItemList = foodItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodMenuRecyclerViewDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_menu_display_custom_listview, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final FoodMenuRecyclerViewDisplayAdapter.ViewHolder holder, final int position) {


        holder.itemNameTextView.setText(foodItemList.get(position).getItemName());
        holder.itemPriceTextView.setText(String.format("%s %s", context.getString(R.string.rupee_symbol), foodItemList.get(position).getItemPrice()));
        if (foodItemList.get(position).getItemRating() != null) {
            holder.itemRatingTextView.setText(foodItemList.get(position).getItemRating());
            holder.itemRatingTextView.setTextColor(AppUtils.getColorForRating(context, foodItemList.get(position).getItemRating()));

        } else {
            holder.itemRatingTextView.setVisibility(View.INVISIBLE);
            holder.ivRatingStar.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.isItemInCart(FoodMenuDisplayActivity.cartItems, foodItemList.get(position)) != -1) {
                    Toast.makeText(context, context.getString(R.string.item_in_cart), Toast.LENGTH_SHORT).show();
                } else {
                    AlertUtils.showAddToCartDialog(context, foodItemList.get(position), new DialogAddToCart.AddToCartDialogListener() {

                        @Override
                        public void onAddToCartClicked(int quantity) {
                            if (quantity != 0) {
                                int probablePosition = AppUtils.isItemInCart(FoodMenuDisplayActivity.cartItems, foodItemList.get(position));
                                if (probablePosition != -1) {
                                    FoodMenuDisplayActivity.cartItems.get(probablePosition)
                                            .setItemQuantity(FoodMenuDisplayActivity.cartItems
                                                    .get(probablePosition).getItemQuantity() + quantity);
                                } else {
                                    FoodMenuDisplayActivity.cartItems.add(new CartItem(foodItemList.get(position),
                                            "Order-Placed", quantity));
                                }
                                Toast.makeText(context, context.getString(R.string.add_to_cart), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelClicked() {

                        }

                        @Override
                        public void onIncreaseQuantityClicked() {

                        }

                        @Override
                        public void onDecreaseQuantityClicked() {

                        }
                    });
                }
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
