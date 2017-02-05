package net.squanchy.model.database;

import android.database.sqlite.SQLiteDatabase;

public interface IMigrationTask {
    void onUpgrade(SQLiteDatabase theDb, int oldVersion, int newVersion);
}
