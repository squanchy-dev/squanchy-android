package com.ls.drupalconapp.model.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yakiv M. on 19.09.2014.
 */
public class About {

    @SerializedName("aboutHTML")
    private String mContent;

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
