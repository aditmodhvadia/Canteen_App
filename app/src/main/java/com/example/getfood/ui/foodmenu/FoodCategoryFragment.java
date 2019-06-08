package com.example.getfood.ui.foodmenu;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.models.CartItem;
import com.example.getfood.models.FoodItem;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.AppUtils;
import com.example.getfood.utils.DialogAddToCart;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodCategoryFragment extends Fragment {

    List<FoodItem> foodItem;
    String CATEGORY = null;
    ListView foodDisplayListView;
    MenuDisplayAdapter displayAdapter;
    ShimmerFrameLayout shimmerLayout;

    public FoodCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chinese, container, false);
        shimmerLayout = v.findViewById(R.id.shimmerLayout);
        foodDisplayListView = v.findViewById(R.id.chineseDisplayListView);
        foodItem = new ArrayList<>();

        Bundle args = this.getArguments();

        if (args != null) {
            CATEGORY = args.getString(getString(R.string.cat_type));
        } else {
            Toast.makeText(getContext(), getString(R.string.args_empty), Toast.LENGTH_SHORT).show();
        }
        DatabaseReference rootFood = FirebaseDatabase.getInstance().getReference().child(getString(R.string.food)).child(CATEGORY);

        rootFood.keepSynced(true);

        rootFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodItem.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    if (dsp.hasChild(getString(R.string.available)) &&
                            dsp.child(getString(R.string.available)).getValue().toString().equals(getString(R.string.yes))) {
                        FoodItem newItem = new FoodItem(dsp.getKey(),
                                dsp.hasChild(getString(R.string.price)) ?
                                        dsp.child(getString(R.string.price)).getValue().toString() : null,
                                dsp.hasChild(getString(R.string.rating)) ?
                                        dsp.child(getString(R.string.rating)).getValue().toString() : null, CATEGORY);
//                        TODO: parse FoodItem model directly
                        foodItem.add(newItem);
                    }
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        displayAdapter = new MenuDisplayAdapter(foodItem, getContext());
                        foodDisplayListView.setAdapter(displayAdapter);
                        shimmerLayout.stopShimmer();
                        shimmerLayout.setVisibility(View.GONE);
                    }
                }, 1000);


//                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        foodDisplayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                if (AppUtils.isItemInCart(FoodMenuDisplayActivity.cartItems, foodItem.get(position)) != -1) {
                    Toast.makeText(getContext(), getString(R.string.item_in_cart), Toast.LENGTH_SHORT).show();
                } else {
                    AlertUtils.showAddToCartDialog(getContext(), foodItem.get(position), new DialogAddToCart.AddToCartDialogListener() {

                        @Override
                        public void onAddToCartClicked(int quantity) {
                            if (quantity != 0) {
                                int probablePosition = AppUtils.isItemInCart(FoodMenuDisplayActivity.cartItems, foodItem.get(position));
                                if (probablePosition != -1) {
                                    FoodMenuDisplayActivity.cartItems.get(probablePosition)
                                            .setItemQuantity(FoodMenuDisplayActivity.cartItems
                                                    .get(probablePosition).getItemQuantity() + quantity);
                                } else {
                                    FoodMenuDisplayActivity.cartItems.add(new CartItem(foodItem.get(position),
                                            "Order-Placed", quantity));
                                }
                                Toast.makeText(getContext(), getString(R.string.add_to_cart), Toast.LENGTH_LONG).show();
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
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
