package com.example.getfood.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.getfood.FoodItem;
import com.example.getfood.R;

import java.util.ArrayList;

public class MenuDisplayAdapter extends BaseAdapter {

    ArrayList<String> itemName,itemPrice, itemRating;
    ArrayList<Integer> colors;
    Context context;
    LayoutInflater inflater;

    TextView itemNameTextView, itemPriceTextView, itemRatingTextView;

    public MenuDisplayAdapter(FoodItem foodList, ArrayList<Integer> colors, Context context) {
        this.itemName = foodList.getItemName();
        this.itemPrice = foodList.getItemPrice();
        this.itemRating = foodList.getItemRating();
        this.colors = colors;
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
            itemRatingTextView.setTextColor(colors.get(2));
        } else if(Float.valueOf(itemRating.get(i))<3.5){
            itemRatingTextView.setTextColor(colors.get(1));
        } else{
            itemRatingTextView.setTextColor(colors.get(0));
        }

        return vi;
    }
}
