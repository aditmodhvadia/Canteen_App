package com.example.getfood.ui.foodmenu

import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.canteen_app_models.models.CartItem
import com.example.canteen_app_models.models.FoodItem
import com.example.getfood.R
import com.example.getfood.callback.FoodItemTouchListener
import com.example.getfood.ui.base.BaseFragment
import com.example.getfood.utils.AppUtils
import com.example.getfood.utils.alert.AlertUtils
import com.example.getfood.utils.alert.DialogAddToCart.AddToCartDialogListener
import com.facebook.shimmer.ShimmerFrameLayout
import java.util.*

class FoodCategoryFragment : BaseFragment(), FoodCategoryMvpView, FoodItemTouchListener {
    private var CATEGORY: String? = null
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var mAdapter: FoodMenuDisplayAdapter
    private lateinit var presenter: FoodCategoryPresenter<FoodCategoryFragment>
    override fun initViews(view: View) {
        shimmerLayout = view.findViewById(R.id.shimmerLayout)
        val foodRecyclerView: RecyclerView = view.findViewById(R.id.foodDisplayRecyclerView)
        foodRecyclerView.layoutManager = LinearLayoutManager(context)
        foodRecyclerView.addItemDecoration(DividerItemDecoration(foodRecyclerView.context, LinearLayoutManager.VERTICAL))
        presenter = FoodCategoryPresenter()
        presenter.onAttach(this)
        val args = this.arguments
        if (args != null) {
            CATEGORY = args.getString(getString(R.string.cat_type))
        } else {
            Toast.makeText(context, getString(R.string.args_empty), Toast.LENGTH_SHORT).show()
        }
        mAdapter = FoodMenuDisplayAdapter(mContext!!, this)
        foodRecyclerView.adapter = mAdapter
        presenter.fetchFoodList(CATEGORY)
    }

    override fun bindFoodListAdapter(foodItems: ArrayList<FoodItem?>?) {
        val handler = Handler()
        handler.postDelayed({
            mAdapter.swapData(foodItems)
            if (shimmerLayout.isShimmerStarted) {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
        }, 500)
    }

    override fun onDatabaseError(error: Error?) {}
    override fun setListeners(view: View) {}
    override val layoutResId: Int
        get() = R.layout.fragment_food_category

    override fun onItemClicked(position: Int, foodItem: FoodItem?) {
        if (AppUtils.isItemInCart(presenter.cartItems, foodItem) != -1) {
            Toast.makeText(mContext, getString(R.string.item_in_cart), Toast.LENGTH_SHORT).show()
        } else {
            AlertUtils.showAddToCartDialog(mContext!!, foodItem, object : AddToCartDialogListener {
                override fun onAddToCartClicked(quantity: Int) {
                    if (quantity != 0) {
                        val probablePosition: Int = AppUtils.Companion.isItemInCart(presenter.cartItems, foodItem)
                        if (probablePosition != -1) {
//                            item is in cart, so increase quantity disabling for them
//                            TODO: Implement later
                            /*FoodMenuDisplayActivity.cartItems.get(probablePosition)
                                    .setItemQuantity(FoodMenuDisplayActivity.cartItems
                                            .get(probablePosition).getItemQuantity() + quantity);*/
                        } else {
                            presenter.addFoodItemToCart(CartItem(quantity,
                                    "Order-Placed", foodItem!!))
                        }
                        Toast.makeText(mContext, getString(R.string.add_to_cart), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelClicked() {}
                override fun onIncreaseQuantityClicked() {}
                override fun onDecreaseQuantityClicked() {}
            })
        }
    }
}