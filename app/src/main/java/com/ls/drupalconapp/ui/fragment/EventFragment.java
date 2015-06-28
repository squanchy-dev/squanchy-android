package com.ls.drupalconapp.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.EventGenerator;
import com.ls.drupalconapp.model.PreferencesManager;
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

public class EventFragment extends Fragment implements NewEventsAdapter.Listener {

	private static final String EXTRAS_ARG_MODE = "EXTRAS_ARG_MODE";
	private static final String EXTRAS_ARG_DAY = "EXTRAS_ARG_DAY";
	private static final int ANIMATION_DURATION = 250;

	private List<Long> levelIds;
	private List<Long> trackIds;
	private long mDay;

    private DrawerManager.EventMode mEventMode;
    private NewEventsAdapter mAdapter;

    private ListView mListView;
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
		initData();
		initViews();
		loadEvents();
	}

	@Override
	public void onClick(int position) {
		onItemClick(position);
	}

	private void initViews() {
		if (getView() != null) {
			mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

			mAdapter = new NewEventsAdapter(getActivity());
			mAdapter.setOnClickListener(this);

			mListView = (ListView) getView().findViewById(R.id.listView);
			mListView.setAlpha(0);
			mListView.setAdapter(mAdapter);
		}
	}

	private void initData() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			int eventPost = bundle.getInt(EXTRAS_ARG_MODE, DrawerManager.EventMode.Program.ordinal());
			mEventMode = DrawerManager.EventMode.values()[eventPost];

			mDay = bundle.getLong(EXTRAS_ARG_DAY, 0);
			levelIds = PreferencesManager.getInstance().loadExpLevel();
			trackIds = PreferencesManager.getInstance().loadTracks();
		}
	}

	private void loadEvents() {
		new AsyncTask<Void, Void, List<EventListItem>>() {
			@Override
			protected List<EventListItem> doInBackground(Void... params) {
				return getEventItems();
			}

			@Override
			protected void onPostExecute(List<EventListItem> eventListItems) {
				if (!isDetached()) {
					handleEventsResult(eventListItems);
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

	private void handleEventsResult(List<EventListItem> eventListItems) {
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}

		mAdapter.setData(eventListItems, mEventMode);
		if (isDateValid() && mEventMode != DrawerManager.EventMode.Favorites) {
			int index = getCurrentTimeIndex(eventListItems);
			mListView.setSelection(index);
		}
		mListView.animate().alpha(1.0f).setDuration(ANIMATION_DURATION).start();
	}


	private void onItemClick(int position) {
		EventListItem item = mAdapter.getItem(position);

		if (item.getEvent() != null && item.getEvent().getId() != 0) {
			long type = item.getEvent().getType();

			if (type == Type.SPEACH || type == Type.SPEACH_OF_DAY) {
//				Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
//				intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, item.getEvent().getId());
//				intent.putExtra(EventDetailsActivity.EXTRA_DAY, mDay);
//				startActivity(intent);
				EventDetailsActivity.startThisActivity(getActivity(), item.getEvent().getId(), mDay);
			}
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
}
