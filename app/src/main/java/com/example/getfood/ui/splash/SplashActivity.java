package com.example.getfood.ui.splash;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.getfood.R;
import com.example.getfood.Utils.AlertUtils;
import com.example.getfood.Utils.OnDialogButtonClickListener;
import com.example.getfood.ui.foodmenu.FoodMenuDisplayActivity;
import com.example.getfood.ui.loginregister.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    String version;
    DatabaseReference vCheck;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PackageInfo pInfo;
        vCheck = FirebaseDatabase.getInstance().getReference().child(getString(R.string.version_check));
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
//            new ValidateVersionAndUser();
            vCheck.child(version).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue().toString().equals(getString(R.string.yes))) {
                        Log.d("vcheck", "Inside db check for yes");
                        auth = FirebaseAuth.getInstance();
                        if (auth.getCurrentUser() != null) {
                            startActivity(new Intent(SplashActivity.this, FoodMenuDisplayActivity.class));
                            Log.d("vcheck", "everything green");
                            finish();

                        } else {
                            //start login activity
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    } else {
                        //deprecated version of app
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

    private class ValidateVersionAndUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            vCheck.child(version).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue().toString().equals(getString(R.string.yes))) {
                        Log.d("vcheck", "Inside db check for yes");
                        auth = FirebaseAuth.getInstance();
                        if (auth.getCurrentUser() != null) {
                            startActivity(new Intent(SplashActivity.this, FoodMenuDisplayActivity.class));
                            Log.d("vcheck", "everything green");

                        } else {
                            //start login activity
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                    } else {
                        //deprecated version of app
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

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

}

