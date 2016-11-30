package com.connfa.model.database;

import android.content.ContentValues;
import android.database.Cursor;

public interface AbstractEntity<I> {

    I getId();

    ContentValues getContentValues();

    void initialize(Cursor theCursor);
}
