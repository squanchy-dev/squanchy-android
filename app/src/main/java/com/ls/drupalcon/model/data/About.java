package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

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
