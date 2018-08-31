package com.example.getfood;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChineseFragment extends Fragment {

    private DatabaseReference rootFood;
    ProgressDialog progressDialog;
    ArrayList<String> itemName, itemPrice;

    ListView chineseDisplayListView;
    MenuDisplayAdapter displayAdapter;

    private OnFragmentInteractionListener mListener;

    public ChineseFragment() {
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

        chineseDisplayListView = v.findViewById(R.id.chineseDisplayListView);
//        display progress dialog till data is fetched
//        progressDialog.setTitle("Please Wait..");
//        progressDialog.setMessage("Fetching data");
//        progressDialog.show();
//    create instance of database and keep it synced locally as well
        String CATEGORY = "Chinese";
        rootFood = FirebaseDatabase.getInstance().getReference().child("Food").child(CATEGORY);

        rootFood.keepSynced(true);

        rootFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemName.clear();
                itemPrice.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    itemName.add(dsp.getKey());
                    itemPrice.add(dsp.child("Price").getValue().toString());

                }
                displayAdapter = new MenuDisplayAdapter(itemName,itemPrice,getContext());
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
//                TODO: AlertDialog which confirms to add the item to cart

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setTitle("Select Quantity");
//                builder.setMessage("Are you sure you want to Logout?");
                builder.setView(R.layout.adjust_quantity_display);
                builder.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i1) {
                        if(FoodMenuDisplayActivity.cartItemName.contains(itemName.get(i))){
                            int pos = FoodMenuDisplayActivity.cartItemName.indexOf(itemName.get(i));
                            FoodMenuDisplayActivity.cartItemQuantity.set(pos, FoodMenuDisplayActivity.cartItemQuantity.get(pos)+1);
                        }
                        else{
                            FoodMenuDisplayActivity.cartItemName.add(itemName.get(i));
                            FoodMenuDisplayActivity.cartItemQuantity.add(1);
                            FoodMenuDisplayActivity.cartItemPrice.add(Integer.parseInt(itemPrice.get(i)));
                        }

//                        Toast.makeText(getContext(),FoodMenuDisplayActivity.cartItemName.toString() + "\n"
//                                +FoodMenuDisplayActivity.cartItemQuantity.toString(),Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                android.support.v7.app.AlertDialog dialog = builder.show();


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
