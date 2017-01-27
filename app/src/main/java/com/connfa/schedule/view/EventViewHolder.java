package com.connfa.schedule.view;

import android.support.v7.widget.RecyclerView;

import com.connfa.model.data.Event;

class EventViewHolder extends RecyclerView.ViewHolder {

    public EventViewHolder(EventItemView itemView) {
        super(itemView);
    }

    public void updateWith(Event event) {
        ((EventItemView) itemView).updateWith(event);
    }
}
