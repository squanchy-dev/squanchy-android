package com.ls.drupalconapp.ui.fragment;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.Model;
import com.ls.drupalconapp.model.PreferencesManager;
import com.ls.drupalconapp.model.UpdatesManager;
import com.ls.drupalconapp.model.managers.BofsManager;
import com.ls.drupalconapp.model.managers.FavoriteManager;
import com.ls.drupalconapp.model.managers.ProgramManager;
import com.ls.drupalconapp.model.managers.SocialManager;
import com.ls.drupalconapp.ui.activity.HomeActivity;
import com.ls.drupalconapp.ui.adapter.BaseEventDaysPagerAdapter;
import com.ls.drupalconapp.ui.dialog.FilterDialog;
import com.ls.drupalconapp.ui.drawer.DrawerManager;
import com.ls.drupalconapp.ui.receiver.FavoriteReceiverManager;
import com.ls.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EventHolderFragment extends Fragment {

	public static final String TAG = "ProjectsFragment";
	private static final String EXTRAS_ARG_MODE = "EXTRAS_ARG_MODE";
	private static final int ANIMATION_DURATION = 250;

	private ViewPager mViewPager;
	private View mTabView;
	private PagerSlidingTabStrip mPagerTabs;
	private BaseEventDaysPagerAdapter mAdapter;
	private List<Long> mLevelIds = new ArrayList<>();
	private List<Long> mTrackIds = new ArrayList<>();
	private HomeActivity homeActivity;

	private DrawerManager.EventMode mEventMode;
	private View mTxtNoEvents;
	private View mNoFavorites;
	private List<Long> mDayIdList;

	private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener()
	{
		@Override
		public void onDataUpdated(List<Integer> requestIds) {
			Log.d("UPDATED", "EventHolderFragment");

			Activity activity = getActivity();
			if (activity instanceof HomeActivity) {
				((HomeActivity) activity).initFilterDialog();

				FilterDialog filterDialog = ((HomeActivity) activity).mFilterDialog;
				if (filterDialog != null) {
					filterDialog.clearFilter();

					if (((HomeActivity) activity).mFilterDialog.isAdded()) {
						((HomeActivity) activity).mFilterDialog.dismissAllowingStateLoss();
					}
				}
			}

			for (int id : requestIds) {
				int eventModePos = UpdatesManager.convertEventIdToEventModePos(id);
				if (eventModePos == mEventMode.ordinal()) {
					updateData();
					break;
				}
			}
		}
	};

	private FavoriteReceiverManager favoriteReceiverManager = new FavoriteReceiverManager(
			new FavoriteReceiverManager.FavoriteUpdatedListener() {
				@Override
				public void onFavoriteUpdated(long eventId, boolean isFavorite) {
					if (getView() == null) {
						return;
					}
					if (mEventMode == DrawerManager.EventMode.Favorites) {
						updateData();
					}
				}
			});

	public static EventHolderFragment newInstance(int modePos) {
		EventHolderFragment fragment = new EventHolderFragment();
		Bundle args = new Bundle();
		args.putInt(EXTRAS_ARG_MODE, modePos);
		fragment.setArguments(args);

		return fragment;
	}

	public List<Long> getmLevelIds() {
		return mLevelIds;
	}

	public List<Long> getmTrackIds() {
		return mTrackIds;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		homeActivity = (HomeActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_holder_event, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_filter, menu);

		MenuItem filter = menu.findItem(R.id.actionFilter);
		if (filter != null) {
			updateFilterState(filter);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.actionFilter:
				showFilter();
				break;
		}
		return true;
	}


	private void showFilter() {
		Activity activity = getActivity();
		if (activity instanceof HomeActivity) {

			if (!((HomeActivity) activity).mFilterDialog.isAdded()) {
				((HomeActivity) activity).mFilterDialog.show(getActivity().getSupportFragmentManager(), "filter");
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Model.instance().getUpdatesManager().registerUpdateListener(updateListener);
		favoriteReceiverManager.register(getActivity());

		initData();
		initView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
		favoriteReceiverManager.unregister(getActivity());
	}

	private void initData() {
		Bundle bundle = getArguments();
		if (bundle == null) {
			return;
		}
		mLevelIds = homeActivity.getLevelIds();
		mTrackIds = homeActivity.getTrackIds();

		int eventPos = bundle.getInt(EXTRAS_ARG_MODE, DrawerManager.EventMode.Program.ordinal());
		mEventMode = DrawerManager.EventMode.values()[eventPos];
	}

    /*
	 * Call this method in onCreate() in your fragment
     */
//    protected void setMode(int mode) {
//        mMode = mode;
//    }

	private void initView() {
		View view = getView();
		if (view == null) {
			return;
		}

		mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mAdapter = new BaseEventDaysPagerAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setAlpha(0);

		mTabView = view.findViewById(R.id.tabView);
		mTabView.setAlpha(1);

		mPagerTabs = (PagerSlidingTabStrip) getView().findViewById(R.id.pager_tab_strip);
		Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
		mPagerTabs.setTypeface(typeface, 0);
		mPagerTabs.setViewPager(mViewPager);

		mTxtNoEvents = view.findViewById(R.id.txtNoEvents);
		mNoFavorites = view.findViewById(R.id.emptyIcon);
		mDayIdList = new ArrayList<>();

		new PerformLoadData().execute();
	}

	class PerformLoadData extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			switch (mEventMode) {
				case Bofs:
					BofsManager bofsManager = Model.instance().getBofsManager();
					mDayIdList.addAll(bofsManager.getBofsDays());
					break;
				case Social:
					SocialManager socialManager = Model.instance().getSocialManager();
					mDayIdList.addAll(socialManager.getSocialsDays());
					break;
				case Favorites:
					FavoriteManager favoriteManager = Model.instance().getFavoriteManager();
					mDayIdList.addAll(favoriteManager.getFavoriteEventDays());
					break;
				default:
					ProgramManager programManager = Model.instance().getProgramManager();
					mDayIdList.addAll(programManager.getProgramDays());
					break;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			updateViews();
		}
	}

	private void updateViews() {
		if (mEventMode == DrawerManager.EventMode.Program) {
			setHasOptionsMenu(true);
		} else {
			setHasOptionsMenu(false);
		}

		if (mDayIdList.isEmpty()) {
			mPagerTabs.setVisibility(View.GONE);
			if (mEventMode == DrawerManager.EventMode.Favorites) {
				mNoFavorites.setVisibility(View.VISIBLE);
			} else {
				mTxtNoEvents.setVisibility(View.VISIBLE);
			}

		} else {
			mTxtNoEvents.setVisibility(View.GONE);
			mPagerTabs.setVisibility(View.VISIBLE);
		}

		mAdapter.setData(mDayIdList, mEventMode);
		switchToCurrentDay(mDayIdList);
		mViewPager.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).start();
		mTabView.animate().alpha(0).setDuration(ANIMATION_DURATION).start();
	}

	private void switchToCurrentDay(List<Long> days) {
		int item = 0;
		for (Long millis : days) {
			if (DateUtils.isToday(millis)) {
				mViewPager.setCurrentItem(item);
				break;
			}
			item++;
		}
	}

	private void updateData() {
		mAdapter = null;
		mViewPager.setAdapter(null);
		initView();
	}

	private void updateFilterState(@NotNull MenuItem filter) {
		boolean isFilterUsed = false;
		List<Long> levelIds = PreferencesManager.getInstance().loadExpLevel();
		List<Long> trackIds = PreferencesManager.getInstance().loadTracks();

		mLevelIds.addAll(levelIds);
		mTrackIds.addAll(trackIds);

		if (!levelIds.isEmpty() || !trackIds.isEmpty()) {
			isFilterUsed = true;
		}

		if (isFilterUsed) {
			filter.setIcon(getResources().getDrawable(R.drawable.ic_filter));
		} else {
			filter.setIcon(getResources().getDrawable(R.drawable.ic_filter_empty));
		}
	}
}
