package com.ls.drupalcon.model.data;

import com.ls.utils.DateUtils;

import java.text.SimpleDateFormat;

public class SpeakerDetailsEvent {

	private long eventId;
    private String place;
	private String eventName;
	private long from;
	private long to;
	private String date;
	private String levelName;
	private String trackName;
	private boolean isFavorite;

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(long time) {
		this.date = DateUtils.getInstance().getWeekDay(time);
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public long getTo() {
		return to;
	}

	public void setTo(long to) {
		this.to = to;
	}
}
