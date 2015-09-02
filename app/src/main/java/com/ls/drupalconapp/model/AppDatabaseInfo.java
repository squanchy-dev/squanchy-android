package com.ls.drupalconapp.model;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.dao.EventDao;
import com.ls.drupalconapp.model.dao.InfoDao;
import com.ls.drupalconapp.model.dao.LevelDao;
import com.ls.drupalconapp.model.dao.LocationDao;
import com.ls.drupalconapp.model.dao.POIDao;
import com.ls.drupalconapp.model.dao.SpeakerDao;
import com.ls.drupalconapp.model.dao.TrackDao;
import com.ls.drupalconapp.model.dao.TypeDao;
import com.ls.drupalconapp.model.database.DBInfo;
import com.ls.drupalconapp.model.database.IMigrationTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppDatabaseInfo implements DBInfo, IMigrationTask {

    public static final String DATABASE_NAME = "drupal_db";
    public static final int DATABASE_VERSION = 8;

    private Resources resources;

    public AppDatabaseInfo(Context theContext) {
        this.resources = theContext.getResources();
    }

    @Override
    public List<String> getTableCreationQueries() {
        List<String> dbSchemaQueryList = generateCreationQueryList();
        return dbSchemaQueryList;
    }

    private void addStringWithIdToList(List<String> theList, int theId) {
        theList.add(this.resources.getString(theId));
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
        dbTableNameList.add("table_event_and_speaker");
        dbTableNameList.add("table_favorite_events");
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
            List<String> dbDropList = generateDropQueryList();

            List<String> dbSchemaQueryList = generateCreationQueryList();

            for (String query : dbDropList){
                theDb.execSQL(query);
            }

            for (String query : dbSchemaQueryList){
                theDb.execSQL(query);
            }

        }
    }

    private List<String> generateCreationQueryList(){
        List<String> dbSchemaQueryList = new LinkedList<String>();
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

    private List<String> generateDropQueryList(){
        List<String> dbDropList = new ArrayList<String>();
        addStringWithIdToList(dbDropList, R.string.delete_table_type);
        addStringWithIdToList(dbDropList, R.string.delete_table_speaker);
        addStringWithIdToList(dbDropList, R.string.delete_table_level);
        addStringWithIdToList(dbDropList, R.string.delete_table_track);
        addStringWithIdToList(dbDropList, R.string.delete_table_location);
        addStringWithIdToList(dbDropList, R.string.delete_table_house_plans);
        addStringWithIdToList(dbDropList, R.string.delete_table_info);
        addStringWithIdToList(dbDropList, R.string.delete_table_poi);
        addStringWithIdToList(dbDropList, R.string.delete_table_event);
        addStringWithIdToList(dbDropList, R.string.delete_table_event_and_speaker);
        addStringWithIdToList(dbDropList, R.string.delete_table_favorite_events);
        return dbDropList;
    }
}
