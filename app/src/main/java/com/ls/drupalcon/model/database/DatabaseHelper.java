package com.ls.drupalcon.model.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ls.drupalcon.model.PreferencesManager;

import java.util.Iterator;

public class DatabaseHelper
        extends SQLiteOpenHelper {

    private DBInfo dbInfo;

    public DatabaseHelper(Context theContext, DBInfo theDbInfo) {
        super(theContext, theDbInfo.getDatabaseName(), null, theDbInfo.getDatabaseVersion());

        this.dbInfo = theDbInfo;
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
        PreferencesManager.getInstance().saveLastUpdateDate(null);
        this.dbInfo.getMigrationTask().onUpgrade(theDb, oldVersion, newVersion);
    }

}
