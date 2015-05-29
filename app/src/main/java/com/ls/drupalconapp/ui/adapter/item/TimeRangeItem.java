package com.ls.drupalconapp.ui.adapter.item;

import com.ls.drupalconapp.model.data.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yakiv M. on 24.09.2014.
 */
public class TimeRangeItem implements EventListItem {

    private String mFromTime;
    private String mToTime;
    private Date mDate;
    private Event mEvent;
    private String mTrack;
    private boolean isFirst = false;
    private boolean isLast = false;

    private long mType;

    private List<String> mSpeakers = new ArrayList<String>();

    @Override
    public int getAdapterType() {
        return TYPE_TIME_RANGE;
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

    public void setEvent(Event mEvent) {
        this.mEvent = mEvent;
    }

    public String getFromTime() {
        return mFromTime;
    }

    public void setFromTime(String fromTime) {
        mFromTime = fromTime;
    }

    public String getToTime() {
        return mToTime;
    }

    public void setToTime(String toTime) {
        mToTime = toTime;
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

    public List<String> getSpeakers() {
        return mSpeakers;
    }

    public void setSpeakers(List<String> speakers) {
        mSpeakers = speakers;
    }

    public void setTrack(String track) {
        mTrack = track;
    }

    public void setFirst(boolean isFirst){
        this.isFirst = isFirst;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public String getTrack() {
        return mTrack;
    }
}
