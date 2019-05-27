package com.example.getfood.ui.foodmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.models.FoodItem;
import com.example.getfood.utils.AppUtils;

import java.util.List;

public class MenuDisplayAdapter extends BaseAdapter {

    private List<FoodItem> foodItemList;
    private LayoutInflater inflater;
    private Context context;

    private TextView itemNameTextView, itemPriceTextView, itemRatingTextView;
    private ImageView ivRatingStar;

    public MenuDisplayAdapter(List<FoodItem> foodItemList, Context context) {
        this.foodItemList = foodItemList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return foodItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return foodItemList.get(i);
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
        ivRatingStar = vi.findViewById(R.id.ivRatingStar);

        itemNameTextView.setText(foodItemList.get(i).getItemName());
        itemPriceTextView.setText(String.format("%s %s", context.getString(R.string.rupee_symbol), foodItemList.get(i).getItemPrice()));
        if (foodItemList.get(i).getItemRating() != null) {
            itemRatingTextView.setText(foodItemList.get(i).getItemRating());
            itemRatingTextView.setTextColor(AppUtils.getColorForRating(context, foodItemList.get(i).getItemRating()));

        } else {
            itemRatingTextView.setVisibility(View.INVISIBLE);
            ivRatingStar.setVisibility(View.INVISIBLE);
        }

        return vi;
    }
}
