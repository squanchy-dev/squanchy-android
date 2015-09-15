package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

public class SettingsHolder {

    @SerializedName("settings")
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
