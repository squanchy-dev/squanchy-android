package com.connfa.schedule.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.connfa.R;
import com.connfa.model.data.Event;

import java.util.Collections;
import java.util.List;

class EventsAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private final Context context;

    private List<Event> events = Collections.emptyList();

    EventsAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return events.get(position).getId();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EventItemView itemView = (EventItemView) LayoutInflater.from(context)
                .inflate(R.layout.item_schedule_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.updateWith(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public List<Event> events() {
        return events;
    }

    public void updateWith(List<Event> events) {
        this.events = events;
    }
}
