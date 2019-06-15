package com.example.getfood.ui.foodmenu;

import android.support.annotation.NonNull;

import com.example.getfood.ui.base.BasePresenter;
import com.fazemeright.canteen_app_models.helpers.FoodMenuDetails;
import com.fazemeright.canteen_app_models.models.CartItem;
import com.fazemeright.canteen_app_models.models.FoodItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodCategoryPresenter<V extends FoodCategoryMvpView> extends BasePresenter<V> implements FoodCategoryMvpPresenter<V> {

    public FoodCategoryPresenter() {
    }


    @Override
    public void fetchFoodList(final String category) {

        apiManager.foodMenuListener(category, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<FoodItem> foodItems = new ArrayList<>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    FoodItem foodItem = FoodItem.fromMap(dsp.getValue(), category, dsp.getKey());

                    if (foodItem != null) {
//                        Log.d("##DebugData", foodItem.toString());
                    } else {
//                        Log.d("##DebugData", "\n\t\t Test Item null");
                    }
//                    todo: Create a method to convert from HashMap instead of this
                    if (dsp.hasChild(FoodMenuDetails.AVAILABLE) &&
                            dsp.child(FoodMenuDetails.AVAILABLE).getValue().toString().equals("Yes")) {
                        foodItems.add(foodItem);
                    }
                }
                getMvpView().bindFoodListAdapter(foodItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public ArrayList<CartItem> getCartItems() {
        return dataManager.getCartItems();
    }

    @Override
    public void addFoodItemToCart(CartItem cartItem) {
        dataManager.getCartItems().add(cartItem);
    }
}
