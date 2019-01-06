package com.example.getfood.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.Fragment.FoodCategoryFragment;
import com.example.getfood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FoodMenuDisplayActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DrawerLayout mDrawerLayout;
    FloatingActionButton floatingActionButton;
    CoordinatorLayout coordinatorLayoutParent;
    FirebaseAuth auth;
    int exitCount;
    long currTime, prevTime;
    ImageButton helpButton;

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
        helpButton = findViewById(R.id.helpButton);

        NavigationView navigationView = findViewById(R.id.nav_view);
        coordinatorLayoutParent = findViewById(R.id.CoordinatorLayoutParent);
        View headerView = navigationView.getHeaderView(0);
        TextView emailTextView = headerView.findViewById(R.id.emailTextView);

        auth = FirebaseAuth.getInstance();
        emailTextView.setText(auth.getCurrentUser().getEmail());


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
                                makeText("Cart is Empty");
                            else
                                showCart();
                        } else if (menuItem.getItemId() == R.id.nav_order) {
                            Intent orders = new Intent(FoodMenuDisplayActivity.this, OrderListActivity.class);
                            String email = auth.getCurrentUser().getEmail();
                            String rollNo = email.substring(0, email.indexOf("@"));
                            orders.putExtra("RollNo", rollNo);
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
                            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"adit.modhvadia@gmail.com"});
                            email.putExtra(Intent.EXTRA_CC, new String[]{"15bce001@nirmauni.ac.in",
                                    "15bce014@nirmauni.ac.in"});
                            email.putExtra(Intent.EXTRA_SUBJECT, "Query/Report for my Kanteen");

                            email.putExtra(Intent.EXTRA_TEXT, "Debug Information: " + Build.MANUFACTURER + "\n" + Build.DEVICE + "\n"
                                    + Build.BRAND + "\n" + Build.MODEL + "\nAPI Level: " + Build.VERSION.SDK_INT);

                            //need this to prompts email client only
                            email.setType("message/rfc822");

                            startActivity(Intent.createChooser(email, "Choose an Email client :"));

                        } else if (menuItem.getItemId() == R.id.nav_reset_password) {

                            auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(FoodMenuDisplayActivity.this, "Password Reset Email Sent!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(FoodMenuDisplayActivity.this, "Help Pressed", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(FoodMenuDisplayActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();

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
                    makeText("Cart is Empty");
                else
                    showCart();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeText("Help Pressed");
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
                case 0: {
                    FoodCategoryFragment fragment = new FoodCategoryFragment();
                    Bundle args = new Bundle();
                    args.putString("CATEGORY_TYPE", "Chinese");
                    fragment.setArguments(args);
                    return fragment;
                }
                case 1: {
                    FoodCategoryFragment fragment = new FoodCategoryFragment();
                    Bundle args = new Bundle();
                    args.putString("CATEGORY_TYPE", "South Indian");
                    fragment.setArguments(args);
                    return fragment;
                }
                case 2: {
                    FoodCategoryFragment fragment = new FoodCategoryFragment();
                    Bundle args = new Bundle();
                    args.putString("CATEGORY_TYPE", "Pizza Sandwich");
                    fragment.setArguments(args);
                    return fragment;
                }
                default: {
                    FoodCategoryFragment fragment = new FoodCategoryFragment();
                    Bundle args = new Bundle();
                    args.putString("CATEGORY_TYPE", "Chinese");
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

    @Override
    public void onBackPressed() {
        exitCount++;
        if (exitCount == 1) {
            showSnackBar(coordinatorLayoutParent, "Press back once more to exit");
            prevTime = System.currentTimeMillis();
        }
        if (exitCount == 2) {
            currTime = System.currentTimeMillis();
            if (currTime - prevTime > 2000) {
                showSnackBar(coordinatorLayoutParent, "Press back once more to exit");
                prevTime = System.currentTimeMillis();
                exitCount = 1;
            } else {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);

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
                auth.signOut();
                startActivity(new Intent(FoodMenuDisplayActivity.this, LoginActivity.class));
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.show();

        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.colorPrimary));
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.colorPrimary));
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
}
