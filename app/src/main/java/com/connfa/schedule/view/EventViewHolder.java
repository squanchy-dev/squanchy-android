package com.connfa.schedule.view;

import android.support.v7.widget.RecyclerView;

import com.connfa.schedule.domain.view.Event;

class EventViewHolder extends RecyclerView.ViewHolder {

    public EventViewHolder(EventItemView itemView) {
        super(itemView);
    }

    public void updateWith(Event event) {
        ((EventItemView) itemView).updateWith(event);
    }
}
