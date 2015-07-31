package com.ls.ui.adapter.item;

import com.ls.drupalconapp.model.data.Event;

/**
 * Created by Yakiv M. on 24.09.2014.
 */
public class SocialItem implements EventListItem {

    private Event mEvent;

    @Override
    public int getAdapterType() {
        return TYPE_SOCIAL;
    }

    @Override
    public Event getEvent() {
        return mEvent;
    }

    @Override
    public void setLast(boolean isLast) {
    }

    public void setEvent(Event event) {
        mEvent = event;
    }
}
