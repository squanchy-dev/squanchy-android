package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

import com.ls.drupalcon.model.database.AbstractEntity;
import com.ls.utils.CursorParser;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class POI extends AbstractEntity<Long> {

    @SerializedName("poiId")
    private long mId;

    @SerializedName("poiName")
    private String mName;

    @SerializedName("poiDescription")
    private String mDescription;

    @SerializedName("poiImageURL")
    private String mImageURL;

    @SerializedName("poiDetailURL")
    private String mDetailURL;

    @SerializedName("order")
    private double mOrder;

    @SerializedName("deleted")
    private boolean mDeleted;

    @Override
    public Long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String getDetailURL() {
        return mDetailURL;
    }

    public void setDetailURL(String mDetailURL) {
        this.mDetailURL = mDetailURL;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public double getOrder() {
        return mOrder;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put("_id", mId);
        result.put("poi_name", mName);
        result.put("poi_description", mDescription);
        result.put("image_url", mImageURL);
        result.put("detail_url", mDetailURL);
        result.put("_order", mOrder);
        result.put("_deleted", mDeleted);

        return result;
    }

    @Override
    public void initialize(Cursor theCursor) {
        CursorParser parser = new CursorParser(theCursor);

        mId = parser.readLong();
        mName = parser.readString();
        mDescription = parser.readString();
        mImageURL = parser.readString();
        mDetailURL = parser.readString();
        mOrder = parser.readDouble();
        mDeleted = parser.readBoolean();

    }

    public static class Holder {

        @SerializedName("poi")
        private List<POI> mPOIs = new ArrayList<POI>();

        public List<POI> getPOIs() {
            return mPOIs;
        }
    }
}
