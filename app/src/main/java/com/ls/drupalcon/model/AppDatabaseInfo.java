package com.ls.drupalcon.model;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.dao.EventDao;
import com.ls.drupalcon.model.dao.InfoDao;
import com.ls.drupalcon.model.dao.LevelDao;
import com.ls.drupalcon.model.dao.LocationDao;
import com.ls.drupalcon.model.dao.POIDao;
import com.ls.drupalcon.model.dao.SpeakerDao;
import com.ls.drupalcon.model.dao.TrackDao;
import com.ls.drupalcon.model.dao.TypeDao;
import com.ls.drupalcon.model.database.DBInfo;
import com.ls.drupalcon.model.database.IMigrationTask;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppDatabaseInfo implements DBInfo, IMigrationTask {

    public static final String DATABASE_NAME = "drupal_db";
    public static final int DATABASE_VERSION = 9;

    private static final String TABLE_EVENT_SPEAKER = "table_event_and_speaker";
    private static final String TABLE_FAVORITE_EVENTS = "table_favorite_events";
    private static final String DELETE_TABLE_IF_EXIST = "DROP TABLE IF EXISTS ";

    private Resources mResources;

    public AppDatabaseInfo(Context theContext) {
        this.mResources = theContext.getResources();
    }

    @Override
    public List<String> getTableCreationQueries() {
        return generateCreationQueryList();
    }

    @Override
    public List<String> getTableNameList() {
        List<String> dbTableNameList = new ArrayList<String>();
        dbTableNameList.add(TypeDao.TABLE_NAME);
        dbTableNameList.add(SpeakerDao.TABLE_NAME);
        dbTableNameList.add(LevelDao.TABLE_NAME);
        dbTableNameList.add(TrackDao.TABLE_NAME);
        dbTableNameList.add(LocationDao.TABLE_NAME);
        dbTableNameList.add(EventDao.TABLE_NAME);
        dbTableNameList.add(POIDao.TABLE_NAME);
        dbTableNameList.add(InfoDao.TABLE_NAME);
        dbTableNameList.add(TABLE_EVENT_SPEAKER);
        dbTableNameList.add(TABLE_FAVORITE_EVENTS);
        return dbTableNameList;
    }

    @Override
    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    public IMigrationTask getMigrationTask() {
        return this;
    }

    @Override
    public void onUpgrade(SQLiteDatabase theDb, int oldVersion, int newVersion) {
        if (newVersion > oldVersion){
            PreferencesManager.getInstance().saveLastUpdateDate(null);
            List<String> dbDropList = generateDropQueryList();
            List<String> dbSchemaQueryList = generateCreationQueryList();

            for (String query : dbDropList) {
                theDb.execSQL(query);
            }

            for (String query : dbSchemaQueryList) {
                theDb.execSQL(query);
            }

        }
    }

    private List<String> generateCreationQueryList() {
        List<String> dbSchemaQueryList = new LinkedList<>();
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_type);
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_speaker);
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_level);
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_track);
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_location);
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_event);
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_event_and_speaker);
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_favorite_events);
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_info);
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_poi);
        return dbSchemaQueryList;
    }

    private List<String> generateDropQueryList() {
        List<String> dbDropList = new ArrayList<>();
        for (String tableName : getTableNameList()) {
            String query = DELETE_TABLE_IF_EXIST + tableName;
            dbDropList.add(query);
        }
        return dbDropList;
    }

    private void addStringWithIdToList(List<String> theList, int theId) {
        theList.add(this.mResources.getString(theId));
    }
}
