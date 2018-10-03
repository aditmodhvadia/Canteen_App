package com.example.getfood.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.getfood.Fragment.RegisterFragment;
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
        String working = "https://www.google.com/a/nirmauni.ac.in/ServiceLogin?service=wise&passive=1209600&continue=https://docs.google.com/a/nirmauni.ac.in/document/d/e/2PACX-1vSs_x9OacQFiTHHp0PCLfVaQXvYCmu703Vedff1zdLDkqfNm--RiOXEBBle-H_aEDLmgrFMLfNYayPx/pub?embedded%3Dtrue&followup=https://docs.google.com/a/nirmauni.ac.in/document/d/e/2PACX-1vSs_x9OacQFiTHHp0PCLfVaQXvYCmu703Vedff1zdLDkqfNm--RiOXEBBle-H_aEDLmgrFMLfNYayPx/pub?embedded%3Dtrue&ltmpl=docs";

        String directdisplay = "https://docs.google.com/a/nirmauni.ac.in/document/d/e/2PACX-1vSs_x9OacQFiTHHp0PCLfVaQXvYCmu703Vedff1zdLDkqfNm--RiOXEBBle-H_aEDLmgrFMLfNYayPx/pub";
        wv.loadUrl(working);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
    }
}
