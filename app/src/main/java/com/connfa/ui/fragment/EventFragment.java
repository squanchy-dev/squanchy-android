package com.connfa.ui.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.connfa.R;
import com.connfa.analytics.Analytics;
import com.connfa.model.EventGenerator;
import com.connfa.model.PreferencesManager;
import com.connfa.model.data.Event;
import com.connfa.ui.activity.EventDetailsActivity;
import com.connfa.ui.adapter.EventsAdapter;
import com.connfa.ui.adapter.item.EventListItem;
import com.connfa.ui.adapter.item.SimpleTimeRangeCreator;
import com.connfa.ui.adapter.item.TimeRangeItem;
import com.connfa.ui.drawer.DrawerManager;
import com.connfa.ui.receiver.ReceiverManager;
import com.connfa.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventFragment extends Fragment implements EventsAdapter.Listener {

    private static final String EXTRAS_ARG_MODE = "EXTRAS_ARG_MODE";
    private static final String EXTRAS_ARG_DAY = "EXTRAS_ARG_DAY";

    private List<Long> levelIds;
    private List<Long> trackIds;

    private long day;

    private DrawerManager.EventMode eventMode;
    private EventsAdapter adapter;

    private ListView listView;
    private ProgressBar progressBar;

    private EventGenerator generator;

    private final ReceiverManager receiverManager = new ReceiverManager(
            new ReceiverManager.FavoriteUpdatedListener() {
                @Override
                public void onFavoriteUpdated(long eventId, boolean isFavorite) {
                    if (eventMode != DrawerManager.EventMode.FAVORITES) {
                        new LoadData().execute();
                    }
                }
            });

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
        return inflater.inflate(R.layout.fr_event, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initViews();
        new LoadData().execute();
        receiverManager.register(getActivity());
    }

    @Override
    public void onClick(int position) {
        onItemClick(position);
    }

    @Override
    public void onDestroy() {
        generator.setShouldBreak(true);
        receiverManager.unregister(getActivity());
        super.onDestroy();
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int eventPost = bundle.getInt(EXTRAS_ARG_MODE, DrawerManager.EventMode.PROGRAM.ordinal());
            eventMode = DrawerManager.EventMode.values()[eventPost];

            day = bundle.getLong(EXTRAS_ARG_DAY, 0);
            PreferencesManager preferencesManager = PreferencesManager.create(getActivity());
            levelIds = preferencesManager.getExpLevels();
            trackIds = preferencesManager.getTracks();
        }
        generator = new EventGenerator(getContext());
    }

    private void initViews() {
        if (getView() != null) {
            progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

            adapter = new EventsAdapter(getActivity());
            adapter.setOnItemClickListener(this);

            listView = (ListView) getView().findViewById(R.id.listView);
            listView.setAdapter(adapter);
        }
    }

    class LoadData extends AsyncTask<Void, Void, List<EventListItem>> {

        @Override
        protected List<EventListItem> doInBackground(Void... params) {
            return getEventItems();
        }

        @Override
        protected void onPostExecute(List<EventListItem> result) {
            updateViewsUI(result);
        }
    }

    private void updateViewsUI(final List<EventListItem> eventList) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handleEventsResult(eventList);
                }
            });
        }
    }

    private List<EventListItem> getEventItems() {
        List<EventListItem> eventList = new ArrayList<>();

        switch (eventMode) {
            case PROGRAM:
                eventList.addAll(generator.generate(day, Event.PROGRAM_CLASS, levelIds, trackIds, new SimpleTimeRangeCreator()));
                break;
            case BOFS:
                eventList.addAll(generator.generate(day, Event.BOFS_CLASS, levelIds, trackIds, new SimpleTimeRangeCreator()));
                break;
            case SOCIAL:
                eventList.addAll(generator.generate(day, Event.SOCIALS_CLASS, levelIds, trackIds, new SimpleTimeRangeCreator()));
                break;
            case FAVORITES:
                eventList.addAll(generator.generateForFavorites(day, new SimpleTimeRangeCreator()));
                break;
        }
        return eventList;
    }

    private void handleEventsResult(List<EventListItem> eventListItems) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        adapter.setData(eventListItems, eventMode);
        if (DateUtils.isToday(getActivity(), day) && eventMode != DrawerManager.EventMode.FAVORITES) {
            int index = getCurrentTimePosition(eventListItems);
            listView.setSelection(index);
        }
    }

    private void onItemClick(int position) {
        EventListItem item = adapter.getItem(position);

        if (item.getEvent() != null && item.getEvent().getId() != 0) {
            Long eventId = item.getEvent().getId();
            String eventName = item.getEvent().getName();
            Analytics.from(getActivity())
                    .trackEvent(
                            getString(R.string.event_category), getString(R.string.action_open), eventId + " " + eventName
                    );
            EventDetailsActivity.startThisActivity(getActivity(), item.getEvent().getId(), day);
        }
    }

    private int getCurrentTimePosition(List<EventListItem> eventListItems) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(DateUtils.getTimeZone(getActivity()));
        int deviceTimeMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);

        int minDifference = Integer.MAX_VALUE;
        int position = 0;

        EventListItem eventToSelect = null;

        for (EventListItem item : eventListItems) {

            if (item instanceof TimeRangeItem) {

                Event event = item.getEvent();
                calendar.setTimeInMillis(event.getFromMillis());
                int eventTimeMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);

                int difference = Math.abs(eventTimeMinutes - deviceTimeMinutes);

                if (eventTimeMinutes <= deviceTimeMinutes && minDifference > difference) {
                    minDifference = difference;
                    eventToSelect = item;
                }

            }
        }

        if (eventToSelect != null) {
            position = eventListItems.indexOf(eventToSelect);
        }
        return position;
    }
}
