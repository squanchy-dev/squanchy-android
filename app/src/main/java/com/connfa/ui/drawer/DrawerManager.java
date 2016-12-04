package com.connfa.ui.drawer;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.connfa.ui.fragment.AboutFragment;
import com.connfa.ui.fragment.EventHolderFragment;
import com.connfa.ui.fragment.FloorPlanFragment;
import com.connfa.ui.fragment.LocationFragment;
import com.connfa.ui.fragment.SocialMediaFragment;
import com.connfa.ui.fragment.SpeakersListFragment;

import org.jetbrains.annotations.NotNull;

public class DrawerManager {

    public enum EventMode {
        PROGRAM,
        BOFS,
        SOCIAL,
        SPEAKERS,
        FAVORITES,
        LOCATION,
        ABOUT
    }

    private FragmentManager fragmentManager;
    private int fragmentHolderId;
    private EventMode currentEventMode;

    public DrawerManager(FragmentManager fragmentManager, @IdRes int fragmentContainerResId) {
        this.fragmentManager = fragmentManager;
        this.fragmentHolderId = fragmentContainerResId;
    }

    public void setFragment(@NotNull DrawerMenu.DrawerItem drawerItem) {
        Fragment fragment;
        String fragmentTag = null;

        switch (drawerItem) {
            case PROGRAM:
                fragment = EventHolderFragment.newInstance(DrawerMenu.DrawerItem.PROGRAM.ordinal());
                fragmentTag = EventHolderFragment.TAG;
                break;

            case BOFS:
                fragment = EventHolderFragment.newInstance(DrawerMenu.DrawerItem.BOFS.ordinal());
                fragmentTag = EventHolderFragment.TAG;
                break;

            case SOCIAL:
                fragment = EventHolderFragment.newInstance(DrawerMenu.DrawerItem.SOCIAL.ordinal());
                fragmentTag = EventHolderFragment.TAG;
                break;

            case FAVORITES:
                fragment = EventHolderFragment.newInstance(DrawerMenu.DrawerItem.FAVORITES.ordinal());
                fragmentTag = EventHolderFragment.TAG;
                break;

            case SPEAKERS:
                fragment = new SpeakersListFragment();
                fragmentTag = SpeakersListFragment.TAG;
                break;

            case FLOOR_PLAN:
                fragment = new FloorPlanFragment();
                fragmentTag = FloorPlanFragment.TAG;
                break;

            case LOCATION:
                fragment = new LocationFragment();
                fragmentTag = LocationFragment.TAG;
                break;
            case SOCIAL_MEDIA:
                fragment = new SocialMediaFragment();
                fragmentTag = SocialMediaFragment.TAG;
                break;
            case ABOUT:
                fragment = new AboutFragment();
                fragmentTag = SocialMediaFragment.TAG;
                break;
            default:
                fragment = EventHolderFragment.newInstance(EventMode.PROGRAM.ordinal());
        }
        fragmentManager.beginTransaction().replace(fragmentHolderId, fragment, fragmentTag).commit();
    }

    public void reloadPrograms(@NotNull DrawerMenu.DrawerItem drawerItem) {
        Fragment fragment;
        switch (drawerItem) {
            case PROGRAM:
                fragment = EventHolderFragment.newInstance(DrawerMenu.DrawerItem.PROGRAM.ordinal());
                break;

            case BOFS:
                fragment = EventHolderFragment.newInstance(DrawerMenu.DrawerItem.BOFS.ordinal());
                break;

            case SOCIAL:
                fragment = EventHolderFragment.newInstance(DrawerMenu.DrawerItem.SOCIAL.ordinal());
                break;
            default:
                fragment = EventHolderFragment.newInstance(EventMode.PROGRAM.ordinal());
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(fragmentHolderId, fragment, EventHolderFragment.TAG);
        ft.commitAllowingStateLoss();
    }
}
