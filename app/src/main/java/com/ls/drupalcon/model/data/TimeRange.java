package com.ls.drupalcon.model.data;

import java.util.Calendar;

public class TimeRange {

    private long mDate;
    private Calendar mFromTime;
    private Calendar mToTime;

    public TimeRange(long date, Calendar fromTime, Calendar toTime) {
        mDate = date;
        mFromTime = fromTime;
        mToTime = toTime;
    }

    public Calendar getFromTime() {
        return mFromTime;
    }

    public void setFromTime(Calendar fromTime) {
        mFromTime = fromTime;
    }

    public Calendar getToTime() {
        return mToTime;
    }

    public void setToTime(Calendar toTime) {
        mToTime = toTime;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimeRange timeRange = (TimeRange) o;

        if (mDate != timeRange.mDate) {
            return false;
        }
        if (mFromTime != null ? !mFromTime.equals(timeRange.mFromTime)
                : timeRange.mFromTime != null) {
            return false;
        }
        if (mToTime != null ? !mToTime.equals(timeRange.mToTime) : timeRange.mToTime != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (mDate ^ (mDate >>> 32));
        result = 31 * result + (mFromTime != null ? mFromTime.hashCode() : 0);
        result = 31 * result + (mToTime != null ? mToTime.hashCode() : 0);
        return result;
    }
}
