package com.ls.drupalconapp.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.UpdatesManager;
import com.ls.drupalconapp.model.data.EventDetailsEvent;
import com.ls.drupalconapp.model.data.Level;
import com.ls.drupalconapp.model.data.Speaker;
import com.ls.drupalconapp.model.managers.EventManager;
import com.ls.drupalconapp.model.managers.FavoriteManager;
import com.ls.drupalconapp.model.managers.SpeakerManager;
import com.ls.drupalconapp.ui.receiver.FavoriteReceiverManager;
import com.ls.drupalconapp.ui.view.CircleDrupalImageView;
import com.ls.drupalconapp.ui.view.NotifyingScrollView;
import com.ls.utils.AnalyticsManager;
import com.ls.utils.DateUtils;
import com.ls.utils.ScheduleManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventDetailsActivity extends StackKeeperActivity {
	public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
	public static final String EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID";
	public static final String EXTRA_DAY = "EXTRA_DAY";
	private static int ANIMATION_DURATION = 250;

	private boolean mIsAddedToSchedule = false;
	private ScheduleManager scheduleManager;
	private long mEventId = -1;
	private EventDetailsEvent mEvent;
	private long mDay;
	private float m_downX;
	private String eventDescription = "";

	private View mViewToolbar;
	private TextView mToolbarTitle;
	private MenuItem mItemShare;

	private NotifyingScrollView mScrollView;
	private ProgressBar mProgressBar;

	private FavoriteReceiverManager favoriteReceiverManager = new FavoriteReceiverManager(
			new FavoriteReceiverManager.FavoriteUpdatedListener() {
				@Override
				public void onFavoriteUpdated(long eventId, boolean isFavorite) {
					mIsAddedToSchedule = isFavorite;
				}
			});

	private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener()
	{
		@Override
		public void onDataUpdated(List<Integer> requestIds) {
			Log.d("UPDATED", "EventDetailsActivity");
			loadEventFromDb();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_event_details);

		int apiVersion = android.os.Build.VERSION.SDK_INT;
		if (apiVersion >= Build.VERSION_CODES.LOLLIPOP) {
			ANIMATION_DURATION = 750;
		}
		favoriteReceiverManager.register(this);
		Model.instance().getUpdatesManager().registerUpdateListener(updateListener);
		scheduleManager = new ScheduleManager(this);

		handleExtras(getIntent());
		initStatusBar();
		initToolbar();
		initView();
		loadEventFromDb();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_share, menu);
		mItemShare = menu.findItem(R.id.actionShare);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.actionShare:
				shareEvent(mEvent.getLink());
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		favoriteReceiverManager.unregister(this);
		Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		long oldEventId = mEventId;

		handleExtras(intent);

		if (oldEventId != mEventId) {
			loadEventFromDb();
		}
	}

	private void initStatusBar() {
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			findViewById(R.id.viewStatusBarTrans).setVisibility(View.VISIBLE);
		}
	}

	private void initToolbar() {
		mToolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
		mToolbarTitle.setAlpha(0f);

		mViewToolbar = findViewById(R.id.viewToolbar);
		mViewToolbar.setAlpha(0f);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
		if (toolbar != null) {
			toolbar.setTitle("");
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public void shareEvent(String url) {
		startActivity(Intent.createChooser(
				createShareIntent(url),
				getString(R.string.title_share)));
	}

	public Intent createShareIntent(String url) {
		String shareSubject = PreferencesManager.getInstance().getMajorInfoTitle();
		ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this)
				.setType("text/plain")
				.setSubject(shareSubject)
				.setText(getString(R.string.body_share) + " " + url);
		return builder.getIntent();
	}


	private void handleExtras(Intent intent) {
		if (intent == null) return;

		mEventId = intent.getLongExtra(EXTRA_EVENT_ID, -1);
		mDay = intent.getLongExtra(EXTRA_DAY, -1);

		AnalyticsManager.sendEvent(this, R.string.event_category, R.string.action_open, mEventId);
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

	private void initView() {
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mScrollView = (NotifyingScrollView) findViewById(R.id.scrollView);
		mScrollView.setAlpha(0);
		mScrollView.setOnScrollChangedListener(onScrollChangedListener);
		mScrollView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mScrollView.scrollTo(0, 0);
			}
		}, 350);
	}

	private void loadEventFromDb() {
		if (mEventId == -1) return;

		new AsyncTask<Void, Void, EventDetailsEvent>() {
			@Override
			protected EventDetailsEvent doInBackground(Void... params) {
				EventManager manager = Model.instance().getEventManager();
				return manager.getEventById(mEventId);
			}

			@Override
			protected void onPostExecute(EventDetailsEvent event) {
				fillEventView(event);
			}
		}.execute();
	}

	private void fillEventView(EventDetailsEvent event) {
		if (event == null) {
			finish();
			return;
		}
		mEvent = event;

		if (mEvent.getLink().equals("") && mItemShare != null) {
			mItemShare.setVisible(false);
		}
		mToolbarTitle.setText(event.getEventName());

//		findViewById(R.id.btnAddToSchedule).setOnTouchListener(getOnSelectorTouchListener());
		((TextView) findViewById(R.id.txtEventName)).setText(event.getEventName());

		String day = formatDate(new Date(mDay));

		String place = "";
		if (!event.getPlace().equals("")) {
			place = " in " + event.getPlace();
		}

		String fromTime = event.getFrom();
		String toTime = event.getTo();

		if (android.text.format.DateFormat.is24HourFormat(this)) {
			if (fromTime != null && toTime != null) {
				fromTime = DateUtils.convertDateTo24Format(fromTime);
				toTime = DateUtils.convertDateTo24Format(toTime);
			}
		}

		String date = day + ", " + fromTime + " - " + toTime + place;
		((TextView) findViewById(R.id.label_where)).setText(date);

		if (TextUtils.isEmpty(event.getTrack()) && TextUtils.isEmpty(event.getLevel())) {
			findViewById(R.id.layout_level_track).setVisibility(View.GONE);
		} else {
			if (TextUtils.isEmpty(event.getTrack())) {
				findViewById(R.id.txtTrack).setVisibility(View.GONE);
			} else {
				((TextView) findViewById(R.id.txtTrack)).setText(event.getTrack());
			}

			if (TextUtils.isEmpty(event.getLevel())) {
				findViewById(R.id.layoutExpLevel).setVisibility(View.GONE);
			} else {
				((TextView) findViewById(R.id.txtExpLevel)).setText(getString(R.string.exp_level) + " " + event.getLevel());
				int imageId = Level.getIcon(event.getLevelId());
				((ImageView) findViewById(R.id.imgExperience)).setImageResource(imageId);
			}
		}

		if (!eventDescription.equals(event.getDescription())) {
			eventDescription = event.getDescription();

			String css = "<link rel='stylesheet' href='css/style.css' type='text/css'>";
			String html = "<html><header>" + css + "</header>" + "<body>" + eventDescription + "</body></html>";
			WebView webView = (WebView) findViewById(R.id.webView);
			webView.setHorizontalScrollBarEnabled(false);
			webView.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {

					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN: {
							m_downX = event.getX();
						}
						break;

						case MotionEvent.ACTION_MOVE:
						case MotionEvent.ACTION_CANCEL:
						case MotionEvent.ACTION_UP: {
							event.setLocation(m_downX, event.getY());
						}
						break;

					}

					return false;
				}
			});
			webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
		}
		mIsAddedToSchedule = event.isFavorite();
		final CheckBox checkBoxFavorite = (CheckBox) findViewById(R.id.checkBoxFavorite);
		checkBoxFavorite.setChecked(mIsAddedToSchedule);

		RelativeLayout layoutFavorite = (RelativeLayout) findViewById(R.id.layoutFavorite);
		layoutFavorite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBoxFavorite.setChecked(!checkBoxFavorite.isChecked());
				mIsAddedToSchedule = checkBoxFavorite.isChecked();
				setFavorite();
			}
		});

		addSpeakers(event.getDescription());
	}

	private void addSpeakers(final String description) {
		new AsyncTask<Void, Void, List<Speaker>>() {
			@Override
			protected List<Speaker> doInBackground(Void... params) {
				SpeakerManager manager = Model.instance().getSpeakerManager();
				return manager.getSpeakersByEventId(mEventId);
			}

			@Override
			protected void onPostExecute(List<Speaker> speakers) {
				mProgressBar.setVisibility(View.GONE);
				mScrollView.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).start();

				LayoutInflater inflater = LayoutInflater.from(EventDetailsActivity.this);
				LinearLayout holderSpeakers = (LinearLayout) findViewById(R.id.holderSpeakers);
				holderSpeakers.removeAllViews();
				for (Speaker speaker : speakers) {
					View speakerView = inflater.inflate(R.layout.item_speaker_no_letter, null);
					fillSpeakerView(speaker, speakerView);
					holderSpeakers.addView(speakerView);
				}

				if (speakers.size() == 0 && TextUtils.isEmpty(description)) {
					findViewById(R.id.imgEmptyView).setVisibility(View.VISIBLE);
				}

				if (speakers.size() == 0) {
					findViewById(R.id.generalDivider).setVisibility(View.VISIBLE);
					findViewById(R.id.topDivider).setVisibility(View.GONE);
					findViewById(R.id.bottomDivider).setVisibility(View.GONE);
				} else {
					findViewById(R.id.generalDivider).setVisibility(View.GONE);
					findViewById(R.id.topDivider).setVisibility(View.VISIBLE);
					findViewById(R.id.bottomDivider).setVisibility(View.VISIBLE);
				}

				findViewById(R.id.scrollView).scrollTo(0, 0);
			}
		}.execute();
	}

	private void fillSpeakerView(final Speaker speaker, View speakerView) {
		// Speaker image
		CircleDrupalImageView imgPhoto = (CircleDrupalImageView) speakerView.findViewById(R.id.imgPhoto);
		String imageUrl = speaker.getAvatarImageUrl();
		imgPhoto.setImageWithURL(imageUrl);

		//Speaker name
		((TextView) speakerView.findViewById(R.id.txtName)).setText(speaker.getFirstName() + " " + speaker.getLastName());

		//Speaker organization & job title
		String organization = speaker.getOrganization();
		String jobTitle = speaker.getJobTitle();
		String space = (TextUtils.isEmpty(organization) && TextUtils.isEmpty(jobTitle))
				|| TextUtils.isEmpty(organization)
				|| TextUtils.isEmpty(jobTitle)
				? "" : " / ";

		((TextView) speakerView.findViewById(R.id.txtOrgAndJobTitle)).setText(organization + space + jobTitle);

		speakerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(EventDetailsActivity.this, SpeakerDetailsActivity.class);
				intent.putExtra(SpeakerDetailsActivity.EXTRA_SPEAKER_ID, speaker.getId());
				intent.putExtra(SpeakerDetailsActivity.EXTRA_SPEAKER, speaker);
				startActivity(intent);
			}
		});
	}

	private String formatDate(Date date) {
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat("EEE");
			return format.format(date).toUpperCase();
		} else {
			return null;
		}
	}

	private void setFavorite() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				FavoriteManager manager = new FavoriteManager();
				manager.setFavoriteEvent(mEventId, mIsAddedToSchedule);
				return null;
			}
		}.execute();

		int actionId;

		setToNotificationQueue();
		if (mIsAddedToSchedule) {
			actionId = R.string.action_add_to_favorites;
		} else {
			actionId = R.string.action_remove_from_favorites;
		}

		AnalyticsManager.sendEvent(this, R.string.event_category, actionId, mEventId);
		FavoriteReceiverManager.updateFavorites(EventDetailsActivity.this, mEventId,
				mIsAddedToSchedule);
	}

	private void setToNotificationQueue() {
		String eventFromTime = mEvent.getFrom();
//		String hours = eventFromTime.split(":")[0];
//		String minutes = eventFromTime.split(":")[1];
//		Date scheduleDate = DateUtils.convertDate(eventFromTime);

		Date date = new Date(mDay);
		Date scheduleTime = DateUtils.convertDate(eventFromTime);

		if (mIsAddedToSchedule) {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if (scheduleTime != null) {
				calendar.set(Calendar.HOUR_OF_DAY, scheduleTime.getHours());
				calendar.set(Calendar.MINUTE, scheduleTime.getMinutes());
				calendar.set(Calendar.SECOND, 0);
			}

			long systemDate = System.currentTimeMillis();
			long scheduleDate = calendar.getTimeInMillis();

			if (scheduleDate > systemDate) {
				scheduleManager.setAlarmForNotification(calendar, mEvent, mDay);
			}

		} else {
			scheduleManager.cancelAlarm(mEventId);
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
			mToolbarTitle.setAlpha(headerRatio);
			mViewToolbar.setAlpha(headerRatio);
		}
	};
}
