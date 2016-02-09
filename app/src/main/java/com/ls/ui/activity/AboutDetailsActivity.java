package com.ls.ui.activity;

import android.webkit.WebViewClient;
import com.google.android.gms.analytics.GoogleAnalytics;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.UpdatesManager;
import com.ls.utils.AnalyticsManager;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import com.ls.utils.WebviewUtils;

import java.util.List;

public class AboutDetailsActivity extends StateActivity {

    public static final String EXTRA_DETAILS_ID = "EXTRA_DETAILS_ID";
    public static final String EXTRA_DETAILS_CONTENT = "EXTRA_DETAILS_CONTENT";
    public static final String EXTRA_DETAILS_TITLE = "EXTRA_DETAILS_TITLE";

    private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<Integer> requestIds) {
            Log.d("UPDATED", "AboutDetailsActivity");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_about_detail);

        String aboutTitle = getIntent().getStringExtra(EXTRA_DETAILS_TITLE);
        String aboutContent = getIntent().getStringExtra(EXTRA_DETAILS_CONTENT);
        long id = getIntent().getLongExtra(EXTRA_DETAILS_ID, -1);
        initView(aboutContent);
        initToolbar(aboutTitle);
        Model.instance().getUpdatesManager().registerUpdateListener(updateListener);

        AnalyticsManager.sendEvent(this, R.string.about_category, R.string.action_open, id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(String content) {
        WebView webView = (WebView) findViewById(R.id.web_view);

        String css = "<link rel='stylesheet' href='css/style.css' type='text/css'>";
        String html = "<html><header>" + css + "</header>" + "<body>" + content + "</body></html>";
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebviewUtils.openUrl(AboutDetailsActivity.this, url);
                return true;
            }
        });
    }

    private void initToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }
}
