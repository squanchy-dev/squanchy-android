package com.ls.ui.adapter.item;

import com.ls.drupalcon.model.data.Event;

public interface EventListItem {

    int TYPE_TIME_RANGE = 0;
    int TYPE_PROGRAM = 1;
    int TYPE_BOFS = 2;
    int TYPE_SOCIAL = 3;
    int TYPE_SECTION_NAME = 4;

    int getAdapterType();

    Event getEvent();

    void setLast(boolean isLast);

}
