package com.example.getfood.ui.foodmenu;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.models.FoodItem;
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
    ShimmerFrameLayout shimmerLayout;
    private RecyclerView foodDisplayListView;
    private FoodMenuRecyclerViewDisplayAdapter mAdapter;

    public FoodCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_food_category, container, false);
        shimmerLayout = v.findViewById(R.id.shimmerLayout);
        foodDisplayListView = v.findViewById(R.id.foodDisplayListView);
        foodDisplayListView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodItem = new ArrayList<>();

//        Log.d("##DebugData", AppUtils.getInstance(getContext()).generateString());
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
                        mAdapter = new FoodMenuRecyclerViewDisplayAdapter(foodItem, getContext());
                        foodDisplayListView.setAdapter(mAdapter);
                        shimmerLayout.stopShimmer();
                        shimmerLayout.setVisibility(View.GONE);
                    }
                }, 500);


//                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
