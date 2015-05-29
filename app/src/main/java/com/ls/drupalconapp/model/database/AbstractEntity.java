package com.ls.drupalconapp.model.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * @author Lemberg
 * @version 3.00
 */

public abstract class AbstractEntity<IdClass> {

    public AbstractEntity() {
    }

    public abstract IdClass getId();


    public abstract ContentValues getContentValues();


    public abstract void initialize(Cursor theCursor);
}
