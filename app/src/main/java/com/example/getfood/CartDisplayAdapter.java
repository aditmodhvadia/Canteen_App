package com.example.getfood;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartDisplayAdapter extends BaseAdapter {

    ArrayList<String> cartItemName;
    ArrayList<Integer> cartItemQuantity, cartItemPrice;
    Context context;

    TextView itemQuantityTextView, itemNameTextView, itemPriceTextView;

    public CartDisplayAdapter(ArrayList<String> cartItemName, ArrayList<Integer> cartItemQuantity, ArrayList<Integer> cartItemPrice, Context context) {
        this.cartItemName = cartItemName;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemPrice = cartItemPrice;
        this.context = context;
        this.inflater = (LayoutInflater) LayoutInflater.from(context);
    }

    LayoutInflater inflater;

    @Override
    public int getCount() {
        return cartItemName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {


        final View vi;
        ViewHolder mainViewHolder = null;
        if (view == null) {
            vi = inflater.inflate(R.layout.cart_display_customlistview, null);

            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.name = vi.findViewById(R.id.itemNameTextView);
            viewHolder.price = vi.findViewById(R.id.itemPriceTextView);
            viewHolder.increase = vi.findViewById(R.id.increaseButton);
            viewHolder.decrease = vi.findViewById(R.id.decreaseButton);
            viewHolder.quantity = vi.findViewById(R.id.itemQuantityTextView);

//            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
//            vi.startAnimation(animation);



            viewHolder.increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int value = Integer.valueOf(viewHolder.quantity.getText().toString());
                    if (value < 10) {

                        viewHolder.quantity.setText(String.valueOf(value + 1));
                        FoodMenuDisplayActivity.cartItemQuantity.set(i, value + 1);
                        CartActivity.calcTotal();
                        Toast.makeText(context, "Cart Adjusted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            viewHolder.decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int value = FoodMenuDisplayActivity.cartItemQuantity.get(i);

//                    check for the error in removing the value of item from the cart
//                    mismatch in position
                    if (value > 1) {
                        value--;
                        viewHolder.quantity.setText(String.valueOf(value));
                        FoodMenuDisplayActivity.cartItemQuantity.set(i, value);
//                        CartActivity.cartDisplayAdapter.notifyDataSetChanged();
                        CartActivity.calcTotal();
                        Toast.makeText(context, "Cart Adjusted", Toast.LENGTH_SHORT).show();
                    }
                    else if (value == 1) {

                        vi.animate().alpha(0.0f).setDuration(750);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // yourMethod();
                                FoodMenuDisplayActivity.cartItemQuantity.remove(i);
                                FoodMenuDisplayActivity.cartItemPrice.remove(i);
                                FoodMenuDisplayActivity.cartItemName.remove(i);
                                FoodMenuDisplayActivity.cartItemCategory.remove(i);
//                        notifyDataSetChanged(); not working properly
//                        CartActivity.cartDisplayAdapter.notifyDataSetChanged();
                                CartActivity.setDisplayListView(context);
                                if(FoodMenuDisplayActivity.cartItemName.size() == 0){
//                            finish the activity
                                    Toast.makeText(context, "Cart is Empty", Toast.LENGTH_SHORT).show();
                                    CartActivity.activity.finish();
                                }
//                                CartActivity.notifyChangeAndCalcTotal(context);
                                CartActivity.calcTotal();
                                Toast.makeText(context, "Cart Adjusted", Toast.LENGTH_SHORT).show();
                            }
                        }, 750);

                    }
                }
            });
            viewHolder.name.setText(cartItemName.get(i));
            viewHolder.price.setText("Price: Rs. " + cartItemPrice.get(i));
            viewHolder.quantity.setText(cartItemQuantity.get(i).toString());

            vi.setTag(viewHolder);
        } else {
            vi = view;
            mainViewHolder = (ViewHolder) view.getTag();
        }


        return vi;
    }

    public class ViewHolder {
        TextView name, price, quantity;
        ImageButton increase, decrease;
    }
}
