package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

import com.ls.drupalcon.model.database.AbstractEntity;
import com.ls.utils.CursorParser;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Speaker extends AbstractEntity<Long> implements Parcelable, Comparable<Speaker> {

    @SerializedName("speakerId")
    private long mId;

    @SerializedName("firstName")
    private String mFirstName;

    @SerializedName("lastName")
    private String mLastName;

    @SerializedName("avatarImageURL")
    private String mAvatarImageUrl;

    @SerializedName("organizationName")
    private String mOrganization;

    @SerializedName("jobTitle")
    private String mJobTitle;

    @SerializedName("characteristic")
    private String mCharacteristic;

    @SerializedName("twitterName")
    private String mTwitterName;

    @SerializedName("webSite")
    private String mWebSite;

    @SerializedName("order")
    private double mOrder;

    @SerializedName("deleted")
    private boolean mDeleted;


    public Speaker() {
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put("_id", mId);
        result.put("first_name", mFirstName);
        result.put("last_name", mLastName);
        result.put("avatar_image_url", mAvatarImageUrl);
        result.put("organization", mOrganization);
        result.put("job_title", mJobTitle);
        result.put("charact", mCharacteristic);
        result.put("twitter_name", mTwitterName);
        result.put("web_site", mWebSite);
        result.put("_order", mOrder);
        result.put("_deleted", mDeleted);

        return result;
    }

    @Override
    public void initialize(Cursor theCursor) {
        CursorParser parser = new CursorParser(theCursor);

        mId = parser.readLong();
        mFirstName = parser.readString();
        mLastName = parser.readString();
        mAvatarImageUrl = parser.readString();
        mOrganization = parser.readString();
        mJobTitle = parser.readString();
        mCharacteristic = parser.readString();
        mTwitterName = parser.readString();
        mWebSite = parser.readString();
        mOrder = parser.readDouble();
        mDeleted = parser.readBoolean();
    }

    public static final Parcelable.Creator<Speaker> CREATOR = new Parcelable.Creator<Speaker>() {

        @Override
        public Speaker createFromParcel(Parcel in) {
            return new Speaker(in);
        }

        @Override
        public Speaker[] newArray(int size) {
            return new Speaker[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mFirstName);
        parcel.writeString(mLastName);
        parcel.writeString(mAvatarImageUrl);
        parcel.writeString(mOrganization);
        parcel.writeString(mJobTitle);
        parcel.writeString(mCharacteristic);
        parcel.writeString(mTwitterName);
        parcel.writeString(mWebSite);
        parcel.writeDouble(mOrder);
        parcel.writeInt(fromBoolean(mDeleted));
    }

    public Speaker(Parcel parcel) {
        mId = parcel.readLong();
        mFirstName = parcel.readString();
        mLastName = parcel.readString();
        mAvatarImageUrl = parcel.readString();
        mOrganization = parcel.readString();
        mJobTitle = parcel.readString();
        mCharacteristic = parcel.readString();
        mTwitterName = parcel.readString();
        mWebSite = parcel.readString();
        mOrder = parcel.readDouble();
        mDeleted = toBoolean(parcel.readInt());
    }

    private boolean toBoolean(int num) {
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }

    private int fromBoolean(boolean bl) {
        if (bl) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(Speaker speaker) {
        return (mFirstName + mLastName).compareTo(speaker.mFirstName + speaker.mLastName);
    }

    @Override
    public Long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getAvatarImageUrl() {
        return mAvatarImageUrl;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        mAvatarImageUrl = avatarImageUrl;
    }

    public String getOrganization() {
        return mOrganization;
    }

    public void setOrganization(String organization) {
        mOrganization = organization;
    }

    public String getJobTitle() {
        return mJobTitle;
    }

    public void setJobTitle(String jobTitle) {
        mJobTitle = jobTitle;
    }

    public String getCharact() {
        return mCharacteristic;
    }

    public void setCharact(String charact) {
        mCharacteristic = charact;
    }

    public String getTwitterName() {
        return mTwitterName;
    }

    public void setTwitterName(String twitterName) {
        mTwitterName = twitterName;
    }

    public String getWebSite() {
        return mWebSite;
    }

    public void setWebSite(String webSite) {
        mWebSite = webSite;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public static class Holder {

        @SerializedName("speakers")
        private List<Speaker> mSpeakers = new ArrayList<Speaker>();

        public List<Speaker> getSpeakers() {
            return mSpeakers;
        }
    }
}
