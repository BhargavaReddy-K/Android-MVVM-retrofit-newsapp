package com.company.demoapp.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.company.demoapp.R;

import java.util.Objects;


public class WebViewActivity extends AppCompatActivity {

    private  WebView webView;
    private ProgressBar progressBar;
    private WebSettings webSettings;
    private WebViewClientNews webViewClient;
    private String URL = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        try {
            //get data from intent
            URL = getIntent().getStringExtra("url");

            //header declaration
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(URL);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //views
            webView = findViewById(R.id.web_viewNews);
            progressBar = findViewById(R.id.progressBar);

            //ojb declare
            webSettings = webView.getSettings();

            //web view settings
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(true);

            //web client
            webViewClient = new WebViewClientNews();
            webView.setWebViewClient(webViewClient);

            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl(URL);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class WebViewClientNews extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.GONE);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

    }


}
