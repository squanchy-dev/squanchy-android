package com.ls.ui.adapter.item;

import com.ls.drupalcon.model.data.Event;

public class HeaderItem implements EventListItem {

    private String mTitle;

    public HeaderItem(String title) {
        this.mTitle = title;
    }

    @Override
    public int getAdapterType() {
        return TYPE_SECTION_NAME;
    }

    @Override
    public Event getEvent() {
        return null;
    }

    @Override
    public void setLast(boolean isLast) {

    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getTitle() {
        return mTitle;
    }
}
