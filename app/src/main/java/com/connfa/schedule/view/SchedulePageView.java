package com.connfa.schedule.view;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.connfa.PageView;
import com.connfa.R;
import com.connfa.schedule.domain.view.Event;

import java.util.List;

public class SchedulePageView extends FrameLayout implements PageView<List<Event>> {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EventsAdapter adapter;

    public SchedulePageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SchedulePageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventsAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateWith(List<Event> newData) {
        DiffUtil.Callback callback = new EventsDiffCallback(adapter.events(), newData);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback, true);    // TODO move off the UI thread
        adapter.updateWith(newData);
        diffResult.dispatchUpdatesTo(adapter);
        progressBar.setVisibility(GONE);
    }

    private static class EventsDiffCallback extends DiffUtil.Callback {

        private final List<Event> oldEvents;
        private final List<Event> newEvents;

        private EventsDiffCallback(List<Event> oldEvents, List<Event> newEvents) {
            this.oldEvents = oldEvents;
            this.newEvents = newEvents;
        }

        @Override
        public int getOldListSize() {
            return oldEvents.size();
        }

        @Override
        public int getNewListSize() {
            return newEvents.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Event oldEvent = oldEvents.get(oldItemPosition);
            Event newEvent = newEvents.get(newItemPosition);
            return oldEvent.id() == newEvent.id();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Event oldEvent = oldEvents.get(oldItemPosition);
            Event newEvent = newEvents.get(newItemPosition);
            return oldEvent.equals(newEvent);
        }
    }
}
