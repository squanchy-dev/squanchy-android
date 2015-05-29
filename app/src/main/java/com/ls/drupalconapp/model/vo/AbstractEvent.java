package com.ls.drupalconapp.model.vo;

public abstract class AbstractEvent {
    protected long id;
    protected String beginTime;
    protected String endTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public abstract boolean isBreakEvent();
}
