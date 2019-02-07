package com.example.getfood.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.Activity.CartActivity;
import com.example.getfood.Activity.FoodMenuDisplayActivity;
import com.example.getfood.R;

import java.util.ArrayList;
import java.util.Locale;

public class CartRecyclerViewDisplayAdapter extends RecyclerView.Adapter<CartRecyclerViewDisplayAdapter.ViewHolder> {

    private ArrayList<String> cartItemName;
    private ArrayList<Integer> cartItemQuantity, cartItemPrice;
    private Context context;

    public CartRecyclerViewDisplayAdapter(ArrayList<String> cartItemName, ArrayList<Integer> cartItemQuantity, ArrayList<Integer> cartItemPrice, Context context) {
        this.cartItemName = cartItemName;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
        this.context = context;
    }

    @NonNull
    @Override
    public CartRecyclerViewDisplayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_display_customlistview, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final CartRecyclerViewDisplayAdapter.ViewHolder holder, int position) {

        holder.itemNameTextView.setText(cartItemName.get(position));
        holder.itemPriceTextView.setText(String.format(Locale.ENGLISH, "%s%d", context.getString(R.string.rupee_symbol), cartItemPrice.get(position)));
        holder.itemQuantityTextView.setText(cartItemQuantity.get(position).toString());

        holder.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                int value = FoodMenuDisplayActivity.cartItemQuantity.get(position);
                if (value < 10) {

//                    holder.itemQuantityTextView.setText(String.valueOf(value + 1));
                    FoodMenuDisplayActivity.cartItemQuantity.set(position, value + 1);
                    CartActivity.calcTotal();
                    notifyItemChanged(position);
                    Toast.makeText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {

                    int value = FoodMenuDisplayActivity.cartItemQuantity.get(position);

//                    check for the error in removing the value of item from the cart
//                    mismatch in position
                    if (value > 1) {
                        value--;
                        FoodMenuDisplayActivity.cartItemQuantity.set(position, value);
//                        CartActivity.cartDisplayAdapter.notifyDataSetChanged();
                        notifyItemChanged(position);
                        CartActivity.calcTotal();
                        Toast.makeText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();
                    } else if (value == 1) {

                        FoodMenuDisplayActivity.cartItemQuantity.remove(position);
                        FoodMenuDisplayActivity.cartItemPrice.remove(position);
                        FoodMenuDisplayActivity.cartItemName.remove(position);
                        FoodMenuDisplayActivity.cartItemCategory.remove(position);
//                            CartActivity.setDisplayListView(context);

                        if (FoodMenuDisplayActivity.cartItemName.size() == 0) {
//                            finish the activity
                            Toast.makeText(context, context.getString(R.string.cart_empty), Toast.LENGTH_SHORT).show();
                            CartActivity.activity.finish();
                        }
//                                CartActivity.notifyChangeAndCalcTotal(context);
                        notifyItemRemoved(position);
                        CartActivity.calcTotal();
                        Toast.makeText(context, context.getString(R.string.adjust_cart), Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItemName.size();
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
