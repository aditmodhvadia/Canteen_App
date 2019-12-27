package com.example.getfood.ui.cart;

import android.content.Context;
import androidx.annotation.NonNull;

import com.example.canteen_app_models.models.CartItem;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.callback.CartItemTouchListener;
import com.example.getfood.utils.AppUtils;

import java.util.List;
import java.util.Locale;

public class CartDisplayAdapter extends ListAdapter<CartItem, CartDisplayAdapter.ViewHolder> {

    private CartItemTouchListener cartItemTouchListener;
    private Context context;
    private CartItem removedItem;
    private int mRecentlyDeletedItemPosition;

    CartDisplayAdapter(Context context, CartItemTouchListener cartItemTouchListener) {
        super(new CartDiffCallBack());
        this.context = context;
        this.cartItemTouchListener = cartItemTouchListener;
    }

    @NonNull
    @Override
    public CartDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_display_view_item, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CartDisplayAdapter.ViewHolder holder, int position) {

        holder.bind(getItem(position));
    }

    void swapData(List<CartItem> newList) {
        submitList(newList);
    }

    public Context getContext() {
        return context;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItemPosition = position;
        removedItem = getItem(mRecentlyDeletedItemPosition);

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

    int getCartItemsCount() {
        return getItemCount();
    }

    void itemInserted(int position) {
        notifyItemInserted(position);
    }

    void itemRemoved(int position) {
        notifyItemRemoved(position);
    }

    void itemChanged(int position) {
        notifyItemChanged(position);
    }

    static class CartDiffCallBack extends DiffUtil.ItemCallback<CartItem> {

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

        void bind(final CartItem item) {
            itemNameTextView.setText(item.getItemName());
            itemPriceTextView.setText(String.format(Locale.ENGLISH, "%s%d", context.getString(R.string.rupee_symbol),
                    Integer.parseInt(item.getItemPrice())));
            itemQuantityTextView.setText(String.valueOf(item.getItemQuantity()));

            increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*int position = holder.getAdapterPosition();
                int value = FoodMenuDisplayActivity.cartItems.get(position).getItemQuantity();
                if (value < 10) {
                    FoodMenuDisplayActivity.cartItems.get(position).increaseQuantity();
                    CartActivity.calcTotal();
                    notifyItemChanged(position);
                    Toast.makeShortText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
                }*/
                    cartItemTouchListener.onIncreaseClicked(getAdapterPosition());
                }
            });

            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {

                    int value = FoodMenuDisplayActivity.cartItems.get(position).getItemQuantity();

                    if (value > 1) {
                        FoodMenuDisplayActivity.cartItems.get(position).decreaseQuantity();
                        notifyItemChanged(position);
                        CartActivity.calcTotal();
                        Toast.makeShortText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
                    } else if (value == 1) {

                        FoodMenuDisplayActivity.cartItems.remove(position);
                        if (FoodMenuDisplayActivity.cartItems.size() == 0) {
                            Toast.makeShortText(context, context.getString(R.string.cart_empty), Toast.LENGTH_SHORT).show();
                            CartActivity.activity.finish();
                        }
                        notifyItemRemoved(position);
                        CartActivity.calcTotal();
                        Toast.makeShortText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();

                    }
                }*/
                    if (item.getItemQuantity() == 1) {
                        cartItemTouchListener.onItemRemoved(getAdapterPosition());
                    } else {
                        cartItemTouchListener.onDecreaseClicked(getAdapterPosition());
                    }
                }
            });
        }
    }
}
