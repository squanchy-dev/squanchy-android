package com.ls.utils;

import android.database.Cursor;

public class CursorStringParser {

    private Cursor mCursor;

    public CursorStringParser(Cursor cursor) {
        mCursor = cursor;
    }

    public int readInt(String column) {
        return mCursor.getInt(mCursor.getColumnIndex(column));
    }

    public long readLong(String column) {
        return mCursor.getLong(mCursor.getColumnIndex(column));
    }

    public String readString(String column) {
        return mCursor.getString(mCursor.getColumnIndex(column));
    }

    public boolean readBoolean(String column) {
        return mCursor.getInt(mCursor.getColumnIndex(column)) != 0;
    }

    public float readFloat(String column) {
        return mCursor.getFloat(mCursor.getColumnIndex(column));
    }

    public double readDouble(String column) {
        return mCursor.getDouble(mCursor.getColumnIndex(column));
    }

    public boolean moveToFirst(String column) {
        return mCursor.moveToFirst();
    }

    public boolean moveToLast(String column) {
        return mCursor.moveToLast();
    }

    public boolean moveToNext(String column) {
        return mCursor.moveToNext();
    }

    public void close() {
        mCursor.close();
    }

    public Cursor getWrapedCursor() {
        return mCursor;
    }
}
