package com.connfa.ui.adapter.item;

import com.connfa.model.data.Event;

public interface EventItemCreator {

    EventListItem getItem(Event event);

}
