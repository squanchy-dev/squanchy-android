package com.ls.drupalconapp.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yakiv M. on 19.09.2014.
 */
public class PreferencesManager {

	private static final String TIME_ZONE_ID = "TIME_ZONE_ID";
	private static final String PREF_NAME = "com.ls.drupalconapp.model.MAIN_PREFERENCES";
	private static final String KEY_LAST_UPDATE_DATE = "KEY_LAST_UPDATE_DATE";
	private static final String KEY_ABOUT = "KEY_ABOUT";
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

    public void saveTimeZoneId(String timeZoneId) {
        mPref.edit()
                .putString(TIME_ZONE_ID, timeZoneId)
                .commit();
    }

    public String getTimeZoneId() {
        return mPref.getString(TIME_ZONE_ID, "");
    }

	public void saveLastUpdateDate(String value) {
		mPref.edit()
				.putString(KEY_LAST_UPDATE_DATE, value)
				.commit();
	}

	public String getLastUpdateDate() {
		return mPref.getString(KEY_LAST_UPDATE_DATE, "");
	}

	public void saveAbout(String value) {
		mPref.edit()
				.putString(KEY_ABOUT, value)
				.commit();
	}

	public String getAbout() {
		return mPref.getString(KEY_ABOUT, null);
	}

	public void saveMajorInfoTitle(String value) {
		mPref.edit()
				.putString(KEY_INFO_MAJOR_TITLE, value)
				.commit();
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
		mPref.edit()
				.remove(key)
				.commit();
	}

	public boolean clear() {
		return mPref.edit()
				.clear()
				.commit();
	}

	public void saveExpLevel(List<Long> list) {
		SharedPreferences.Editor editor = mPref.edit();
		editor.putInt(FILTER_EXP_LEVEL + "_size", list.size());

		for (int i = 0; i < list.size(); i++) {
			editor.putLong(FILTER_EXP_LEVEL + "_" + i, list.get(i));
		}
		editor.commit();
	}

	public List<Long> loadExpLevel() {
		int size = mPref.getInt(FILTER_EXP_LEVEL + "_size", 0);
		List<Long> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			list.add(mPref.getLong(FILTER_EXP_LEVEL + "_" + i, 0));
		}
		return list;
	}

	public void saveTrack(List<Long> list) {
		SharedPreferences.Editor editor = mPref.edit();
		editor.putInt(FILTER_TRACK + "_size", list.size());
		for (int i = 0; i < list.size(); i++) {
			editor.putLong(FILTER_TRACK + "_" + i, list.get(i));
		}
		editor.commit();
	}

	public List<Long> loadTracks() {
		int size = mPref.getInt(FILTER_TRACK + "_size", 0);
		List<Long> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			list.add(mPref.getLong(FILTER_TRACK + "_" + i, 0));
		}
		return list;
	}
}
