package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

import com.ls.drupalcon.model.database.AbstractEntity;
import com.ls.utils.CursorParser;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Location extends AbstractEntity<Long> {

    @SerializedName("locationId")
    private long mId;

    @SerializedName("locationName")
    private String mName;

    @SerializedName("address")
    private String mAddress;

    @SerializedName("number")
    private String mNumber;

    @SerializedName("latitude")
    private double mLat;

    @SerializedName("longitude")
    private double mLon;

    @SerializedName("order")
    private double mOrder;

    @SerializedName("deleted")
    private boolean mDeleted;

    @Override
    public ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put("_id", mId);
        result.put("location_name", mName);
        result.put("address", mAddress);
        result.put("number", mNumber);
        result.put("lat", mLat);
        result.put("lon", mLon);
        result.put("_order", mOrder);
        result.put("_deleted", mDeleted);

        return result;
    }

    @Override
    public void initialize(Cursor theCursor) {
        CursorParser parser = new CursorParser(theCursor);

        mId = parser.readLong();
        mName = parser.readString();
        mAddress = parser.readString();
        mNumber = parser.readString();
        mLat = parser.readDouble();
        mLon = parser.readDouble();
        mOrder = parser.readInt();
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

    public String getAddress() {
        return (mAddress == null) ? "" : mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public static class Holder {

        @SerializedName("locations")
        private List<Location> mLocations = new ArrayList<Location>();

        public List<Location> getLocations() {
            return mLocations;
        }
    }
}
