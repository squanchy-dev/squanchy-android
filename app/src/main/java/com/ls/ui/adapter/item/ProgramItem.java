package com.ls.ui.adapter.item;

import com.ls.drupalcon.model.data.Event;

import java.util.ArrayList;
import java.util.List;

public class ProgramItem implements EventListItem {

    private Event mEvent;
    private String mTrack;
    private String mLevel;

    private boolean isLast = false;

    private List<String> mSpeakers = new ArrayList<String>();

    @Override
    public int getAdapterType() {
        return TYPE_PROGRAM;
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

    public String getTrack() {
        return mTrack;
    }

    public void setTrack(String track) {
        mTrack = track;
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String level) {
        mLevel = level;
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
