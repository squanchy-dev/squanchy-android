package com.ls.ui.adapter.item;

import com.ls.drupalcon.model.data.Event;

import java.util.ArrayList;
import java.util.List;

public class BofsItem implements EventListItem {

    private Event mEvent;
    private List<String> mSpeakers = new ArrayList<String>();

    private boolean isLast = false;

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

    public List<String> getSpeakers() {
        return mSpeakers;
    }

    public void setSpeakers(List<String> speakers) {
        mSpeakers = speakers;
    }

    public void addSpeaker(String speaker) {
        mSpeakers.add(speaker);
    }
}
