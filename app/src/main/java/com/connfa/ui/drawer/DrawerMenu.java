package com.connfa.ui.drawer;

import com.connfa.R;

public class DrawerMenu {

    public enum DrawerItem {
        PROGRAM,
        BOFS,
        SOCIAL,
        SOCIAL_MEDIA,
        FAVORITES,
        FLOOR_PLAN,
        LOCATION,
        SPEAKERS,
        ABOUT
    }

    public static final int[] MENU_ICON_RES = {
            R.drawable.menu_icon_program,
            R.drawable.menu_icon_bofs,
            R.drawable.menu_icon_social,
            R.drawable.menu_icon_social_media,
            R.drawable.menu_icon_my_schedule,
            R.drawable.menu_icon_floor_plan,
            R.drawable.menu_icon_location,
            R.drawable.menu_icon_speakers,
            R.drawable.menu_icon_about
    };

    public static final int[] MENU_ICON_RES_SEL = {
            R.drawable.menu_icon_program_sel,
            R.drawable.menu_icon_bofs_sel,
            R.drawable.menu_icon_social_sel,
            R.drawable.menu_icon_social_media_sel,
            R.drawable.menu_icon_my_schedule_sel,
            R.drawable.menu_icon_floor_plan_sel,
            R.drawable.menu_icon_location_sel,
            R.drawable.menu_icon_speakers_sel,
            R.drawable.menu_icon_about_sel
    };

    public static final int[] MENU_STRING_RES_ARRAY = {
            R.string.Sessions,
            R.string.bofs,
            R.string.social_events,
            R.string.social_media,
            R.string.my_schedule,
            R.string.floor_plan,
            R.string.location,
            R.string.speakers,
            R.string.about

    };
}
