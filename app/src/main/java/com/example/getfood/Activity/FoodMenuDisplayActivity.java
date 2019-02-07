package com.example.getfood.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.Fragment.FoodCategoryFragment;
import com.example.getfood.R;
import com.example.getfood.Utils.AlertUtils;
import com.example.getfood.Utils.OnDialogButtonClickListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FoodMenuDisplayActivity extends AppCompatActivity {


    public static ArrayList<String> cartItemName, cartItemCategory;
    public static ArrayList<Integer> cartItemQuantity, cartItemPrice;
    FloatingActionButton floatingActionButton;
    CoordinatorLayout coordinatorLayoutParent;
    FirebaseAuth auth;
    int exitCount;
    long currTime, prevTime;
    ImageButton helpButton;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_display);

        cartItemName = new ArrayList<>();
        cartItemCategory = new ArrayList<>();
        cartItemPrice = new ArrayList<>();
        cartItemQuantity = new ArrayList<>();
        helpButton = findViewById(R.id.helpButton);

        NavigationView navigationView = findViewById(R.id.nav_view);
        coordinatorLayoutParent = findViewById(R.id.CoordinatorLayoutParent);
        View headerView = navigationView.getHeaderView(0);
        TextView emailTextView = headerView.findViewById(R.id.emailTextView);

        auth = FirebaseAuth.getInstance();
        emailTextView.setText(auth.getCurrentUser().getEmail());


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /**
         * The {@link ViewPager} that will host the section contents.
         */
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        } else {
            makeText("Action Bar null");
        }


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mDrawerLayout = findViewById(R.id.drawer_layout);

//        NavigationView navigationView = findViewById(R.id.nav_view);
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
                                makeText(getString(R.string.cart_empty));
                            else
                                showCart();
                        } else if (menuItem.getItemId() == R.id.nav_order) {
                            Intent orders = new Intent(FoodMenuDisplayActivity.this, OrderListActivity.class);
                            String email = auth.getCurrentUser().getEmail();
                            String rollNo = email.substring(0, email.indexOf(getString(R.string.email_replace)));
                            orders.putExtra(getString(R.string.i_roll_no), rollNo);
                            startActivity(orders);
//                            Toast.makeText(FoodMenuDisplayActivity.this, "Order Pressed", Toast.LENGTH_SHORT).show();
                        } else if (menuItem.getItemId() == R.id.nav_terms) {
                            startActivity(new Intent(FoodMenuDisplayActivity.this, TermsActivity.class));
//                            Toast.makeText(FoodMenuDisplayActivity.this, "Terms & Conditions Pressed", Toast.LENGTH_SHORT).show();
                        } else if (menuItem.getItemId() == R.id.nav_map) {
                            startActivity(new Intent(FoodMenuDisplayActivity.this, MapsActivity.class));
//                            Toast.makeText(FoodMenuDisplayActivity.this, "Terms & Conditions Pressed", Toast.LENGTH_SHORT).show();
                        } else if (menuItem.getItemId() == R.id.nav_contact) {

                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.primary_email)});
                            email.putExtra(Intent.EXTRA_CC, new String[]{getString(R.string.email_1),
                                    getString(R.string.email_2)});
                            email.putExtra(Intent.EXTRA_SUBJECT, R.string.subject_email);

                            email.putExtra(Intent.EXTRA_TEXT, "Debug Information: " + Build.MANUFACTURER + "\n" + Build.DEVICE + "\n"
                                    + Build.BRAND + "\n" + Build.MODEL + "\nAPI Level: " + Build.VERSION.SDK_INT);

                            //need this to prompts email client only
                            email.setType(getString(R.string.email_type));

                            startActivity(Intent.createChooser(email, "Choose an Email client :"));

                        } else if (menuItem.getItemId() == R.id.nav_reset_password) {

                            auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(FoodMenuDisplayActivity.this, getString(R.string.pwd_reset_sent), Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FoodMenuDisplayActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else if (menuItem.getItemId() == R.id.nav_logout) {
//                            Toast.makeText(FoodMenuDisplayActivity.this, "Logout Pressed", Toast.LENGTH_SHORT).show();
                            logout();
                        } else if (menuItem.getItemId() == R.id.nav_help) {
                            Toast.makeText(FoodMenuDisplayActivity.this, getString(R.string.help_pressed), Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(FoodMenuDisplayActivity.this, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();

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
                if (cartItemName.isEmpty())
                    makeText(getString(R.string.cart_empty));
                else
                    showCart();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeText(getString(R.string.help_pressed));
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

    @Override
    public void onBackPressed() {
        exitCount++;
        if (exitCount == 1) {
            showSnackBar(coordinatorLayoutParent, getString(R.string.press_back_exit));
            prevTime = System.currentTimeMillis();
        }
        if (exitCount == 2) {
            currTime = System.currentTimeMillis();
            if (currTime - prevTime > 2000) {
                showSnackBar(coordinatorLayoutParent, getString(R.string.press_back_exit));
                prevTime = System.currentTimeMillis();
                exitCount = 1;
            } else {
//                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                homeIntent.addCategory(Intent.CATEGORY_HOME);
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(homeIntent);
                finishAffinity();

            }
        }
    }

    private void logout() {
        AlertUtils.openAlertDialog(this, getString(R.string.logout), getString(R.string.sure_logout),
                getString(R.string.yes), getString(R.string.no), new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        auth.signOut();
                        startActivity(new Intent(FoodMenuDisplayActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onNegativeButtonClicked() {

                    }
                });
    }

    private void showCart() {
        Intent i = new Intent(this, CartActivity.class);
        startActivity(i);
    }

    public void showSnackBar(View parent, String msg) {
        Snackbar snackbar = Snackbar
                .make(parent, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void makeText(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0: {
                    FoodCategoryFragment fragment = new FoodCategoryFragment();
                    Bundle args = new Bundle();
                    args.putString(getString(R.string.cat_type), getString(R.string.chinese));
                    fragment.setArguments(args);
                    return fragment;
                }
                case 1: {
                    FoodCategoryFragment fragment = new FoodCategoryFragment();
                    Bundle args = new Bundle();
                    args.putString(getString(R.string.cat_type), getString(R.string.south_indian));
                    fragment.setArguments(args);
                    return fragment;
                }
                case 2: {
                    FoodCategoryFragment fragment = new FoodCategoryFragment();
                    Bundle args = new Bundle();
                    args.putString(getString(R.string.cat_type), getString(R.string.pizza_sandwich));
                    fragment.setArguments(args);
                    return fragment;
                }
                default: {
                    FoodCategoryFragment fragment = new FoodCategoryFragment();
                    Bundle args = new Bundle();
                    args.putString(getString(R.string.cat_type), getString(R.string.chinese));
                    fragment.setArguments(args);
                    return fragment;
                }
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
