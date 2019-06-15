package com.example.getfood.ui.foodmenu;

import com.example.getfood.ui.base.BaseMvpPresenter;
import com.fazemeright.canteen_app_models.models.CartItem;

import java.util.ArrayList;

public interface FoodCategoryMvpPresenter<V extends FoodCategoryMvpView> extends BaseMvpPresenter<V> {
    void fetchFoodList(String category);

    ArrayList<CartItem> getCartItems();

    void addFoodItemToCart(CartItem cartItem);
}
