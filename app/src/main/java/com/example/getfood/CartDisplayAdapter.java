package com.example.getfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CartDisplayAdapter extends BaseAdapter {

    ArrayList<String> cartItemName;
    ArrayList<Integer> cartItemQuantity, cartItemPrice;
    Context context;

    TextView itemQuantityTextView,itemNameTextView,itemPriceTextView;

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
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vi = view;
        vi = inflater.inflate(R.layout.cart_display_customlistview, null);
        itemNameTextView = vi.findViewById(R.id.itemNameTextView);
        itemPriceTextView = vi.findViewById(R.id.itemPriceTextView);
        itemQuantityTextView = vi.findViewById(R.id.itemQuantityTextView);

        itemNameTextView.setText(cartItemName.get(i));
        itemQuantityTextView.setText(cartItemQuantity.get(i).toString());
        itemPriceTextView.setText("Price: Rs. "+cartItemPrice.get(i).toString());

        return vi;
    }
}
