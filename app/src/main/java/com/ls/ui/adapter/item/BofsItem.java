package com.ls.ui.adapter.item;

import com.ls.drupalconapp.model.data.Event;

/**
 * Created by Yakiv M. on 24.09.2014.
 */
public class BofsItem implements EventListItem {

    private Event mEvent;

    private boolean  isLast = false;

    @Override
    public int getAdapterType() {
        return TYPE_BOFS;
    }

    @Override
    public Event getEvent() {
        return mEvent;
    }

    @Override
    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setEvent(Event event) {
        mEvent = event;
    }
}
