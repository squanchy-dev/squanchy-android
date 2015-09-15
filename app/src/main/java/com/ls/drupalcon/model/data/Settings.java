package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

public class Settings {

    @SerializedName("titleMajor")
    private String titleMajor;

    @SerializedName("titleMinor")
    private String titleMinor;

    @SerializedName("twitterWidget")
    private String twitterWidget;

    @SerializedName("timezone")
    private String timeZone;

    public String getTitleMajor() {
        return titleMajor;
    }

    public void setTitleMajor(String titleMajor) {
        this.titleMajor = titleMajor;
    }

    public String getTitleMinor() {
        return titleMinor;
    }

    public void setTitleMinor(String titleMinor) {
        this.titleMinor = titleMinor;
    }

    public String getTwitterWidget() {
        return twitterWidget;
    }

    public void setTwitterWidget(String twitterWidget) {
        this.twitterWidget = twitterWidget;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
