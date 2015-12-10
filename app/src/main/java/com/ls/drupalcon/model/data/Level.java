package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.database.AbstractEntity;
import com.ls.utils.CursorParser;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Level extends AbstractEntity<Long>{

    public static final int BEGINNER = 1;
    public static final int INTERMEDIATE = 2;
    public static final int ADVANCED = 3;

    @SerializedName("levelId")
    private long mId;

    @SerializedName("levelName")
    private String mName;

    @SerializedName("order")
    private double mOrder;

    @SerializedName("deleted")
    private boolean mDeleted;

    @Override
    public ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put("_id", mId);
        result.put("level_name", mName);
        result.put("_order", mOrder);
        result.put("_deleted", mDeleted);

        return result;
    }

    @Override
    public void initialize(Cursor theCursor) {
        CursorParser parser = new CursorParser(theCursor);

        mId = parser.readLong();
        mName = parser.readString();
        mOrder = parser.readDouble();
        mDeleted = parser.readBoolean();
    }

    @Override
    public Long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getOrder() {
        return mOrder;
    }

    public void setOrder(double order) {
        mOrder = order;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public static class Holder {

        @SerializedName("levels")
        private List<Level> mLevels = new ArrayList<Level>();

        public List<Level> getLevels() {
            return mLevels;
        }
    }

    public static int getIcon(long typeId) {
        switch ((int) typeId) {

            case Level.BEGINNER:
                return R.drawable.ic_experience_beginner;

            case Level.INTERMEDIATE:
                return R.drawable.ic_experience_intermediate;

            case Level.ADVANCED:
                return R.drawable.ic_experience_advanced;

            default:
                return 0;
        }
    }

    public static int getIcon(String levelName) {
        switch (levelName) {

            case "Beginner":
                return R.drawable.ic_experience_beginner;

            case "Intermediate":
                return R.drawable.ic_experience_intermediate;

            case "Advanced":
                return R.drawable.ic_experience_advanced;

            default:
                return 0;
        }
    }
}
