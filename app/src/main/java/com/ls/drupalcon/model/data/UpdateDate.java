package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateDate {

    @SerializedName("Last-Modified")
    private String mTime;

    public List<Integer> idsForUpdate;

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public List<Integer> getIdsForUpdate() {
        return idsForUpdate;
    }
}
