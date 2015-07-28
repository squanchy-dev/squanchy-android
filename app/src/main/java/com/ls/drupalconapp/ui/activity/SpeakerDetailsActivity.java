package com.ls.drupalconapp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.ls.drupalconapp.R;
import com.ls.drupalconapp.app.App;
import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.UpdatesManager;
import com.ls.drupalconapp.model.dao.EventDao;
import com.ls.drupalconapp.model.data.Speaker;
import com.ls.drupalconapp.model.data.SpeakerDetailsEvent;
import com.ls.drupalconapp.model.managers.SpeakerManager;
import com.ls.drupalconapp.ui.view.CircleImageView;
import com.ls.drupalconapp.ui.view.NotifyingScrollView;
import com.ls.utils.AnalyticsManager;
import com.ls.utils.DateUtils;
import com.ls.utils.UIUtils;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

import java.util.List;

public class SpeakerDetailsActivity extends StackKeeperActivity
		implements ParallaxScrollView.OnParallaxViewScrolledListener, View.OnClickListener {

	public static final String EXTRA_SPEAKER = "EXTRA_SPEAKER";
	public static final String EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID";

	private static final String TWITTER_URL = "https://twitter.com/";
	private static final String TWITTER_APP_URL = "twitter://user?screen_name=";

	private View inspectView;
	private int mActionBarBottomCoordinate = -1;
	private Speaker mSpeaker;
	private long mSpeakerId = -1;

	private Toolbar mToolbar;
	private String mSpeakerName;

	//ActionBar flags
	private boolean isTransparentBbSet = true;
	private boolean isTitleBgSet = false;
	private SpeakerManager mSpeakerManager;

	private View mViewToolbar;
	private TextView mTitle;
	private NotifyingScrollView mScrollView;

	private boolean isWebViewLoaded;
	private boolean isDataLoaded;

	private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener() {
		@Override
		public void onDataUpdated(List<Integer> requestIds) {
			Log.d("UPDATED", "SpeakerDetailsActivity");
			loadSpeakerFromDb();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_speaker_details);

		mSpeakerManager = Model.instance().getSpeakerManager();
		handleExtras(getIntent());
		initToolbar();
		initView();
		loadSpeakerFromDb();
		Model.instance().getUpdatesManager().registerUpdateListener(updateListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		long oldSpeakerId = mSpeaker.getId();

		handleExtras(intent);

		if (oldSpeakerId != mSpeaker.getId()) {
			fillView(mSpeaker);
		}
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
	public void onScrolled(int horPos, int verPos, int oldHorPos, int oldVerPos) {
		handleScrolling();
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

	private void initStatusBar() {
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setStatusBarColor(getResources().getColor(R.color.event));
		}
	}

	private void initToolbar() {
		mViewToolbar = findViewById(R.id.viewToolbar);
		mViewToolbar.setAlpha(0);

		mTitle = (TextView) findViewById(R.id.toolbarTitle);
		mTitle.setText(mSpeakerName);
		mTitle.setAlpha(0);

		mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolBar);
		if (mToolbar != null) {
			mToolbar.setTitle("");
			setSupportActionBar(mToolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		initStatusBar();
	}

	private void handleExtras(Intent intent) {
		if (intent == null) return;

		mSpeaker = intent.getParcelableExtra(EXTRA_SPEAKER);
		mSpeakerId = intent.getLongExtra(EXTRA_SPEAKER_ID, -1);
		if (mSpeaker != null) {
			mSpeakerName = String.format("%s %s", mSpeaker.getFirstName(), mSpeaker.getLastName());
		}

		AnalyticsManager.sendEvent(this, R.string.speaker_category, R.string.action_open, mSpeakerId);
	}

	private void initView() {
		mScrollView = (NotifyingScrollView) findViewById(R.id.scrollView);
		mScrollView.setOnScrollChangedListener(onScrollChangedListener);
		mScrollView.setAlpha(0);

		mActionBarBottomCoordinate = (int) getResources().getDimension(R.dimen.action_bar_height) +
				(int) UIUtils.dipToPixels(SpeakerDetailsActivity.this, 24); // + height of top bar
	}

	private void loadSpeakerFromDb() {
		if (mSpeakerId == -1) return;

		new AsyncTask<Void, Void, Speaker>() {
			@Override
			protected Speaker doInBackground(Void... params) {
				return mSpeakerManager.getSpeakerById(mSpeakerId);
			}

			@Override
			protected void onPostExecute(Speaker speaker) {
				fillView(speaker);
			}
		}.execute();
	}

	private void fillView(Speaker speaker) {
		if (speaker == null) {
			finish();
			return;
		}
		mSpeaker = speaker;

		// Speaker image
		CircleImageView imgPhoto = (CircleImageView) findViewById(R.id.imgPhoto);
		String imageUrl = mSpeaker.getAvatarImageUrl();
		imgPhoto.setImageWithURL(imageUrl);

		// Speaker name
		String speakerName = TextUtils.isEmpty(mSpeaker.getFirstName()) ? "" : mSpeaker.getFirstName() + " ";
		speakerName += TextUtils.isEmpty(mSpeaker.getLastName()) ? "" : mSpeaker.getLastName();
		((TextView) findViewById(R.id.txtSpeakerName)).setText(speakerName);
//        ((TextView) findViewById(R.id.txtScreenTitle)).setText(speakerName);

		if (TextUtils.isEmpty(mSpeaker.getJobTitle()) && TextUtils.isEmpty(mSpeaker.getOrganization())) {
			findViewById(R.id.txtSpeakerPosition).setVisibility(View.GONE);
			inspectView = findViewById(R.id.holderButtons);
		} else {
			findViewById(R.id.txtSpeakerPosition).setVisibility(View.VISIBLE);
			inspectView = findViewById(R.id.txtSpeakerPosition);
		}

		// Speaker job title
		if (!TextUtils.isEmpty(mSpeaker.getJobTitle())) {
			((TextView) findViewById(R.id.txtSpeakerPosition)).setText(mSpeaker.getJobTitle());
		}

		// Speaker organization
		if (!TextUtils.isEmpty(mSpeaker.getOrganization())) {
			TextView jobTxt = (TextView) findViewById(R.id.txtSpeakerPosition);
			String text = jobTxt.getText().toString() + " at " + mSpeaker.getOrganization();
			jobTxt.setText(text);
		}

		// Link to Twitter
		if (TextUtils.isEmpty(mSpeaker.getTwitterName())) {
			findViewById(R.id.holderBtnTwitter).setVisibility(View.GONE);
		} else {
			findViewById(R.id.btnTwitter).setOnClickListener(this);
		}

		// Link to Website
		if (TextUtils.isEmpty(mSpeaker.getWebSite())) {
			findViewById(R.id.holderBtnWebsite).setVisibility(View.GONE);
		} else {
			findViewById(R.id.btnWebsite).setOnClickListener(this);
		}

		// Speaker details
		WebView webView = (WebView) findViewById(R.id.webView);
		if (!TextUtils.isEmpty(speaker.getCharact())) {
			webView.setVisibility(View.VISIBLE);
			webView.loadUrl("about:blank");
			String css = "<link rel='stylesheet' href='css/style.css' type='text/css'>";
			String html = "<html><header>" + css + "</header>" + "<body>" + mSpeaker.getCharact() + "</body></html>";
			webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
			webView.setWebViewClient(new WebViewClient() {
				public void onPageFinished(WebView view, String url) {
					isWebViewLoaded = true;
					checkForLoadingComplete();
				}
			});
		} else {
			isWebViewLoaded = true;
		}
		// Events related to the speaker
		LinearLayout layoutEvents = (LinearLayout) findViewById(R.id.layoutEvents);
		addEventsToContainer(layoutEvents, mSpeaker);
		mScrollView.fullScroll(ScrollView.FOCUS_UP);

		isDataLoaded = true;
		checkForLoadingComplete();
	}

	private void addEventsToContainer(final LinearLayout layoutEvents, final Speaker speaker) {
		new AsyncTask<Void, Void, List<SpeakerDetailsEvent>>() {
			@Override
			protected List<SpeakerDetailsEvent> doInBackground(Void... params) {
				EventDao eventDao = new EventDao(App.getContext());
				return eventDao.getEventsBySpeakerId(speaker.getId());
			}

			@Override
			protected void onPostExecute(List<SpeakerDetailsEvent> events) {
				findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
				LayoutInflater inflater = LayoutInflater.from(SpeakerDetailsActivity.this);
				layoutEvents.removeAllViews();
				for (SpeakerDetailsEvent event : events) {
					View eventView = inflater.inflate(R.layout.item_speakers_event, null);
					fillEventView(event, eventView);
					layoutEvents.addView(eventView);
				}
			}
		}.execute();
	}

	private void fillEventView(final SpeakerDetailsEvent event, View eventView) {
		((TextView) eventView.findViewById(R.id.txtArticleName)).setText(event.getEventName());
		TextView txtTrack = (TextView) eventView.findViewById(R.id.txtTrack);
		if (!TextUtils.isEmpty(event.getTrackName())) {
			txtTrack.setText(event.getTrackName());
			txtTrack.setVisibility(View.VISIBLE);
		}

		String fromTime = event.getFrom();
		String toTime = event.getTo();

		if (android.text.format.DateFormat.is24HourFormat(this)) {
			if (fromTime != null && toTime != null) {
				fromTime = DateUtils.convertDateTo24Format(fromTime);
				toTime = DateUtils.convertDateTo24Format(toTime);
			}
		}

		TextView txtWhere = (TextView) eventView.findViewById(R.id.txtWhere);
		String date = String.format("%s, %s - %s", event.getDate(), fromTime, toTime);
		txtWhere.setText(date);
		if (!event.getPlace().equals("")) {
			txtWhere.append(String.format(" in %s", event.getPlace()));
		}

		initEventExpLevel(eventView, event);
		eventView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				EventDetailsActivity.startThisActivity(SpeakerDetailsActivity.this, event.getEventId(), DateUtils.convertWeekDayToLong(event.getDate()));
			}
		});
	}

	private void sendEventDetailsBroadcast() {
		Intent intent = new Intent();
		intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
		sendBroadcast(intent);
	}

	private void initEventExpLevel(View eventView, SpeakerDetailsEvent event) {
		TextView txtExpLevel = (TextView) eventView.findViewById(R.id.txtExpLevel);
		ImageView imgExpLevel = (ImageView) eventView.findViewById(R.id.imgExpLevel);

		String expLevel = event.getLevelName();
		if (!TextUtils.isEmpty(expLevel)) {
			String expText = String.format("%s %s", getResources().getString(R.string.experience_level), expLevel);
			txtExpLevel.setText(expText);
			txtExpLevel.setVisibility(View.VISIBLE);

			switch (expLevel) {
				case "Beginner":
					imgExpLevel.setImageResource(R.drawable.ic_experience_beginner);
					imgExpLevel.setVisibility(View.VISIBLE);
					break;
				case "Intermediate":
					imgExpLevel.setImageResource(R.drawable.ic_experience_intermediate);
					imgExpLevel.setVisibility(View.VISIBLE);
					break;
				case "Advanced":
					imgExpLevel.setImageResource(R.drawable.ic_experience_advanced);
					imgExpLevel.setVisibility(View.VISIBLE);
					break;
			}
		}
	}

	private void handleScrolling() {
		int locations[] = new int[2];
		inspectView.getLocationOnScreen(locations);
		int verCoordinate = locations[1];

		if (mActionBarBottomCoordinate > verCoordinate && !isTitleBgSet) {
			mToolbar.setBackgroundColor(getResources().getColor(R.color.primary));
			isTitleBgSet = true;
			isTransparentBbSet = false;

			mToolbar.setTitle(mSpeakerName);
		} else if (mActionBarBottomCoordinate < verCoordinate && !isTransparentBbSet) {
			mToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			isTitleBgSet = false;
			isTransparentBbSet = true;

			mToolbar.setTitle("");
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnTwitter:
				try {
					String url = TWITTER_APP_URL + mSpeaker.getTwitterName();
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
				} catch (Exception e) {
					String url = TWITTER_URL + mSpeaker.getTwitterName();
					openBrowser(url);
				}
				break;
			case R.id.btnWebsite:
				String url = mSpeaker.getWebSite();
				openBrowser(url);
				break;
		}
	}

	private NotifyingScrollView.OnScrollChangedListener onScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {

		@Override
		public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
			int headerHeight = findViewById(R.id.imgHeader).getHeight();
			float headerRatio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;

			fadeActionBar(headerRatio);
		}

		private void fadeActionBar(float headerRatio) {
			mTitle.setAlpha(headerRatio);
			mViewToolbar.setAlpha(headerRatio);
		}
	};

	private void openBrowser(String url) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.no_apps_can_perform_this_action), Toast.LENGTH_SHORT).show();
		}
	}

	private void checkForLoadingComplete() {
		if (isDataLoaded && isWebViewLoaded) {
			findViewById(R.id.progressBar).setVisibility(View.GONE);
			mScrollView.setAlpha(1.0f);
		}
	}
}
