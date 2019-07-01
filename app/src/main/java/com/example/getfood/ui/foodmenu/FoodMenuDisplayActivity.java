package com.example.getfood.ui.foodmenu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.ui.base.BaseActivity;
import com.example.getfood.ui.cart.CartActivity;
import com.example.getfood.ui.loginregister.LoginActivity;
import com.example.getfood.ui.map.MapsActivity;
import com.example.getfood.ui.orderlist.OrderListActivity;
import com.example.getfood.ui.terms.TermsActivity;
import com.example.getfood.utils.alert.AlertUtils;
import com.example.getfood.utils.alert.DialogConfirmation;

public class FoodMenuDisplayActivity extends BaseActivity implements FoodMenuDisplayActivityMvpView {


    CoordinatorLayout coordinatorLayoutParent;
    private int exitCount;
    private long currTime, prevTime;
    private DrawerLayout mDrawerLayout;
    private FoodMenuDisplayActivityPresenter<FoodMenuDisplayActivity> presenter;

    private FoodCategoryFragment chineseFragment;
    private FoodCategoryFragment southIndianFragment;
    private FoodCategoryFragment sandwichPizzaFragment;

    @Override
    public void initViews() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        coordinatorLayoutParent = findViewById(R.id.CoordinatorLayoutParent);
        View headerView = navigationView.getHeaderView(0);
        TextView emailTextView = headerView.findViewById(R.id.emailTextView);

        presenter = new FoodMenuDisplayActivityPresenter<>();
        presenter.onAttach(this);
//        TODO: Move to Mvp
        emailTextView.setText(presenter.getUserEmail());


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

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openCart();
                openCart();
            }
        });

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
                            presenter.openCart();
                        } else if (menuItem.getItemId() == R.id.nav_order) {
                            openOrderListActivity();
                        } else if (menuItem.getItemId() == R.id.nav_terms) {
                            openTermsActivity();
                        } else if (menuItem.getItemId() == R.id.nav_map) {
                            openMapActivity();
                        } else if (menuItem.getItemId() == R.id.nav_contact) {
                            openEmailClient();
                        } else if (menuItem.getItemId() == R.id.nav_reset_password) {
                            presenter.sendPasswordResetEmail();

                        } else if (menuItem.getItemId() == R.id.nav_logout) {
                            logout();
                        } else if (menuItem.getItemId() == R.id.nav_help) {
                            Toast.makeText(FoodMenuDisplayActivity.this, getString(R.string.help_pressed), Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(FoodMenuDisplayActivity.this, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();

                        }
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onPasswordResetEmailSentSuccessfully() {
        Toast.makeText(FoodMenuDisplayActivity.this, getString(R.string.pwd_reset_sent), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPasswordResetEmailSentFailed(String errMsg) {
        Toast.makeText(FoodMenuDisplayActivity.this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_food_menu_display;
    }

    private void openTermsActivity() {
        startActivity(new Intent(FoodMenuDisplayActivity.this, TermsActivity.class));
    }

    private void openOrderListActivity() {
        startActivity(new Intent(FoodMenuDisplayActivity.this, OrderListActivity.class));
    }

    private void openMapActivity() {
        startActivity(new Intent(FoodMenuDisplayActivity.this, MapsActivity.class));
    }

    private void openEmailClient() {
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


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menuHelp:
                makeText(getString(R.string.help_pressed));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_help, menu);
        return true;
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
                finishAffinity();

            }
        }
    }

    private void logout() {
        AlertUtils.showConfirmationDialog(this, getString(R.string.logout), getString(R.string.sure_logout),
                getString(R.string.yes), getString(R.string.no), new DialogConfirmation.ConfirmationDialogListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        presenter.signOutUser();
                        startActivity(new Intent(FoodMenuDisplayActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onNegativeButtonClicked() {

                    }
                });
    }

    @Override
    public void openCart() {
        startActivity(new Intent(this, CartActivity.class));
    }

    @Override
    public void cantOpenCart() {
        makeText(getString(R.string.cart_empty));
    }

    public void showSnackBar(View parent, String msg) {
        Snackbar snackbar = Snackbar
                .make(parent, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void makeText(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
                    if (chineseFragment == null) {
                        chineseFragment = new FoodCategoryFragment();
                        Bundle args = new Bundle();
                        args.putString(getString(R.string.cat_type), getString(R.string.chinese));
                        chineseFragment.setArguments(args);
                    }
                    return chineseFragment;
                }
                case 1: {
                    if (southIndianFragment == null) {
                        southIndianFragment = new FoodCategoryFragment();
                        Bundle args = new Bundle();
                        args.putString(getString(R.string.cat_type), getString(R.string.south_indian));
                        southIndianFragment.setArguments(args);
                    }
                    return southIndianFragment;
                }
                case 2: {
                    if (sandwichPizzaFragment == null) {
                        sandwichPizzaFragment = new FoodCategoryFragment();
                        Bundle args = new Bundle();
                        args.putString(getString(R.string.cat_type), getString(R.string.pizza_sandwich));
                        sandwichPizzaFragment.setArguments(args);
                    }
                    return sandwichPizzaFragment;
                }
                default: {
                    if (chineseFragment == null) {
                        chineseFragment = new FoodCategoryFragment();
                        Bundle args = new Bundle();
                        args.putString(getString(R.string.cat_type), getString(R.string.chinese));
                        chineseFragment.setArguments(args);
                    }
                    return chineseFragment;
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
