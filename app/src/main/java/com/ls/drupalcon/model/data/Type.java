package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.database.AbstractEntity;
import com.ls.utils.CursorParser;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Type extends AbstractEntity<Long> {

    public static final int NONE = 0;
    public static final int SPEACH = 1;
    public static final int SPEACH_OF_DAY = 2;
    public static final int COFFEBREAK = 3;
    public static final int LUNCH = 4;
    public static final int ALL_DAY = 5;
    public static final int GROUP = 6;
    public static final int WALKING = 7;
    public static final int REGISTRATION = 8;
    public static final int FREE_SLOT = 9;

    @SerializedName("typeId")
    private long mId;

    @SerializedName("typeName")
    private String mName;

    @SerializedName("typeIconURL")
    private String mIconUrl;

    @SerializedName("order")
    private double mOrder;

    @SerializedName("deleted")
    private boolean mDeleted;

    @Override
    public ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put("_id", mId);
        result.put("name", mName);
        result.put("type_icon_url", mIconUrl);
        result.put("_order", mOrder);
        result.put("_deleted", mDeleted);

        return result;
    }

    @Override
    public void initialize(Cursor theCursor) {
        CursorParser parser = new CursorParser(theCursor);

        mId = parser.readLong();
        mName = parser.readString();
        mIconUrl = parser.readString();
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

    public void setDeleted(boolean mDeleted) {
        this.mDeleted = mDeleted;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public static int getIcon(long typeId) {
        switch ((int) typeId) {

            case Type.SPEACH_OF_DAY:
                return R.drawable.ic_program_speach_of_the_day;

            case Type.GROUP:
                return R.drawable.ic_program_group_photo;

            case Type.WALKING:
                return R.drawable.ic_program_walking_break;

            case Type.COFFEBREAK:
                return R.drawable.ic_program_coffe_break;

            case Type.LUNCH:
                return R.drawable.ic_program_lanch;

            case Type.REGISTRATION:
                return R.drawable.ic_program_check_in;

            case Type.ALL_DAY:
                return R.drawable.ic_program_24_hour;

            default:
                return 0;
        }
    }

    public static class Holder {

        @SerializedName("types")
        private List<Type> mTypes = new ArrayList<Type>();

        public List<Type> getTypes() {
            return mTypes;
        }
    }

}
