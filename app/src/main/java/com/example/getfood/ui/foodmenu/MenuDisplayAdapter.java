package com.example.getfood.ui.foodmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.getfood.models.FoodItem;
import com.example.getfood.R;

import java.util.ArrayList;
import java.util.List;

public class MenuDisplayAdapter extends BaseAdapter {

    private List<FoodItem> foodItem;
    private ArrayList<Integer> colors;
    private LayoutInflater inflater;
    private Context context;

    private TextView itemNameTextView, itemPriceTextView, itemRatingTextView;

    public MenuDisplayAdapter(List<FoodItem> foodItem, ArrayList<Integer> colors, Context context) {
        this.foodItem = foodItem;
        this.colors = colors;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return foodItem.size();
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
        itemNameTextView.setText(foodItem.get(i).getItemName());
        itemPriceTextView.setText(String.format("%s %s", context.getString(R.string.rupee_symbol), foodItem.get(i).getItemPrice()));
        itemRatingTextView.setText(foodItem.get(i).getItemRating());
        if (Float.valueOf(foodItem.get(i).getItemRating()) < 2.0) {
            itemRatingTextView.setTextColor(colors.get(2));
        } else if (Float.valueOf(foodItem.get(i).getItemRating()) < 3.5) {
            itemRatingTextView.setTextColor(colors.get(1));
        } else {
            itemRatingTextView.setTextColor(colors.get(0));
        }

        return vi;
    }
}
