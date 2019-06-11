package com.example.getfood.ui.foodmenu;

import android.content.Context;

import com.example.getfood.models.FoodItem;
import com.example.getfood.ui.base.BaseView;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public interface FoodCategoryMvpView extends BaseView {

    Context getContext();

    void bindFoodListAdapter(ArrayList<FoodItem> foodItems);

    void onDatabaseError(DatabaseError databaseError);
}
