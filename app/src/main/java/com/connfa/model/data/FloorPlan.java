package com.connfa.model.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.connfa.model.database.AbstractEntity;
import com.connfa.utils.CursorStringParser;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 09.06.2016.
 */
public class FloorPlan implements AbstractEntity<String>, Comparable<FloorPlan> {
    public final static String COLUMN_ID = "_id";
    private final static String COLUMN_NAME = "_name";
    private final static String COLUMN_IMAGE_URL = "_image_url";
    private final static String COLUMN_ORDER = "_order";
    private final static String COLUMN_DELETED = "_deleted";

    @SerializedName("floorPlanId")
    private String mId;
    @SerializedName("floorPlanName")
    private String mName;
    @SerializedName("floorPlanImageURL")
    private String mImageURL;
    @SerializedName("deleted")
    private boolean mDeleted;
    @SerializedName("order")
    private double mOrder;

    public String getName() {
        return mName;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public double getOrder() {
        return mOrder;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put(COLUMN_ID, mId);
        result.put(COLUMN_NAME, mName);
        result.put(COLUMN_IMAGE_URL, mImageURL);
        result.put(COLUMN_DELETED, mDeleted);
        result.put(COLUMN_ORDER, mOrder);
        return result;
    }

    @Override
    public void initialize(Cursor cursor) {
        CursorStringParser parser = new CursorStringParser(cursor);
        mId = parser.readString(COLUMN_ID);
        mName = parser.readString(COLUMN_NAME);
        mImageURL = parser.readString(COLUMN_IMAGE_URL);
        mDeleted = parser.readBoolean(COLUMN_DELETED);
        mOrder = parser.readDouble(COLUMN_ORDER);
    }

    @Override
    public int compareTo(@NotNull FloorPlan event) {

        int result;
        if (mOrder == event.mOrder) {
            result = 0;
        } else if (mOrder > event.mOrder) {
            result = 1;
        } else {
            result = -1;
        }

        return result;
    }

    public String getFilePath() {
        return "FloorImages" + File.separator + this.getName() + "-" + this.getId();
    }

    public static class Holder {

        @SerializedName("floorPlans")
        private List<FloorPlan> floorPlans = new ArrayList<>();

        public List<FloorPlan> getFloorPlans() {
            return floorPlans;
        }
    }

    @Override
    public String toString() {
        return "FloorPlan{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mImageURL='" + mImageURL + '\'' +
                ", mDeleted=" + mDeleted +
                ", mOrder=" + mOrder +
                "} " + super.toString();
    }
}
