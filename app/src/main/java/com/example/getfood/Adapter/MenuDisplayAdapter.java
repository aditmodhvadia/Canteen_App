package com.example.getfood.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.getfood.R;

import java.util.ArrayList;

public class MenuDisplayAdapter extends BaseAdapter {

    ArrayList<String> itemName,itemPrice, itemRating;
    Context context;
    LayoutInflater inflater;

    TextView itemNameTextView, itemPriceTextView, itemRatingTextView;

    public MenuDisplayAdapter(ArrayList<String> itemName, ArrayList<String> itemPrice, ArrayList<String> itemRating, Context context) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemRating = itemRating;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemName.size();
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

        View vi;
        vi = inflater.inflate(R.layout.food_menu_display_custom_listview, null);
        itemNameTextView = vi.findViewById(R.id.itemNameTextView);
        itemPriceTextView = vi.findViewById(R.id.itemPriceTextView);
        itemRatingTextView = vi.findViewById(R.id.itemRatingTextView);
        itemNameTextView.setText(itemName.get(i));
        itemPriceTextView.setText(String.format("₹ %s", itemPrice.get(i)));
        itemRatingTextView.setText(itemRating.get(i));
        if(Float.valueOf(itemRating.get(i))<2.0){
            itemRatingTextView.setTextColor(Color.RED);
        } else if(Float.valueOf(itemRating.get(i))<3.5){
            itemRatingTextView.setTextColor(Color.YELLOW);
        } else{
            itemRatingTextView.setTextColor(Color.GREEN);
        }

        return vi;
    }
}
