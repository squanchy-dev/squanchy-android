package com.ls.ui.adapter.item;

import com.ls.drupalcon.model.data.Event;

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
