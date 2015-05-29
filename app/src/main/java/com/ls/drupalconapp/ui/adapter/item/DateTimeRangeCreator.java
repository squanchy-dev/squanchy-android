package com.ls.drupalconapp.ui.adapter.item;

import com.ls.drupalconapp.model.data.Event;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Yakiv M. on 25.09.2014.
 */
public class DateTimeRangeCreator implements EventItemCreator {

    SimpleTimeRangeCreator mSimpleTimeRangeCreator = new SimpleTimeRangeCreator();

    @Override
    public TimeRangeItem getItem(@NotNull Event event) {
        TimeRangeItem result = mSimpleTimeRangeCreator.getItem(event);
        result.setDate(event.getDate());
        return result;
    }
}
