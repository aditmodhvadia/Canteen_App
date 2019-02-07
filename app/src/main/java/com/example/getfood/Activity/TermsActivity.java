package com.example.getfood.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.getfood.R;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView wv = new WebView(getApplicationContext());
        setContentView(wv);

        wv.setVisibility(View.VISIBLE);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.setWebViewClient(new TermsActivity.Callback());
        String working = getString(R.string.terms_working_url);

        String directdisplay = getString(R.string.terms_direct_url);
        wv.loadUrl(working);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }
}
