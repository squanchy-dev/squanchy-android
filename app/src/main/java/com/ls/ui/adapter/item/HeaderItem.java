package com.ls.ui.adapter.item;

import com.ls.drupalconapp.model.data.Event;

/**
 * Created by Yakiv M. on 24.09.2014.
 */
public class HeaderItem implements EventListItem {

    private String mTitle;

    public HeaderItem(String title){
        this.mTitle = title;
    }

    @Override
    public int getAdapterType() {
        return TYPE_SEACTION_NAME;
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
