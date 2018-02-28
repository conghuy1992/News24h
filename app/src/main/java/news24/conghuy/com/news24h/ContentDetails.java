package news24.conghuy.com.news24h;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import news24.conghuy.com.news24h.common.AdBlocker;
import news24.conghuy.com.news24h.common.Const;
import news24.conghuy.com.news24h.common.PrefManager;

/**
 * Created by huy on 1/24/16.
 */
public class ContentDetails extends AppCompatActivity {
    private String TAG = "ContentDetails";
    private AdView mAdView;
    private WebView mWebView;
    private PrefManager prefManager;
    private ProgressBar myProgressBar;
    private TextView tv_progress;
    private FrameLayout fr_loading;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentdetails);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
//        }
//        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);

        context = this;
        init();
    }

    void setTitle(String msg) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(msg);
        }
    }

    private ArrayList<String> listAdv;

    void init() {
        Intent intent = getIntent();
        String str = intent.getStringExtra("link");

        listAdv = intent.getStringArrayListExtra(Const.LIST_ADV);
        if (listAdv == null) listAdv = new ArrayList<>();


        prefManager = new PrefManager(this);
        boolean flag = prefManager.isFirstTimeLaunch();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(str);
        }
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        myProgressBar = (ProgressBar) findViewById(R.id.myProgressBar);
        fr_loading = (FrameLayout) findViewById(R.id.fr_loading);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mWebView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = mWebView.getSettings();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setSaveFormData(false);
        } else {
            webSettings.setSaveFormData(false);
            webSettings.setSavePassword(false);
        }

        if (flag) webSettings.setJavaScriptEnabled(true);
        else webSettings.setJavaScriptEnabled(false);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.loadUrl(str);
    }

    public void backApp() {
        mWebView.loadUrl(Const.ABOUT_BLANK);
        finish();
    }

    @Override
    public void onBackPressed() {
        backApp();
    }

    private String _url = "";

    private class MyWebViewClient extends WebViewClient {
        @Override
        public final WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            Log.d(TAG, "shouldInterceptRequest:" + url);
            return Const.isContains(listAdv, url) ? AdBlocker.createEmptyResource() : super.shouldInterceptRequest(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            _url = url;
            setTitle(url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(TAG, "newProgress:" + newProgress);
            myProgressBar.setProgress(newProgress);
            tv_progress.setText("" + newProgress + "%");
            if (!myProgressBar.isShown()) fr_loading.setVisibility(View.VISIBLE);
            if (newProgress == 100) fr_loading.setVisibility(View.GONE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backApp();
                return true;
            case R.id.action_copy:
                copy();
                return true;
            case R.id.action_browser:
                browser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void copy() {
        Const.copyToClipBoard(context, _url);
        Toast.makeText(context,"Copy",Toast.LENGTH_SHORT).show();
    }

    void browser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
        startActivity(browserIntent);
    }
//    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    protected void onPause() {
        if (mAdView != null) mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) mAdView.destroy();
    }
}
