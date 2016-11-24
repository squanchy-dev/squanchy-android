package com.connfa.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.connfa.ui.drawer.DrawerManager;
import com.connfa.ui.fragment.EventFragment;
import com.connfa.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseEventDaysPagerAdapter extends FragmentStatePagerAdapter {

    private final Context context;
    private DrawerManager.EventMode mEventMode;

    private List<Long> mDays;

    public BaseEventDaysPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        mDays = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Long date = getDate(position);
        Fragment fragment = EventFragment.newInstance(DrawerManager.EventMode.PROGRAM.ordinal(), date);

        switch (mEventMode) {
            case PROGRAM:
                fragment = EventFragment.newInstance(DrawerManager.EventMode.PROGRAM.ordinal(), date);
                break;
            case BOFS:
                fragment = EventFragment.newInstance(DrawerManager.EventMode.BOFS.ordinal(), date);
                break;
            case SOCIAL:
                fragment = EventFragment.newInstance(DrawerManager.EventMode.SOCIAL.ordinal(), date);
                break;
            case FAVORITES:
                fragment = EventFragment.newInstance(DrawerManager.EventMode.FAVORITES.ordinal(), date);
                break;
        }
        return fragment;
    }

    public void setData(List<Long> eventDays, DrawerManager.EventMode eventMode) {
        mDays.clear();
        mDays.addAll(eventDays);
        mEventMode = eventMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mDays.size();
    }

    public Long getDate(int position) {
        return mDays.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return DateUtils.getWeekNameAndDate(context, getDate(position));
    }
}
