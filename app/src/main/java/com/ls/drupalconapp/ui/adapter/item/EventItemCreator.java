package com.ls.drupalconapp.ui.adapter.item;

import com.ls.drupalconapp.model.data.Event;

/**
 * Created by Yakiv M. on 25.09.2014.
 */
public interface EventItemCreator {

    public EventListItem getItem(Event event);

}
