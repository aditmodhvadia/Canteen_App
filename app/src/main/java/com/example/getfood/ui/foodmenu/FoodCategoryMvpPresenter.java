package com.example.getfood.ui.foodmenu;

import com.example.getfood.ui.base.BaseMvpPresenter;

public interface FoodCategoryMvpPresenter<V extends FoodCategoryMvpView> extends BaseMvpPresenter<V> {
    void fetchFoodList(String category);
}
