package com.ls.drawer;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.app.App;

import java.util.ArrayList;
import java.util.List;

public class DrawerMenu {

    private static final int[] MENU_ICON_RES = {
            R.drawable.menu_icon_program,
//            R.drawable.menu_icon_bofs,
//            R.drawable.menu_icon_social,
            R.drawable.menu_icon_speakers,
            R.drawable.menu_icon_my_schedule,
            R.drawable.menu_icon_location,
            R.drawable.menu_icon_about
    };

    private static final int[] MENU_ICON_RES_SEL = {
            R.drawable.menu_icon_program_sel,
//            R.drawable.menu_icon_bofs_sel,
//            R.drawable.menu_icon_social_sel,
            R.drawable.menu_icon_speakers_sel,
            R.drawable.menu_icon_my_schedule_sel,
            R.drawable.menu_icon_location_sel,
            R.drawable.menu_icon_about_sel
    };

    public static final String[] MENU_STRING_ARRAY = {
            App.getContext().getString(R.string.Schedule),
//            App.getContext().getString(R.string.bofs),
//            App.getContext().getString(R.string.social_events),
            App.getContext().getString(R.string.speakers),
            App.getContext().getString(R.string.my_schedule),
            App.getContext().getString(R.string.location),
            App.getContext().getString(R.string.about)
    };


    public enum DrawerItem {Program, Speakers, Favorites, Location, About}

    public static List<DrawerMenuItem> getNavigationDrawerItems() {
        List<DrawerMenuItem> result = new ArrayList<DrawerMenuItem>();

        for(int i = 0; i < MENU_STRING_ARRAY.length; i++){
            DrawerMenuItem menuItem = new DrawerMenuItem();
            String name = MENU_STRING_ARRAY[i];

            menuItem.setId(i);
            menuItem.setName(name);
            menuItem.setGroup(false);
            menuItem.setIconRes(MENU_ICON_RES[i]);
            menuItem.setSelIconRes(MENU_ICON_RES_SEL[i]);

            result.add(menuItem);
        }

        return result;
    }
}
