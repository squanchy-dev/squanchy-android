package net.squanchy.ui.adapter.item;

import net.squanchy.model.data.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class EventListItem {

    public static final int TYPE_TIME_RANGE = 0;
    public static final int TYPE_PROGRAM = 1;
    public static final int TYPE_BOFS = 2;
    public static final int TYPE_SOCIAL = 3;
    public static final int TYPE_SECTION_NAME = 4;

    private Event mEvent;
    private List<String> mSpeakers = new ArrayList<>();
    private boolean isLast = false;

    abstract public int getAdapterType();

    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event event) {
        mEvent = event;
    }

    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    public boolean isNotLast() {
        return !isLast;
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

    @Override
    public String toString() {
        if (mEvent != null) {
            return "TimeRangeItem{" +
                    "mEvent=" + mEvent.getName() +
                    "mEventType=" + mEvent.getType() + "} ";
        } else {
            return "TimeRangeItem{" +
                    "mEvent=" + "no event" +
                    "} ";
        }
    }

}
