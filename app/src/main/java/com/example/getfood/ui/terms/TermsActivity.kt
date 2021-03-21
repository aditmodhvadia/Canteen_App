package com.example.getfood.ui.terms

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.getfood.R

class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wv = WebView(applicationContext)
        setContentView(wv)
        wv.visibility = View.VISIBLE
        wv.settings.javaScriptEnabled = true
        wv.settings.allowFileAccess = true
        wv.webViewClient = Callback()
        val working = getString(R.string.terms_working_url)
        val directdisplay = getString(R.string.terms_direct_url)
        wv.loadUrl(working)
    }

    private inner class Callback : WebViewClient() {
        override fun shouldOverrideUrlLoading(
                view: WebView, url: String): Boolean {
            return false
        }
    }
}