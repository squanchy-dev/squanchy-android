package com.connfa.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.connfa.R;
import com.connfa.model.dao.EventDao;
import com.connfa.model.dao.InfoDao;
import com.connfa.model.dao.LevelDao;
import com.connfa.model.dao.LocationDao;
import com.connfa.model.dao.POIDao;
import com.connfa.model.dao.SpeakerDao;
import com.connfa.model.dao.TrackDao;
import com.connfa.model.dao.TypeDao;
import com.connfa.model.database.DBInfo;
import com.connfa.model.database.IMigrationTask;
import com.connfa.utils.FileUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppDatabaseInfo implements DBInfo, IMigrationTask {

    public static final String DATABASE_NAME = "drupal_db";
    private static final int DATABASE_VERSION = 11;

    private static final String TABLE_EVENT_SPEAKER = "table_event_and_speaker";
    private static final String TABLE_FAVORITE_EVENTS = "table_favorite_events";
    private static final String DELETE_TABLE_IF_EXIST = "DROP TABLE IF EXISTS ";

    private Context mContext;

    public AppDatabaseInfo(Context context) {
        this.mContext = context;
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            PreferencesManager.getInstance().saveLastUpdateDate(null);
            List<String> dbDropList = generateDropQueryList();
            List<String> dbSchemaQueryList = generateCreationQueryList();

            for (String query : dbDropList) {
                db.execSQL(query);
            }
            FileUtils.deleteDataStorageDirectory(mContext);

            for (String query : dbSchemaQueryList) {
                db.execSQL(query);
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
        addStringWithIdToList(dbSchemaQueryList, R.string.create_table_floor_plans);
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
        theList.add(mContext.getResources().getString(theId));
    }
}
