package net.squanchy.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import net.squanchy.R;
import net.squanchy.analytics.Analytics;
import net.squanchy.utils.WebviewUtils;

public class AboutDetailsActivity extends StateActivity {

    public static final String EXTRA_DETAILS_ID = "EXTRA_DETAILS_ID";
    public static final String EXTRA_DETAILS_CONTENT = "EXTRA_DETAILS_CONTENT";
    public static final String EXTRA_DETAILS_TITLE = "EXTRA_DETAILS_TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_about_detail);

        String aboutTitle = getIntent().getStringExtra(EXTRA_DETAILS_TITLE);
        String aboutContent = getIntent().getStringExtra(EXTRA_DETAILS_CONTENT);
        long id = getIntent().getLongExtra(EXTRA_DETAILS_ID, -1);

        initView(aboutContent);
        initToolbar(aboutTitle);

        Analytics.from(this)
                .trackEvent(
                        getString(R.string.about_category), getString(R.string.action_open), id + " " + aboutTitle
                );
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
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        View layoutPlaceholder = findViewById(R.id.layout_placeholder);
        WebView webView = (WebView) findViewById(R.id.web_view);

        if (content == null || content.isEmpty()) {
            scrollView.setVisibility(View.GONE);
            layoutPlaceholder.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.VISIBLE);
            layoutPlaceholder.setVisibility(View.GONE);

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
    }

    private void initToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }
}
