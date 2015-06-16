package com.ls.drupalconapp.ui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.ui.drawer.DrawerManager;
import com.ls.drupalconapp.ui.fragment.EventFragment;

import java.util.ArrayList;
import java.util.List;

public class BaseEventDaysPagerAdapter extends FragmentStatePagerAdapter{

    private DrawerManager.EventMode mEventMode;

    private List<Long> mDays;

    public BaseEventDaysPagerAdapter(FragmentManager fm) {
        super(fm);
        mDays = new ArrayList<>();
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public Long getDate(int position){
        return mDays.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Long date = getDate(position);
        return Event.format(date);
    }
}
