package com.example.getfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SouthIndianFragment extends Fragment {

    private DatabaseReference rootFood;
    ProgressDialog progressDialog;
    private String CATEGORY = "South Indian";
    ArrayList<String> itemName, itemPrice;

    ListView southIndianDisplayListView;
    MenuDisplayAdapter displayAdapter;

    private OnFragmentInteractionListener mListener;

    public SouthIndianFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_south_indian, container, false);

//        progressDialog = new ProgressDialog(getContext());
        itemName = new ArrayList<String>();
        itemPrice = new ArrayList<String>();
        southIndianDisplayListView = v.findViewById(R.id.southIndianDisplayListView);
//        display progress dialog till data is fetched
//        progressDialog.setTitle("Please Wait..");
//        progressDialog.setMessage("Fetching data");
//        progressDialog.show();
//    create instance of database and keep it synced locally as well
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
                southIndianDisplayListView.setAdapter(displayAdapter);


//                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
