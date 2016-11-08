package com.connfa.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.connfa.model.PreferencesManager;

import java.util.Iterator;

class DatabaseHelper extends SQLiteOpenHelper {

    private final DBInfo dbInfo;
    private final Context context;

    public DatabaseHelper(Context context, DBInfo dbInfo) {
        super(context, dbInfo.getDatabaseName(), null, dbInfo.getDatabaseVersion());
        this.context = context;
        this.dbInfo = dbInfo;
    }

    @Override
    public void onCreate(SQLiteDatabase theDb) {
        Iterator<String> it = this.dbInfo.getTableCreationQueries().iterator();
        while (it.hasNext()) {
            String subQuery = it.next();
            theDb.execSQL(subQuery);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase theDb, int oldVersion, int newVersion) {
        PreferencesManager.create(context).saveLastUpdateDate(null);
        this.dbInfo.getMigrationTask().onUpgrade(theDb, oldVersion, newVersion);
    }

}
