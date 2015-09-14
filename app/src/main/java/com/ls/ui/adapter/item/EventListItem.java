package com.ls.ui.adapter.item;

import com.ls.drupalconapp.model.data.Event;

/**
 * Created by Yakiv M. on 24.09.2014.
 */
public interface EventListItem {

    public static final int TYPE_TIME_RANGE = 0;
    public static final int TYPE_PROGRAM = 1;
    public static final int TYPE_BOFS = 2;
    public static final int TYPE_SOCIAL = 3;
    public static final int TYPE_SEACTION_NAME = 4;

    public int getAdapterType();

    public Event getEvent();

    public void setLast(boolean isLast);

}
