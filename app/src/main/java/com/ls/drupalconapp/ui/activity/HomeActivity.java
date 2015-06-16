package com.ls.drupalconapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.ls.drupalconapp.R;
import com.ls.drupalconapp.app.App;
import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.data.Level;
import com.ls.drupalconapp.model.data.Track;
import com.ls.drupalconapp.model.managers.TracksManager;
import com.ls.drupalconapp.ui.adapter.item.EventListItem;
import com.ls.drupalconapp.ui.dialog.FilterDialog;
import com.ls.drupalconapp.ui.drawer.DrawerAdapter;
import com.ls.drupalconapp.ui.drawer.DrawerManager;
import com.ls.drupalconapp.ui.drawer.DrawerMenu;
import com.ls.drupalconapp.ui.drawer.DrawerMenuItem;
import com.ls.utils.AnalyticsManager;
import com.ls.utils.KeyboardUtils;
import com.ls.utils.ScheduleManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends StateActivity implements FilterDialog.OnCheckedPositionsPass {

	private DrawerManager mFrManager;
	private DrawerAdapter mAdapter;
	private String mPresentTitle;
	private int mSelectedItem = 0;
	private int mLastSelectedItem = 0;
	private boolean isIntentHandled = false;

	private View mStatusBar;
	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;

	public FilterDialog mFilterDialog;
	public boolean mIsDrawerItemClicked;

	public static void startThisActivity(Activity activity) {
		Intent intent = new Intent(activity, HomeActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_main);

		initStatusBar();
		initToolbar();
		initNavigationDrawer();
		initNavigationDrawerList();
		initFilterDialog();

		initFragmentManager();
		if (getIntent().getExtras() != null) {
			isIntentHandled = true;
		}
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(null);
		if (!isIntentHandled) {
			handleIntent(intent);
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
	protected void onDestroy() {
		super.onDestroy();
		AnalyticsManager.sendEvent(this, "Application", R.string.action_close);
	}

	private void initStatusBar() {
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			mStatusBar = findViewById(R.id.viewStatusBar);
			mStatusBar.setBackgroundColor(getResources().getColor(R.color.primary));
			mStatusBar.setVisibility(View.VISIBLE);
			findViewById(R.id.viewStatusBarTrans).setVisibility(View.VISIBLE);
		}
	}

	private void initToolbar() {
		mPresentTitle = DrawerMenu.MENU_STRING_ARRAY[0];
		mToolbar = (Toolbar) findViewById(R.id.toolBar);
		if (mToolbar != null) {
			mToolbar.setBackgroundColor(getResources().getColor(R.color.primary));
			mToolbar.setTitle(mPresentTitle);
			setSupportActionBar(mToolbar);
		}
	}

	private void initNavigationDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);

		mDrawerLayout.setDrawerListener(drawerToggle);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerLayout.closeDrawers();
		mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				KeyboardUtils.hideKeyboard(getCurrentFocus());
			}

			@Override
			public void onDrawerOpened(View drawerView) {
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				if (mIsDrawerItemClicked) {
					mIsDrawerItemClicked = false;
					changeFragment();
				}
			}

			@Override
			public void onDrawerStateChanged(int newState) {
			}
		});

		drawerToggle.syncState();
	}

	private void initNavigationDrawerList() {
		List<DrawerMenuItem> menu = DrawerMenu.getNavigationDrawerItems();
		mAdapter = new DrawerAdapter(this, menu);
		mAdapter.setDrawerItemClickListener(new DrawerAdapter.OnDrawerItemClickListener() {
			@Override
			public void onDrawerItemClicked(int position) {
				onItemClick(position);
			}
		});

		ListView listView = (ListView) findViewById(R.id.leftDrawer);
		listView.addHeaderView(
				getLayoutInflater().inflate(R.layout.nav_drawer_header, null),
				null,
				false);
		listView.setAdapter(mAdapter);
	}

	public void initFilterDialog() {
		new AsyncTask<Void, Void, List<EventListItem>>() {
			@Override
			protected List<EventListItem> doInBackground(Void... params) {
				TracksManager tracksManager = Model.instance().getTracksManager();
				List<Track> trackList = tracksManager.getTracks();
				List<Level> levelList = tracksManager.getLevels();

				Collections.sort(trackList, new Comparator<Track>() {
					@Override
					public int compare(Track track1, Track track2) {
						String name1 = track1.getName();
						String name2 = track2.getName();
						return name1.compareToIgnoreCase(name2);
					}
				});

				String[] tracks = new String[trackList.size()];
				String[] levels = new String[levelList.size()];

				for (int i = 0; i < trackList.size(); i++) {
					tracks[i] = trackList.get(i).getName();
				}

				for (int i = 0; i < levelList.size(); i++) {
					levels[i] = levelList.get(i).getName();
				}
				mFilterDialog = FilterDialog.newInstance(tracks, levels);
				mFilterDialog.setData(levelList, trackList);
				return null;
			}

			@Override
			protected void onPostExecute(List<EventListItem> eventListItems) {
			}
		}.execute();
	}

	private void handleIntent(Intent intent) {
		if (intent.getExtras() != null) {
			long eventId = intent.getLongExtra(EventDetailsActivity.EXTRA_EVENT_ID, -1);
			long day = intent.getLongExtra(EventDetailsActivity.EXTRA_DAY, -1);
			redirectToDetails(eventId, day);
			isIntentHandled = false;
			new ScheduleManager(this).cancelAlarm(eventId);
		}
	}

	private void redirectToDetails(long id, long day) {
		Intent intent = new Intent(this, EventDetailsActivity.class);
		intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, id);
		intent.putExtra(EventDetailsActivity.EXTRA_DAY, day);
		startActivity(intent);
	}

	private void onItemClick(int position) {
		mDrawerLayout.closeDrawers();
		if (mSelectedItem == position) {
			return;
		}
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		mSelectedItem = position;
		mIsDrawerItemClicked = true;
	}

	private void changeFragment() {
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

		if (mSelectedItem == DrawerManager.EventMode.About.ordinal()) {
			AboutActivity.startThisActivity(this);
			mSelectedItem = mLastSelectedItem;
		} else {
			DrawerMenuItem item = mAdapter.getItem(mSelectedItem);
			if (!item.isGroup() && mFrManager != null) {
				mFrManager.setFragment(DrawerManager.EventMode.values()[mSelectedItem]);
				mPresentTitle = DrawerMenu.MENU_STRING_ARRAY[mSelectedItem];
				mToolbar.setTitle(mPresentTitle);

				mAdapter.setSelectedPos(mSelectedItem);
				mAdapter.notifyDataSetChanged();

				if (mSelectedItem == DrawerManager.EventMode.Location.ordinal()) {
					displayLocationTheme();
				} else {
					displayDefaultTheme();
				}

				AnalyticsManager.sendEvent(this, mPresentTitle + " screen", R.string.action_open);
			}
		}
		mLastSelectedItem = mSelectedItem;
	}

	private void initFragmentManager() {
		mFrManager = DrawerManager.getInstance(getSupportFragmentManager(), R.id.mainFragment);
		AnalyticsManager.sendEvent(this, App.getContext().getString(R.string.Schedule) + " screen", R.string.action_open);
		mFrManager.setFragment(DrawerManager.EventMode.Program);
	}

	private void displayDefaultTheme() {
        if (mStatusBar != null && mToolbar != null) {
            mStatusBar.setBackgroundColor(getResources().getColor(R.color.primary));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.primary));
        }
    }

    private void displayLocationTheme() {
        if (mStatusBar != null) {
            mStatusBar.setBackgroundColor(getResources().getColor(R.color.secondary));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.secondary));
        }
    }

	@Override
	public void onNewFilterApplied() {
		mFrManager.reloadPrograms();
	}
}