package com.ls.drupalconapp.ui.adapter;


import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.ui.drawer.DrawerManager;
import com.ls.drupalconapp.ui.fragment.EventFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseEventDaysPagerAdapter extends FragmentStatePagerAdapter{

    private Map<Integer, Fragment> mCurrentFragments = new LinkedHashMap<Integer, Fragment>();
    private DrawerManager.EventMode mEventMode;

    private final List<Long> mDays = new ArrayList<Long>();

    public BaseEventDaysPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Long date = getDate(position);
		Fragment fragment = EventFragment.newInstance(DrawerManager.EventMode.Program.ordinal(), date);

		switch (mEventMode) {
			case Program:
				fragment = EventFragment.newInstance(DrawerManager.EventMode.Program.ordinal(), date);
				break;
			case Bofs:
				fragment = EventFragment.newInstance(DrawerManager.EventMode.Bofs.ordinal(), date);
				break;
			case Social:
				fragment = EventFragment.newInstance(DrawerManager.EventMode.Social.ordinal(), date);
				break;
			case Favorites:
				fragment = EventFragment.newInstance(DrawerManager.EventMode.Favorites.ordinal(), date);
				break;
		}
		return fragment;
    }

	public void setData(List<Long> eventDays, DrawerManager.EventMode eventMode) {
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mCurrentFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mCurrentFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Long getDate(int position){
        return mDays.get(position);
    }

    public Map<Integer, Fragment> getCurrentFragments() {
        return mCurrentFragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Long date = getDate(position);
        return Event.format(date);
    }
}
