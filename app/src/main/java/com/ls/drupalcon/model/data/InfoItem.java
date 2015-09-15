package com.ls.drupalcon.model.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.ls.drupalcon.model.database.AbstractEntity;
import com.ls.utils.CursorParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InfoItem extends AbstractEntity<Long>{

    @SerializedName("infoId")
    private long mId;

    @SerializedName("infoTitle")
    private String mTitle;

    @SerializedName("html")
    private String mContent;

    @SerializedName("order")
    private double mOrder;

    @SerializedName("deleted")
    private boolean mDeleted;

    @Override
    public Long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public double getOrder() {
        return mOrder;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put("_id", mId);
        result.put("info_title", mTitle);
        result.put("info_content", mContent);
        result.put("_order", mOrder);
        result.put("_deleted", mDeleted);

        return result;
    }

    @Override
    public void initialize(Cursor theCursor) {
        CursorParser parser = new CursorParser(theCursor);

        mId = parser.readLong();
        mTitle = parser.readString();
        mContent = parser.readString();
        mOrder = parser.readDouble();
        mDeleted = parser.readBoolean();
    }

    public static class General  {

        @SerializedName("title")
        private HashMap <String, String> mTitles = new HashMap<String, String>();

        public String getMajorTitle(){
            return mTitles.get("titleMajor");
        }

        public String getMinorTitle(){
            return mTitles.get("titleMinor");
        }

        @SerializedName("info")
        private List<InfoItem> mInfo = new ArrayList<InfoItem>();

        public List<InfoItem> getInfo(){
            return mInfo;
        }


    }

}
