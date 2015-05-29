package com.ls.drupalconapp.ui.adapter.item;

import com.ls.drupalconapp.model.data.Event;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Yakiv M. on 25.09.2014.
 */
public class SimpleTimeRangeCreator implements EventItemCreator {

    @Override
    public TimeRangeItem getItem(@NotNull Event event) {
        TimeRangeItem result = new TimeRangeItem();

        result.setType(event.getType());
        result.setFromTime(event.getFromTime());
        result.setToTime(event.getToTime());
        result.setEvent(event);

        return result;
    }
}
