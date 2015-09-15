package com.ls.drupalcon.model.database;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class AbstractEntity<IdClass> {

    public AbstractEntity() {
    }

    public abstract IdClass getId();


    public abstract ContentValues getContentValues();


    public abstract void initialize(Cursor theCursor);
}
