package com.example.getfood.ui.cart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.callback.CartItemTouchListener;
import com.example.getfood.utils.AppUtils;
import com.fazemeright.canteen_app_models.models.CartItem;

import java.util.ArrayList;
import java.util.Locale;

public class CartRecyclerViewDisplayAdapter extends RecyclerView.Adapter<CartRecyclerViewDisplayAdapter.ViewHolder> {

    private ArrayList<CartItem> cartItems;
    private CartItemTouchListener cartItemTouchListener;
    private Context context;
    private CartItem removedItem;
    private int mRecentlyDeletedItemPosition;

    public CartRecyclerViewDisplayAdapter(ArrayList<CartItem> cartItems, Context context, CartItemTouchListener cartItemTouchListener) {
        this.cartItems = cartItems;
        this.context = context;
        this.cartItemTouchListener = cartItemTouchListener;
    }

    @NonNull
    @Override
    public CartRecyclerViewDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_display_view_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final CartRecyclerViewDisplayAdapter.ViewHolder holder, int position) {

        holder.itemNameTextView.setText(cartItems.get(position).getItemName());
        holder.itemPriceTextView.setText(String.format(Locale.ENGLISH, "%s%d", context.getString(R.string.rupee_symbol),
                Integer.parseInt(cartItems.get(position).getItemPrice())));
        holder.itemQuantityTextView.setText(String.valueOf(cartItems.get(position).getItemQuantity()));

        holder.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int position = holder.getAdapterPosition();
                int value = FoodMenuDisplayActivity.cartItems.get(position).getItemQuantity();
                if (value < 10) {
                    FoodMenuDisplayActivity.cartItems.get(position).increaseQuantity();
                    CartActivity.calcTotal();
                    notifyItemChanged(position);
                    Toast.makeText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
                }*/
                cartItemTouchListener.onIncreaseClicked(holder.getAdapterPosition());
            }
        });

        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {

                    int value = FoodMenuDisplayActivity.cartItems.get(position).getItemQuantity();

                    if (value > 1) {
                        FoodMenuDisplayActivity.cartItems.get(position).decreaseQuantity();
                        notifyItemChanged(position);
                        CartActivity.calcTotal();
                        Toast.makeText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
                    } else if (value == 1) {

                        FoodMenuDisplayActivity.cartItems.remove(position);
                        if (FoodMenuDisplayActivity.cartItems.size() == 0) {
                            Toast.makeText(context, context.getString(R.string.cart_empty), Toast.LENGTH_SHORT).show();
                            CartActivity.activity.finish();
                        }
                        notifyItemRemoved(position);
                        CartActivity.calcTotal();
                        Toast.makeText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();

                    }
                }*/
                if (cartItems.get(holder.getAdapterPosition()).getItemQuantity() == 1) {
                    cartItemTouchListener.onItemRemoved(holder.getAdapterPosition());
                } else {
                    cartItemTouchListener.onDecreaseClicked(holder.getAdapterPosition());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public Context getContext() {
        return context;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItemPosition = position;
        removedItem = cartItems.get(mRecentlyDeletedItemPosition);

        cartItemTouchListener.onItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = AppUtils.getSnackBar(context, context.getString(R.string.item_remove));
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.snackbar_yellow));
        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        cartItemTouchListener.onItemRemoveUndo(removedItem, mRecentlyDeletedItemPosition);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemQuantityTextView, itemNameTextView, itemPriceTextView;
        ImageButton increaseButton, decreaseButton;

        ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantityTextView);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
        }
    }
}
