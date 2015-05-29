package com.ls.drupalconapp.model.vo;

import java.util.List;

public class ProgramEvent extends BoFsEvent {

    private String speakersName;
    private String track;
    private String expLevel;
    private boolean isKeynote;
    private List<Speaker> speakers;

    public String getSpeakersName() {
        return speakersName;
    }

    public void setSpeakersName(String speakersName) {
        this.speakersName = speakersName;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String codding) {
        this.track = codding;
    }

    public String getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(String expLevel) {
        this.expLevel = expLevel;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }

    public boolean isKeynote() {
        return isKeynote;
    }

    public void setKeynote(boolean isKeynote) {
        this.isKeynote = isKeynote;
    }
}
