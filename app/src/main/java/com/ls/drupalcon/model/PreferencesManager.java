package com.ls.drupalcon.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class PreferencesManager {

    private static final String PREF_NAME = "com.ls.drupalconapp.model.MAIN_PREFERENCES";

    private static final String TIME_ZONE = "TIME_ZONE";
    private static final String KEY_LAST_UPDATE_DATE = "KEY_LAST_UPDATE_DATE";
    private static final String KEY_INFO_MAJOR_TITLE = "KEY_INFO_MAJOR_TITLE";
    private static final String KEY_INFO_MINOR_TITLE = "KEY_INFO_MINOR_TITLE";
    private static final String FILTER_EXP_LEVEL = "FILTER_EXP_LEVEL";
    private static final String FILTER_TRACK = "FILTER_TRACK ";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void saveTimeZone(String timeZone) {
        mPref.edit().putString(TIME_ZONE, timeZone).commit();
    }

    public String getTimeZone() {
        return mPref.getString(TIME_ZONE, "");
    }

    public TimeZone getServerTimeZoneObject() {
        String timezoneId = getTimeZone();
        return TimeZone.getTimeZone(timezoneId);
    }

    public void saveLastUpdateDate(String value) {
        mPref.edit().putString(KEY_LAST_UPDATE_DATE, value).commit();
    }

    public String getLastUpdateDate() {
        return mPref.getString(KEY_LAST_UPDATE_DATE, "");
    }

    public void saveMajorInfoTitle(String value) {
        mPref.edit().putString(KEY_INFO_MAJOR_TITLE, value).commit();
    }

    public String getMajorInfoTitle() {
        return mPref.getString(KEY_INFO_MAJOR_TITLE, null);
    }

    public void saveMinorInfoTitle(String value) {
        mPref.edit()
                .putString(KEY_INFO_MINOR_TITLE, value)
                .commit();
    }

    public String getMinorInfoTitle() {
        return mPref.getString(KEY_INFO_MINOR_TITLE, null);
    }

    public void remove(String key) {
        mPref.edit().remove(key).commit();
    }

    public boolean clear() {
        return mPref.edit().clear().commit();
    }

    public void saveExpLevel(List<Long> list) {
        SharedPreferences.Editor editor = mPref.edit();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Long level = list.get(i);
            builder.append(level);

            if (i != list.size() - 1) {
                builder.append(";");
            }
        }

        String expLevelAll = builder.toString();
        editor.putString(FILTER_EXP_LEVEL, expLevelAll);
        editor.commit();
    }

    public List<Long> loadExpLevel() {
        String expLevel = mPref.getString(FILTER_EXP_LEVEL, "");
        List<Long> expLevelList = new ArrayList<>();

        if (!TextUtils.isEmpty(expLevel)) {
            String levels[] = expLevel.split(";");
            for (String level : levels) {
                expLevelList.add(Long.valueOf(level));
            }
        }
        return expLevelList;
    }

    public void saveTracks(List<Long> list) {
        SharedPreferences.Editor editor = mPref.edit();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Long track = list.get(i);
            builder.append(track);

            if (i != list.size() - 1) {
                builder.append(";");
            }
        }

        String trackAll = builder.toString();
        editor.putString(FILTER_TRACK, trackAll);
        editor.commit();
    }

    public List<Long> loadTracks() {
        String expLevel = mPref.getString(FILTER_TRACK, "");
        List<Long> trackList = new ArrayList<>();

        if (!TextUtils.isEmpty(expLevel)) {
            String levels[] = expLevel.split(";");
            for (String level : levels) {
                trackList.add(Long.valueOf(level));
            }
        }
        return trackList;
    }
}
