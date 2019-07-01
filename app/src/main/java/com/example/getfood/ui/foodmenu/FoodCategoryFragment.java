package com.example.getfood.ui.foodmenu;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.callback.FoodItemTouchListener;
import com.example.getfood.ui.base.BaseFragment;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.AppUtils;
import com.example.getfood.utils.DialogAddToCart;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fazemeright.canteen_app_models.models.CartItem;
import com.fazemeright.canteen_app_models.models.FoodItem;

import java.util.ArrayList;

public class FoodCategoryFragment extends BaseFragment implements FoodCategoryMvpView, FoodItemTouchListener {

    private String CATEGORY = null;
    private ShimmerFrameLayout shimmerLayout;
    private FoodMenuRecyclerViewDisplayAdapter mAdapter;
    private FoodCategoryPresenter<FoodCategoryFragment> presenter;

    public FoodCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void initViews(View view) {
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        RecyclerView foodRecyclerView = view.findViewById(R.id.foodDisplayRecyclerView);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodRecyclerView.addItemDecoration(new DividerItemDecoration(foodRecyclerView.getContext(), LinearLayoutManager.VERTICAL));

        presenter = new FoodCategoryPresenter<>();
        presenter.onAttach(this);

        Bundle args = this.getArguments();

        if (args != null) {
            CATEGORY = args.getString(getString(R.string.cat_type));
        } else {
            Toast.makeText(getContext(), getString(R.string.args_empty), Toast.LENGTH_SHORT).show();
        }
        mAdapter = new FoodMenuRecyclerViewDisplayAdapter(mContext, this);
        foodRecyclerView.setAdapter(mAdapter);

        presenter.fetchFoodList(CATEGORY);
    }

    @Override
    public void bindFoodListAdapter(final ArrayList<FoodItem> foodItems) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.swapData(foodItems);
                if (shimmerLayout.isShimmerStarted()) {
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.GONE);
                }
            }
        }, 500);
    }

    @Override
    public void onDatabaseError(Error error) {

    }

    @Override
    public void setListeners(View view) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_food_category;
    }

    @Override
    public void onItemClicked(int position, final FoodItem foodItem) {
        if (presenter.getCartItems() != null && AppUtils.isItemInCart(presenter.getCartItems(), foodItem) != -1) {
            Toast.makeText(mContext, getString(R.string.item_in_cart), Toast.LENGTH_SHORT).show();
        } else {
            AlertUtils.showAddToCartDialog(mContext, foodItem, new DialogAddToCart.AddToCartDialogListener() {

                @Override
                public void onAddToCartClicked(int quantity) {
                    if (quantity != 0) {
                        int probablePosition = AppUtils.isItemInCart(presenter.getCartItems(), foodItem);
                        if (probablePosition != -1) {
//                            item is in cart, so increase quantity disabling for them
//                            TODO: Implement later
                            /*FoodMenuDisplayActivity.cartItems.get(probablePosition)
                                    .setItemQuantity(FoodMenuDisplayActivity.cartItems
                                            .get(probablePosition).getItemQuantity() + quantity);*/
                        } else {
                            presenter.addFoodItemToCart(new CartItem(foodItem,
                                    "Order-Placed", quantity));
                        }
                        Toast.makeText(mContext, getString(R.string.add_to_cart), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelClicked() {

                }

                @Override
                public void onIncreaseQuantityClicked() {

                }

                @Override
                public void onDecreaseQuantityClicked() {

                }
            });
        }
    }
}
