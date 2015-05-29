package com.ls.drupalconapp.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.ls.drupalconapp.R;
import com.ls.drupalconapp.ui.receiver.DataUpdateManager;
import com.ls.utils.AnalyticsManager;

public class AboutDetailsActivity extends StateActivity {

	public static final String EXTRA_DETAILS_ID = "EXTRA_DETAILS_ID";
	public static final String EXTRA_DETAILS_CONTENT = "EXTRA_DETAILS_CONTENT";
	public static final String EXTRA_DETAILS_TITLE = "EXTRA_DETAILS_TITLE";

	private DataUpdateManager dataUpdateManager = new DataUpdateManager(new DataUpdateManager.DataUpdatedListener() {
		@Override
		public void onDataUpdated(int[] requestIds) {
			Log.d("UPDATED", "AboutDetailsActivity");
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_about_detail);

		String aboutTitle = getIntent().getStringExtra(EXTRA_DETAILS_TITLE);
		String aboutContent = getIntent().getStringExtra(EXTRA_DETAILS_CONTENT);
		long id = getIntent().getLongExtra(EXTRA_DETAILS_ID, -1);
		initView(aboutContent);
		initStatusBar();
		initToolbar(aboutTitle);
		dataUpdateManager.register(this);

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
		dataUpdateManager.unregister(this);
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
	}

	private void initStatusBar() {
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			findViewById(R.id.viewStatusBarTrans).setVisibility(View.VISIBLE);
		}
	}

	private void initToolbar(String title) {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
		if (toolbar != null) {
			toolbar.setTitle(title);
			findViewById(R.id.layoutToolbar).setBackgroundColor(getResources().getColor(R.color.title_color));
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
}
