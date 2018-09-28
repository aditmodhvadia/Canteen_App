package com.example.getfood.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.Fragment.ChineseFragment;
import com.example.getfood.Fragment.PizzaSandwichFragment;
import com.example.getfood.R;
import com.example.getfood.Fragment.SouthIndianFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FoodMenuDisplayActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DrawerLayout mDrawerLayout;
    FloatingActionButton floatingActionButton;
    FirebaseAuth auth;
    int exitCount;
    long currTime, prevTime;
    Button cartButton;

    public static ArrayList<String> cartItemName, cartItemCategory;
    public static ArrayList<Integer> cartItemQuantity, cartItemPrice;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_display);

        cartItemName = new ArrayList<>();
        cartItemCategory = new ArrayList<>();
        cartItemPrice = new ArrayList<>();
        cartItemQuantity = new ArrayList<>();
        cartButton = findViewById(R.id.cartButton);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        else{
            makeText("Action Bar null");
        }


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        if (menuItem.getItemId() == R.id.nav_cart) {
//                            Toast.makeText(FoodMenuDisplayActivity.this, "Cart Pressed", Toast.LENGTH_SHORT).show();
                            if (cartItemName.isEmpty())
                                makeText("Cart is Empty");
                            else
                                showCart();
                        }else if (menuItem.getItemId() == R.id.nav_order) {
//                            Toast.makeText(FoodMenuDisplayActivity.this, "Order Pressed", Toast.LENGTH_SHORT).show();
                        }else if (menuItem.getItemId() == R.id.nav_terms) {
//                            Toast.makeText(FoodMenuDisplayActivity.this, "Terms & Conditions Pressed", Toast.LENGTH_SHORT).show();
                        }else if (menuItem.getItemId() == R.id.nav_logout) {
//                            Toast.makeText(FoodMenuDisplayActivity.this, "Logout Pressed", Toast.LENGTH_SHORT).show();
                            logout();
                        } else {

                            Toast.makeText(FoodMenuDisplayActivity.this, String.valueOf(menuItem.getItemId()), Toast.LENGTH_SHORT).show();

                        }
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartItemName.isEmpty())
                    makeText("Cart is Empty");
                else
                    showCart();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_food_menu_display, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ChineseFragment();

                case 1:
                    return new SouthIndianFragment();

                case 2:
                    return new PizzaSandwichFragment();

                case 3:
                    return new ChineseFragment();

                case 4:
                    return new ChineseFragment();

                default:
                    return new ChineseFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
    }

    @Override
    public void onBackPressed() {
        exitCount++;
        if (exitCount == 1) {
            makeText("Press back once more to logout");
            prevTime = System.currentTimeMillis();
        }
        if (exitCount == 2) {
            currTime = System.currentTimeMillis();
            if (currTime - prevTime > 2000) {
                makeText("Press back once more to logout");
                prevTime = System.currentTimeMillis();
                exitCount = 1;
            } else {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.show();
    }

    private void showCart() {
        Intent i = new Intent(this, CartActivity.class);
        startActivity(i);
    }

    public void makeText(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
