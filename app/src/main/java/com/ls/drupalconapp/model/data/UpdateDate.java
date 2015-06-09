package com.ls.drupalconapp.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yakiv M. on 19.09.2014.
 */
public class UpdateDate {

    @SerializedName("Last-Modified")
    private String mTime;

    public List<Integer> idsForUpdate;

    public String getTime() {
        return mTime;
    }


    public List<Integer> getIdsForUpdate() {
        return idsForUpdate;
    }
}
