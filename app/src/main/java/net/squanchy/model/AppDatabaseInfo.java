package net.squanchy.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.squanchy.R;
import net.squanchy.model.dao.EventDao;
import net.squanchy.model.dao.InfoDao;
import net.squanchy.model.dao.LevelDao;
import net.squanchy.model.dao.LocationDao;
import net.squanchy.model.dao.POIDao;
import net.squanchy.model.dao.SpeakerDao;
import net.squanchy.model.dao.TrackDao;
import net.squanchy.model.dao.TypeDao;
import net.squanchy.model.database.DBInfo;
import net.squanchy.model.database.IMigrationTask;
import net.squanchy.utils.FileUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppDatabaseInfo implements DBInfo, IMigrationTask {

    public static final String DATABASE_NAME = "drupal_db";
    private static final int DATABASE_VERSION = 11;

    private static final String TABLE_EVENT_SPEAKER = "table_event_and_speaker";
    private static final String TABLE_FAVORITE_EVENTS = "table_favorite_events";
    private static final String DELETE_TABLE_IF_EXIST = "DROP TABLE IF EXISTS ";

    private Context context;

    public AppDatabaseInfo(Context context) {
        this.context = context;
    }

    @Override
    public List<String> getTableCreationQueries() {
        return generateCreationQueryList();
    }

    @Override
    public List<String> getTableNameList() {
        List<String> dbTableNameList = new ArrayList<>();
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
            PreferencesManager.create(context).clearLastUpdateDate();
            List<String> dbDropList = generateDropQueryList();
            List<String> dbSchemaQueryList = generateCreationQueryList();

            for (String query : dbDropList) {
                db.execSQL(query);
            }
            FileUtils.deleteDataStorageDirectory(context);

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
        theList.add(context.getResources().getString(theId));
    }
}
