package com.example.getfood.ui.loginregister;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.getfood.R;
import com.example.getfood.ui.base.BaseActivity;
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity;
import com.example.getfood.ui.loginregister.loginfragment.LoginFragment;
import com.example.getfood.ui.loginregister.registerfragment.RegisterFragment;
import com.example.getfood.utils.alert.AlertUtils;
import com.example.getfood.utils.alert.DialogSimple;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import timber.log.Timber;

public class LoginActivity extends BaseActivity implements LoginActivityMvpView {

    private int exitCount;
    private long currTime, prevTime;
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
                if (presenter.isUserLoggedIn()) {
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Timber.d(task.getException(), "getInstanceId failed");
                                        return;
                                    }
                                    // Get new Instance ID token
                                    String token = task.getResult().getToken();
                                    presenter.updateToken(token);
                                    // Log and toast
                                    Timber.d(token);
                                }
                            });
                } else {
                    Timber.d("User not signed in");
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
    public void onTokenUpdatedSuccessfully() {
        startActivity(new Intent(LoginActivity.this, FoodMenuDisplayActivity.class));
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
            makeShortText("Press back once more to exit");
            prevTime = System.currentTimeMillis();
        }
        if (exitCount == 2) {
            currTime = System.currentTimeMillis();
            if (currTime - prevTime > 2000) {
                makeShortText("Press back once more to exit");
                prevTime = System.currentTimeMillis();
                exitCount = 1;
            } else {
                presenter.signOutUser();
                finish();
            }
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
            if (position == 1) {
                return new RegisterFragment();
            }
            return new LoginFragment();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
