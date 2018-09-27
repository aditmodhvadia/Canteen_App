package com.example.getfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.getfood.R;

import java.util.ArrayList;

public class MenuDisplayAdapter extends BaseAdapter {

    ArrayList<String> itemName,itemPrice;
    Context context;
    LayoutInflater inflater;

    TextView itemNameTextView, itemPriceTextView;

    public MenuDisplayAdapter(ArrayList<String> itemName, ArrayList<String> itemPrice, Context context) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
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

        itemNameTextView.setText(itemName.get(i));
        itemPriceTextView.setText(String.format("Price: Rs. %s", itemPrice.get(i)));

        return vi;
    }
}
