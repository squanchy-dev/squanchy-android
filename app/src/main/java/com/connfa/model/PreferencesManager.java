package com.connfa.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class PreferencesManager {

    private static final String PREF_NAME = "com.ls.drupalconapp.model.MAIN_PREFERENCES";
    private static final String TIME_ZONE = "TIME_ZONE";
    private static final String TWITTER_SEARCH_QUERY = "TWITTER_SEARCH_QUERY";
    private static final String KEY_LAST_UPDATE_DATE = "KEY_LAST_UPDATE_DATE";
    private static final String KEY_INFO_MAJOR_TITLE = "KEY_INFO_MAJOR_TITLE";
    private static final String KEY_INFO_MINOR_TITLE = "KEY_INFO_MINOR_TITLE";
    private static final String FILTER_EXP_LEVEL = "FILTER_EXP_LEVEL";
    private static final String FILTER_TRACK = "FILTER_TRACK ";

    public static PreferencesManager create(Context context) {
        return new PreferencesManager(context);
    }

    private final SharedPreferences sharedPreferences;

    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public TimeZone getServerTimeZoneObject() {
        String timezoneId = getTimeZone();
        return TimeZone.getTimeZone(timezoneId);
    }

    private String getTimeZone() {
        return sharedPreferences.getString(TIME_ZONE, "");
    }

    public void saveTimeZone(String timeZone) {
        sharedPreferences.edit().putString(TIME_ZONE, timeZone).apply();
    }

    public String getTwitterSearchQuery() {
        return sharedPreferences.getString(TWITTER_SEARCH_QUERY, "");
    }

    public void saveTwitterSearchQuery(String searchQuery) {
        sharedPreferences.edit().putString(TWITTER_SEARCH_QUERY, searchQuery).apply();
    }

    public String getLastUpdateDate() {
        return sharedPreferences.getString(KEY_LAST_UPDATE_DATE, "");
    }

    public void saveLastUpdateDate(String value) {
        sharedPreferences.edit().putString(KEY_LAST_UPDATE_DATE, value).apply();
    }

    public void clearLastUpdateDate() {
        saveLastUpdateDate(null);
    }

    public String getMajorInfoTitle() {
        return sharedPreferences.getString(KEY_INFO_MAJOR_TITLE, null);
    }

    public void saveMajorInfoTitle(String value) {
        sharedPreferences.edit().putString(KEY_INFO_MAJOR_TITLE, value).apply();
    }

    public void saveMinorInfoTitle(String value) {
        sharedPreferences.edit()
                .putString(KEY_INFO_MINOR_TITLE, value)
                .apply();
    }

    public void saveExpLevels(List<Long> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Long level = list.get(i);
            builder.append(level);

            if (i != list.size() - 1) {
                builder.append(";");
            }
        }

        String expLevelAll = builder.toString();
        sharedPreferences.edit().putString(FILTER_EXP_LEVEL, expLevelAll).apply();
    }

    public List<Long> getExpLevels() {
        String expLevel = sharedPreferences.getString(FILTER_EXP_LEVEL, "");
        List<Long> expLevelList = new ArrayList<>();

        if (!TextUtils.isEmpty(expLevel)) {
            String levels[] = expLevel.split(";");
            for (String level : levels) {
                expLevelList.add(Long.valueOf(level));
            }
        }
        return expLevelList;
    }

    public List<Long> getTracks() {
        String expLevel = sharedPreferences.getString(FILTER_TRACK, "");
        List<Long> trackList = new ArrayList<>();

        if (!TextUtils.isEmpty(expLevel)) {
            String levels[] = expLevel.split(";");
            for (String level : levels) {
                trackList.add(Long.valueOf(level));
            }
        }
        return trackList;
    }

    public void saveTracks(List<Long> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Long track = list.get(i);
            builder.append(track);

            if (i != list.size() - 1) {
                builder.append(";");
            }
        }

        String trackAll = builder.toString();
        sharedPreferences.edit().putString(FILTER_TRACK, trackAll).apply();
    }

    public boolean clear() {
        return sharedPreferences.edit().clear().commit();
    }
}
