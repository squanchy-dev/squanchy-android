package com.ls.ui.adapter.item;

public class HeaderItem extends EventListItem {

    private String mTitle;

    public HeaderItem(String title) {
        this.mTitle = title;
    }

    @Override
    public int getAdapterType() {
        return TYPE_SECTION_NAME;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getTitle() {
        return mTitle;
    }
}
