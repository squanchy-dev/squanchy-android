package com.ls.ui.adapter.item;

import com.ls.drupalcon.model.data.Event;

public interface EventItemCreator {

    public EventListItem getItem(Event event);

}
