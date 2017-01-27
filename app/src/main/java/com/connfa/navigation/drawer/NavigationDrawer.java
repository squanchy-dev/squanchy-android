package com.connfa.navigation.drawer;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class NavigationDrawer<T extends View> {

    private static final boolean ANIMATE = true;

    private final DrawerLayout drawerLayout;
    private final T drawerContents;

    public NavigationDrawer(DrawerLayout drawerLayout, T drawerContents) {
        this.drawerLayout = drawerLayout;
        this.drawerContents = drawerContents;
    }

    public boolean isOpen() {
        return drawerLayout.isDrawerOpen(drawerContents);
    }

    public void close() {
        drawerLayout.closeDrawer(drawerContents, ANIMATE);
    }
}
