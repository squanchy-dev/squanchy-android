package com.ls.drupalconapp.model.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yakiv M. on 19.09.2014.
 */
public class UpdateDate {

    @SerializedName("Last-Modified")
    private String mTime;

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }
}
