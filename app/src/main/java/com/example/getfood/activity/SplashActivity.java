package com.example.getfood.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.getfood.R;
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

        PackageInfo pInfo = null;
        vCheck = FirebaseDatabase.getInstance().getReference().child("version-check");
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
//            new ValidateVersionAndUser();
            vCheck.child(version).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue().toString().equals("Yes")) {
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
                    }
                    else{
                        //deprecated version of app
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

                        builder.setMessage("This version of the app is outdated now. Update to the latest version.")
                                .setTitle("Warning!");
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(LoginActivity.this, "Update", Toast.LENGTH_SHORT).show();
                                String url = "https://github.com/aditmodhvadia/Canteen_App/releases";
                                try {
                                    Intent i = new Intent("android.intent.action.MAIN");
                                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                                    i.addCategory("android.intent.category.LAUNCHER");
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                                catch(ActivityNotFoundException e) {
                                    // Chrome is not installed
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(i);
                                }
                            }
                        });
                        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        nbutton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    if (dataSnapshot.getValue().toString().equals("Yes")) {
                        Log.d("vcheck", "Inside db check for yes");
                        auth = FirebaseAuth.getInstance();
                        if (auth.getCurrentUser() != null) {
                            startActivity(new Intent(SplashActivity.this, FoodMenuDisplayActivity.class));
                            Log.d("vcheck", "everything green");

                        } else {
                            //start login activity
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                    }
                    else{
                        //deprecated version of app
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

                        builder.setMessage("This version of the app is outdated now. Update to the latest version.")
                                .setTitle("Warning!");
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(LoginActivity.this, "Update", Toast.LENGTH_SHORT).show();
                                String url = "https://github.com/aditmodhvadia/Canteen_App/releases";
                                try {
                                    Intent i = new Intent("android.intent.action.MAIN");
                                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                                    i.addCategory("android.intent.category.LAUNCHER");
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                                catch(ActivityNotFoundException e) {
                                    // Chrome is not installed
                                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(i);
                                }
                            }
                        });
                        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        nbutton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton.setTextColor(getResources().getColor(R.color.colorPrimary));
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

