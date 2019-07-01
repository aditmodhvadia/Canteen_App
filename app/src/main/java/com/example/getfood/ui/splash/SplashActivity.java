package com.example.getfood.ui.splash;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.ui.base.BaseActivity;
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity;
import com.example.getfood.ui.loginregister.LoginActivity;
import com.example.getfood.utils.alert.AlertUtils;
import com.example.getfood.utils.alert.DialogConfirmation;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    private SplashPresenter<SplashActivity> presenter;

    @Override
    public void initViews() {
        presenter = new SplashPresenter<>();
        presenter.onAttach(this);
        TextView tvVersionName = findViewById(R.id.tvVersionName);

        String versionName = presenter.getVersionName();

        tvVersionName.setText(versionName);

        presenter.determineIfUpdateNeeded(versionName);
    }

    @Override
    public void updateNotRequired() {
        presenter.determineIfUserLoggedIn();
    }

    @Override
    public void updateRequired() {
        AlertUtils.showConfirmationDialog(SplashActivity.this, getString(R.string.warning), getString(R.string.outdated_version_msg),
                getString(R.string.update), "Exit", new DialogConfirmation.ConfirmationDialogListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        String url = getString(R.string.release_url);
                        try {
                            Intent i = new Intent(getString(R.string.main_action));
                            i.setComponent(ComponentName.unflattenFromString(getString(R.string.chrome_intent)));
                            i.addCategory(getString(R.string.launcher_category));
                            i.setData(Uri.parse(url));
                            startActivity(i);
                            finish();
                        } catch (ActivityNotFoundException e) {
                            // Chrome is not installed
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        finish();
                    }
                });
    }

    @Override
    public void userIsSignedIn() {
        startActivity(new Intent(SplashActivity.this, FoodMenuDisplayActivity.class));
        Log.d("vcheck", "everything green");
        finish();
    }

    @Override
    public void userIsNotSignedIn() {
        //start login activity
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }
}

