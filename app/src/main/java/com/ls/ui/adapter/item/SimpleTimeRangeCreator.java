package com.ls.ui.adapter.item;

import com.ls.drupalcon.model.data.Event;

import org.jetbrains.annotations.NotNull;

public class SimpleTimeRangeCreator implements EventItemCreator {

    @Override
    public TimeRangeItem getItem(@NotNull Event event) {
        TimeRangeItem result = new TimeRangeItem();

        result.setType(event.getType());
        result.setEvent(event);

        return result;
    }
}
