package com.ls.utils;

import android.database.Cursor;

public class CursorParser {

    private int mIndex;
    private Cursor mCursor;

    public CursorParser(Cursor cursor) {
        mCursor = cursor;
        mIndex = -1;
    }

    public int readInt() {
        mIndex++;
        return mCursor.getInt(mIndex);
    }

    public long readLong() {
        mIndex++;
        return mCursor.getLong(mIndex);
    }

    public String readString() {
        mIndex++;
        return mCursor.getString(mIndex);
    }

    public boolean readBoolean() {
        mIndex++;
        return mCursor.getInt(mIndex) != 0;
    }

    public float readFloat() {
        mIndex++;
        return mCursor.getFloat(mIndex);
    }

    public double readDouble() {
        mIndex++;
        return mCursor.getDouble(mIndex);
    }

    public boolean moveToFirst() {
        mIndex = -1;
        return mCursor.moveToFirst();
    }

    public boolean moveToLast() {
        mIndex = -1;
        return mCursor.moveToLast();
    }

    public boolean moveToNext() {
        mIndex = -1;
        return mCursor.moveToNext();
    }

    public void close() {
        mIndex = -1;
        mCursor.close();
    }

    public int getIndex() {
        return mIndex;
    }

    public Cursor getWrapedCursor() {
        return mCursor;
    }
}
