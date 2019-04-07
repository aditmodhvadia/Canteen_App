package com.example.getfood.ui.splash;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.example.getfood.R;
import com.example.getfood.utils.AlertUtils;
import com.example.getfood.utils.OnDialogButtonClickListener;
import com.example.getfood.ui.base.BaseActivity;
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity;
import com.example.getfood.ui.loginregister.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    private String versionName;
    private DatabaseReference vCheck;
    private SplashPresenter<SplashActivity> presenter;
    private TextView tvVersionName;

    @Override
    public void initViews() {
        presenter = new SplashPresenter<>();
        presenter.onAttach(this);
        tvVersionName = findViewById(R.id.tvVersionName);

//        LoadingDialog loadingDialog = new LoadingDialog();
//        loadingDialog.show

        PackageInfo pInfo;
        vCheck = FirebaseDatabase.getInstance().getReference().child(getString(R.string.version_check));
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;

            tvVersionName.setText(versionName);
//            new ValidateVersionAndUser();
            vCheck.child(versionName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue().toString().equals(getString(R.string.yes))) {
                        Log.d("vcheck", "Inside db check for yes");
                        if (mAuth.getCurrentUser() != null) {
                            startActivity(new Intent(SplashActivity.this, FoodMenuDisplayActivity.class));
                            Log.d("vcheck", "everything green");
                            finish();

                        } else {
                            //start login activity
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    } else {
                        //deprecated versionName of app
                        AlertUtils.openAlertDialog(SplashActivity.this, getString(R.string.warning), getString(R.string.outdated_version_msg),
                                getString(R.string.update), "Exit", new OnDialogButtonClickListener() {
                                    @Override
                                    public void onPositiveButtonClicked() {
                                        String url = getString(R.string.release_url);
                                        try {
                                            Intent i = new Intent(getString(R.string.main_action));
                                            i.setComponent(ComponentName.unflattenFromString(getString(R.string.chrome_intent)));
                                            i.addCategory(getString(R.string.launcher_category));
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                        } catch (ActivityNotFoundException e) {
                                            // Chrome is not installed
                                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            startActivity(i);
                                        }
                                    }

                                    @Override
                                    public void onNegativeButtonClicked() {
                                        finish();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setListeners() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }
}

