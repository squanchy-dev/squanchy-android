package com.ls.drupalconapp.model.vo;

public class BoFsEvent extends AbstractEvent {
    protected long id;
    protected String name;
    protected String room;
    protected String beginTime;
    protected String endTime;
    protected String date;
    protected String description;
    protected boolean isAddToSchedule;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAddToSchedule() {
        return isAddToSchedule;
    }

    public void setAddToSchedule(boolean isAddToSchedule) {
        this.isAddToSchedule = isAddToSchedule;
    }

    @Override
    public boolean isBreakEvent() {
        return false;
    }
}
