package com.connfa.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

class DatabaseFacade implements ILAPIDBFacade {

    private DBInfo dbInfo;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final Context context;
    private int openCounter = 0;

    DatabaseFacade(Context context, DBInfo dbInfo) {
        this.context = context;
        this.dbInfo = dbInfo;
    }

    public void beginTransactions() {
        db.beginTransaction();
    }

    public void setTransactionSuccesfull() {
        db.setTransactionSuccessful();
    }

    public void endTransactions() {
        db.endTransaction();
    }

    public synchronized DatabaseFacade open()
            throws SQLException {
        if (this.openCounter == 0) {
            this.dbHelper = new DatabaseHelper(this.context, this.dbInfo);
            this.db = this.dbHelper.getWritableDatabase();
        }

        this.openCounter++;

        return this;
    }

    public synchronized void close() {
        if (openCounter == 1) {
            this.db.close();
            this.dbHelper.close();
        }

        openCounter--;
    }

    public boolean containsRecord(String table, String whereClause, String[] columns) {

        Cursor cursor = db.query(true,
                table,
                columns,
                whereClause,// selection
                null,// selection args
                null,// groupBy
                null,// having
                null,// order by
                null);// limit

        boolean containsRecord = cursor.getCount() > 0;

        cursor.close();

        return containsRecord;
    }

    public boolean containsRecord(String table, String whereClause, String[] selectionArgs,
                                  String[] columns) {
        boolean result;

        Cursor cursor = db.query(true,
                table,
                columns,
                whereClause,// selection
                selectionArgs,// selection args
                null,// groupBy
                null,// having
                null,// order by
                null);// limit

        result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

    public Cursor query(String query, String[] selectionArgs) {
        return db.rawQuery(query, selectionArgs);
    }

    public long save(String table, ContentValues values) {
        return db.insert(table, null, values);
    }

    /**
     * @return number of rows affected
     */
    public int update(String table, String whereClause, ContentValues values) {
        return db.update(table, values, whereClause, null);
    }

    public int update(String table, String whereClause, String[] whereArgs,
                      ContentValues values) {
        return db.update(table, values, whereClause, whereArgs);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return db.delete(table, whereClause, whereArgs);
    }

    public int delete(String table, String whereClause) {
        return db.delete(table, whereClause, null);
    }

    public void insert(String sqlQuery) {
        db.execSQL(sqlQuery);
    }

    public void execSQL(String sqlQuery, Object[] bindArgs) {
        db.execSQL(sqlQuery, bindArgs);
    }

    public int clearTable(String table) {
        return this.delete(table, null, null);
    }

    public Cursor getAllRecords(String table, String[] columns, String selection) {
        return db.query(true,
                table,
                columns,
                selection,// selection
                null,// selection args
                null,// groupBy
                null,// having
                null,// order by
                null);
    }

    public Cursor getAllRecords(String table, String[] columns, String selection, String[] selectionArgs) {
        return db.query(true,
                table,
                columns,
                selection,// selection
                selectionArgs,// selection args
                null,// groupBy
                null,// having
                null,// order by
                null);
    }

    public String getQuery(int resId) {
        return context.getString(resId);
    }
}
