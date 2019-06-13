package com.example.getfood.ui.loginregister;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.getfood.R;
import com.example.getfood.ui.base.BaseActivity;
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity;
import com.example.getfood.ui.loginregister.loginfragment.LoginFragment;
import com.example.getfood.ui.loginregister.registerfragment.RegisterFragment;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.DialogSimple;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity implements LoginActivityMvpView {

    int exitCount;
    long currTime, prevTime;
    LoginActivityPresenter<LoginActivity> presenter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {
        presenter = new LoginActivityPresenter<>();
        presenter.onAttach(this);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        presenter.bindDynamicLinkEmailVerification(getIntent());
    }

    @Override
    public void onSuccessfulVerificationAndSignIn() {
        AlertUtils.showAlertBox(LoginActivity.this, "", "Email Address Verified", "Continue", new DialogSimple.AlertDialogListener() {
            @Override
            public void onButtonClicked() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Log.d("##DebugData", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    startActivity(new Intent(LoginActivity.this, FoodMenuDisplayActivity.class));
                } else {
                    Log.d("##DebugData", "User not signed in");
                }
            }
        });
    }

    @Override
    public void onFailedVerificationOrSignIn(Exception e) {
        AlertUtils.showAlertBox(LoginActivity.this, "", e.getMessage(), getString(R.string.ok), new DialogSimple.AlertDialogListener() {
            @Override
            public void onButtonClicked() {

            }
        });
    }

    @Override
    public void setListeners() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exitCount++;
        if (exitCount == 1) {
            makeText("Press back once more to exit");
            prevTime = System.currentTimeMillis();
        }
        if (exitCount == 2) {
            currTime = System.currentTimeMillis();
            if (currTime - prevTime > 2000) {
                makeText("Press back once more to exit");
                prevTime = System.currentTimeMillis();
                exitCount = 1;
            } else {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }
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
                case 0:
                    return new LoginFragment();

                case 1:
                    return new RegisterFragment();

                default:
                    return new LoginFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
