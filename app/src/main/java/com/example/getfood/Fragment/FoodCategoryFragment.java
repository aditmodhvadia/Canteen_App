package com.example.getfood.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodCategoryFragment extends Fragment {

    private DatabaseReference rootFood;
    ProgressDialog progressDialog;
    ArrayList<String> itemName, itemPrice, itemRating;
    ArrayList<Integer> colors;

    String CATEGORY = null;

    Button alertPlus, alertMinus;
    TextView quantitySetTV;

    ListView chineseDisplayListView;
    MenuDisplayAdapter displayAdapter;

    private OnFragmentInteractionListener mListener;

    public FoodCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chinese, container, false);

//        progressDialog = new ProgressDialog(getContext());
        itemName = new ArrayList<>();
        itemPrice = new ArrayList<>();
        itemRating = new ArrayList<>();
        colors = new ArrayList<>();

        colors.add(getResources().getColor(R.color.colorGoodRating));
        colors.add(getResources().getColor(R.color.colorMediumRating));
        colors.add(getResources().getColor(R.color.colorBadRating));
        chineseDisplayListView = v.findViewById(R.id.chineseDisplayListView);

        Bundle args = this.getArguments();

        if (args != null) {
            CATEGORY = args.getString("CATEGORY_TYPE");
            Toast.makeText(getContext(), CATEGORY, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), "Empty args", Toast.LENGTH_SHORT).show();
        }
        rootFood = FirebaseDatabase.getInstance().getReference().child("Food").child(CATEGORY);

        rootFood.keepSynced(true);

        rootFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemName.clear();
                itemPrice.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    if(dsp.child("Available").getValue().toString().equals("Yes")){
                        itemName.add(dsp.getKey());
                        itemPrice.add(dsp.child("Price").getValue().toString());
                        itemRating.add(dsp.child("Rating").getValue().toString());
                    }
                }

                FoodItem chinese = new FoodItem(itemName, itemPrice, itemRating);

                displayAdapter = new MenuDisplayAdapter(chinese, colors, getContext());
                chineseDisplayListView.setAdapter(displayAdapter);

//                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        chineseDisplayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if (FoodMenuDisplayActivity.cartItemName.contains(itemName.get(i))) {
                    Toast.makeText(getContext(), "Item already in Cart", Toast.LENGTH_SHORT).show();
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

                    builder.setTitle("Select Quantity");
                    builder.setMessage(itemName.get(i));
                    builder.setView(quantityAlert);
                    builder.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i1) {
                            int quant = Integer.valueOf(quantitySetTV.getText().toString());
                            if (quant != 0) {
                                if (FoodMenuDisplayActivity.cartItemName.contains(itemName.get(i))) {
                                    int pos = FoodMenuDisplayActivity.cartItemName.indexOf(itemName.get(i));
                                    FoodMenuDisplayActivity.cartItemQuantity.set(pos, FoodMenuDisplayActivity.cartItemQuantity.get(pos) + quant);
                                } else {
                                    FoodMenuDisplayActivity.cartItemName.add(itemName.get(i));
                                    FoodMenuDisplayActivity.cartItemQuantity.add(quant);
                                    FoodMenuDisplayActivity.cartItemPrice.add(Integer.parseInt(itemPrice.get(i)));
                                    FoodMenuDisplayActivity.cartItemCategory.add(CATEGORY);
                                }
                                Toast.makeText(getContext(), "Added to Cart", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
