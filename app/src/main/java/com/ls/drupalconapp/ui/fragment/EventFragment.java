package com.ls.drupalconapp.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.EventGenerator;
import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.model.data.Type;
import com.ls.drupalconapp.ui.activity.EventDetailsActivity;
import com.ls.drupalconapp.ui.adapter.NewEventsAdapter;
import com.ls.drupalconapp.ui.adapter.item.EventListItem;
import com.ls.drupalconapp.ui.adapter.item.SimpleTimeRangeCreator;
import com.ls.drupalconapp.ui.adapter.item.TimeRangeItem;
import com.ls.drupalconapp.ui.drawer.DrawerManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventFragment extends Fragment implements NewEventsAdapter.OnClickListener {

	private static final int ANIMATION_DURATION = 250;
	public static final String TAG = "EventsFragment";

	private static final String EXTRAS_ARG_MODE = "EXTRAS_ARG_MODE";
	private static final String EXTRAS_ARG_DAY = "EXTRAS_ARG_DAY";

	private long mDay;
	private List<Long> levelIds = new ArrayList<>();
	private List<Long> trackIds = new ArrayList<>();

	private DrawerManager.EventMode mEventMode;
	private NewEventsAdapter mAdapter;

	private ListView mListEvent;
	private ProgressBar mProgressBar;

	public static Fragment newInstance(int modePos, long day) {
		Fragment fragment = new EventFragment();
		Bundle args = new Bundle();
		args.putInt(EXTRAS_ARG_MODE, modePos);
		args.putLong(EXTRAS_ARG_DAY, day);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fr_event, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initData();
		loadEvents();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void initViews() {
		if (getView() != null) {
			mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
			mListEvent = (ListView) getView().findViewById(R.id.listView);
			mListEvent.setAlpha(0);
		}
	}

	private void initData() {
		Bundle bundle = getArguments();
		if (bundle == null) {
			return;
		}
		int eventPost = bundle.getInt(EXTRAS_ARG_MODE, DrawerManager.EventMode.Program.ordinal());
		mEventMode = DrawerManager.EventMode.values()[eventPost];

		mDay = bundle.getLong(EXTRAS_ARG_DAY, 0);
		EventHolderFragment parentFragment = (EventHolderFragment) getParentFragment();

		if (parentFragment instanceof EventHolderFragment) {
			levelIds = parentFragment.getmLevelIds();
			trackIds = parentFragment.getmTrackIds();
		}
	}

	private void loadEvents() {
		new AsyncTask<Void, Void, List<EventListItem>>() {
			@Override
			protected List<EventListItem> doInBackground(Void... params) {
				FragmentActivity activity = getActivity();
				if (activity != null) {
					return getEventItems();
				} else {
					return new ArrayList<EventListItem>();
				}
			}

			@Override
			protected void onPostExecute(List<EventListItem> eventListItems) {
				View view = getView();
				if (!isDetached() && view != null) {
					handleEventsResult(eventListItems);
					if (mEventMode != DrawerManager.EventMode.Favorites) {
						view.findViewById(R.id.dark_background).setVisibility(View.VISIBLE);
					}
				}
			}
		}.execute();
	}

	private List<EventListItem> getEventItems() {
		List<EventListItem> eventList = new ArrayList<>();
		EventGenerator generator = new EventGenerator();
		switch (mEventMode) {
			case Program:
				eventList.addAll(generator.generate(mDay, Event.PROGRAM_CLASS, levelIds, trackIds, new SimpleTimeRangeCreator()));
				break;
			case Bofs:
				eventList.addAll(generator.generate(mDay, Event.BOFS_CLASS, new SimpleTimeRangeCreator()));
				break;
			case Social:
				eventList.addAll(generator.generate(mDay, Event.SOCIALS_CLASS, new SimpleTimeRangeCreator()));
				break;
			case Favorites:
				eventList.addAll(generator.generateForFavorites(mDay));
				break;
		}
		return eventList;
	}

	private void updateMySchedule(final NewEventsAdapter adapter) {
		new AsyncTask<Void, Void, List<EventListItem>>() {
			@Override
			protected List<EventListItem> doInBackground(Void... params) {
				FragmentActivity activity = getActivity();
				if (activity != null) {
					EventGenerator generator = new EventGenerator();
					return generator.generateForFavorites(mDay);
				} else {
					return new ArrayList<EventListItem>();
				}
			}

			@Override
			protected void onPostExecute(List<EventListItem> eventListItems) {
				View view = getView();
				if (!isDetached() && view != null) {
					adapter.setData(eventListItems);
				}
			}
		}.execute();
	}

	private void handleEventsResult(List<EventListItem> eventListItems) {
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}

		mAdapter = new NewEventsAdapter(getActivity().getBaseContext(), eventListItems, mEventMode, this);
		initFavoriteAdditionalAction(mAdapter);
		mListEvent.setAdapter(mAdapter);

		if (isDateValid() && mEventMode != DrawerManager.EventMode.Favorites) {
			int index = getCurrentTimeIndex(eventListItems);
			mListEvent.setSelection(index);
		}

		mListEvent.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).start();
	}

	@Override
	public void onClick(int position) {
		onItemClick(position);
	}

	private void onItemClick(int position) {
		EventListItem item = mAdapter.getItem(position);
		if (item.getEvent() != null && item.getEvent().getId() != 0 &&
				(item.getEvent().getType() == Type.SPEACH || item.getEvent().getType() == Type.SPEACH_OF_DAY)) {
			Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
			intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, item.getEvent().getId());
			intent.putExtra(EventDetailsActivity.EXTRA_DAY, mDay);
			startActivity(intent);
		}
	}

	private boolean isDateValid() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String systemDate = dateFormat.format(new Date(System.currentTimeMillis()));
		String eventDate = dateFormat.format(new Date(mDay));

		if (systemDate.equals(eventDate)) {
			return true;
		} else {
			return false;
		}
	}

	private int getCurrentTimeIndex(List<EventListItem> eventListItems) {

		long systemDate = System.currentTimeMillis();
		Calendar systemTime = Calendar.getInstance();
		systemTime.setTimeInMillis(systemDate);

		int systemHour = systemTime.get(Calendar.HOUR_OF_DAY);
		SparseIntArray timeRangeItemArray = new SparseIntArray();

		int index = 0;
		int iterator = 0;

		for (EventListItem item : eventListItems) {
			if (item instanceof TimeRangeItem) {
				TimeRangeItem timeRange = (TimeRangeItem) item;
				if (timeRange.getDate() != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(timeRange.getDate());
					int hour = calendar.get(Calendar.HOUR_OF_DAY);
					timeRangeItemArray.put(iterator, hour);
				}
			}
			iterator++;
		}

		long minDiff = -1;
		int key;
		for (int i = 0; i < timeRangeItemArray.size(); i++) {
			key = timeRangeItemArray.keyAt(i);
			int hour = timeRangeItemArray.get(key);
			long diff = Math.abs(hour - systemHour);
			if ((minDiff == -1) || (diff < minDiff)) {
				minDiff = diff;
				index = key;
			}

		}

		return index;
	}

	private void initFavoriteAdditionalAction(final NewEventsAdapter adapter) {
		if (mEventMode == DrawerManager.EventMode.Favorites) {
			adapter.setFavoriteAdditionAction(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					updateMySchedule(adapter);
				}
			});
		}
	}
}
