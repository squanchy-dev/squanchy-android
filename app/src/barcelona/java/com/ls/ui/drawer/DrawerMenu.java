package com.ls.ui.drawer;

import com.ls.drupalcon.R;
import com.ls.drupalcon.app.App;

public class DrawerMenu {

    public enum DrawerItem {Program, Bofs, Social, Speakers, Favorites, Location, About}

    public static final int[] MENU_ICON_RES = {
            R.drawable.menu_icon_program,
            R.drawable.menu_icon_bofs,
            R.drawable.menu_icon_social,
            R.drawable.menu_icon_speakers,
            R.drawable.menu_icon_my_schedule,
            R.drawable.menu_icon_location,
            R.drawable.menu_icon_about
    };

    public static final int[] MENU_ICON_RES_SEL = {
            R.drawable.menu_icon_program_sel,
            R.drawable.menu_icon_bofs_sel,
            R.drawable.menu_icon_social_sel,
            R.drawable.menu_icon_speakers_sel,
            R.drawable.menu_icon_my_schedule_sel,
            R.drawable.menu_icon_location_sel,
            R.drawable.menu_icon_about_sel
    };

    public static final String[] MENU_STRING_ARRAY = {
            App.getContext().getString(R.string.Sessions),
            App.getContext().getString(R.string.bofs),
            App.getContext().getString(R.string.social_events),
            App.getContext().getString(R.string.speakers),
            App.getContext().getString(R.string.my_schedule),
            App.getContext().getString(R.string.location),
            App.getContext().getString(R.string.about)
    };
}
