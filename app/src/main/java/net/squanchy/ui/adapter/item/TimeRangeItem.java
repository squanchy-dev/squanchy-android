package net.squanchy.ui.adapter.item;

import net.squanchy.model.data.Event;

import java.util.Date;

public class TimeRangeItem extends EventListItem {

    private Date mDate;
    private Event mEvent;
    private String mTrack;
    private boolean isFirst = false;

    private long mType;

    @Override
    public int getAdapterType() {
        return TYPE_TIME_RANGE;
    }

    @Override
    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event mEvent) {
        this.mEvent = mEvent;
    }

    public long getType() {
        return mType;
    }

    public void setType(long type) {
        mType = type;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setTrack(String track) {
        mTrack = track;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public String getTrack() {
        return mTrack;
    }
}
