package com.ls.drupalconapp.model.data;

import java.text.SimpleDateFormat;

/**
 * Created by Kuhta on 24.09.2014.
 */
public class SpeakerDetailsEvent {
	private static final SimpleDateFormat formatter = new SimpleDateFormat("EEE");

	private long eventId;
    private String place;
	private String eventName;
	private String from;
	private String to;
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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getDate() {
		return date;
	}

	public void setDate(long time) {
		this.date = formatter.format(time);
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
}
