package com.example.getfood.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.Activity.FoodMenuDisplayActivity;
import com.example.getfood.Adapter.MenuDisplayAdapter;
import com.example.getfood.FoodItem;
import com.example.getfood.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodCategoryFragment extends Fragment {

    ProgressDialog progressDialog;
    List<FoodItem> foodItem;
    ArrayList<Integer> colors;
    String CATEGORY = null;
    Button alertPlus, alertMinus;
    TextView quantitySetTV;
    ListView chineseDisplayListView;
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
//        progressDialog = new ProgressDialog(getContext());
        foodItem = new ArrayList<>();

        colors = new ArrayList<>();

        colors.add(getResources().getColor(R.color.colorGoodRating));
        colors.add(getResources().getColor(R.color.colorMediumRating));
        colors.add(getResources().getColor(R.color.colorBadRating));
        chineseDisplayListView = v.findViewById(R.id.chineseDisplayListView);

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

                    if (dsp.child(getString(R.string.available)).getValue().toString().equals(getString(R.string.yes))) {
                        FoodItem newItem = new FoodItem(dsp.getKey(), dsp.child(getString(R.string.price)).getValue().toString(), dsp.child(getString(R.string.rating)).getValue().toString());
                        foodItem.add(newItem);
                    }
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        displayAdapter = new MenuDisplayAdapter(foodItem, colors, getContext());
                        chineseDisplayListView.setAdapter(displayAdapter);
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

        chineseDisplayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if (FoodMenuDisplayActivity.cartItemName.contains(foodItem.get(i).getItemName())) {
                    Toast.makeText(getContext(), getString(R.string.item_in_cart), Toast.LENGTH_SHORT).show();
                } else {

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

                    View quantityAlert = getLayoutInflater().inflate(R.layout.adjust_quantity_display, null);
                    alertPlus = quantityAlert.findViewById(R.id.alertPlus);
                    alertMinus = quantityAlert.findViewById(R.id.alertMinus);
                    quantitySetTV = quantityAlert.findViewById(R.id.quantitySetTextView);
                    alertPlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Integer.parseInt(quantitySetTV.getText().toString()) < 5) {
                                quantitySetTV.setText(String.valueOf(Integer.valueOf(quantitySetTV.getText().toString()) + 1));
                            }
                        }
                    });
                    alertMinus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Integer.parseInt(quantitySetTV.getText().toString()) > 0) {
                                quantitySetTV.setText(String.valueOf(Integer.valueOf(quantitySetTV.getText().toString()) - 1));
                            }
                        }
                    });

                    builder.setTitle(R.string.select_quantity);
                    builder.setMessage(foodItem.get(i).getItemName());
                    builder.setView(quantityAlert);
                    builder.setPositiveButton(R.string.add_to_cart, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i1) {
                            int quant = Integer.valueOf(quantitySetTV.getText().toString());
                            if (quant != 0) {
                                if (FoodMenuDisplayActivity.cartItemName.contains(foodItem.get(i).getItemName())) {
                                    int pos = FoodMenuDisplayActivity.cartItemName.indexOf(foodItem.get(i).getItemName());
                                    FoodMenuDisplayActivity.cartItemQuantity.set(pos, FoodMenuDisplayActivity.cartItemQuantity.get(pos) + quant);
                                } else {
                                    FoodMenuDisplayActivity.cartItemName.add(foodItem.get(i).getItemName());
                                    FoodMenuDisplayActivity.cartItemQuantity.add(quant);
                                    FoodMenuDisplayActivity.cartItemPrice.add(Integer.parseInt(foodItem.get(i).getItemPrice()));
                                    FoodMenuDisplayActivity.cartItemCategory.add(CATEGORY);
                                }
                                Toast.makeText(getContext(), getString(R.string.add_to_cart), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    android.support.v7.app.AlertDialog dialog = builder.show();
                    Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(getResources().getColor(R.color.colorPrimary));

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
