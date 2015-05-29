package com.ls.drupalconapp.ui.drawer;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ls.drupalconapp.ui.fragment.EventHolderFragment;
import com.ls.drupalconapp.ui.fragment.LocationFragment;
import com.ls.drupalconapp.ui.fragment.SpeakersListFragment;

import org.jetbrains.annotations.NotNull;

public class DrawerManager {

	public static enum EventMode {Program, Bofs, Social, Speakers, Favorites, Location, About}

	private FragmentManager fragmentManager;
	private int fragmentHolderId;

	public static DrawerManager getInstance(FragmentManager theFragmentManager, int theMainFragmentId) {
		return new DrawerManager(theFragmentManager, theMainFragmentId);
	}

	private DrawerManager(FragmentManager theFragmentManager, int theMainFragmentId) {
		this.fragmentManager = theFragmentManager;
		this.fragmentHolderId = theMainFragmentId;
	}

	public void setFragment(@NotNull EventMode mode) {
		Fragment fragment;
		String fragmentTag = null;

		switch (mode) {
			case Program:
				fragment = EventHolderFragment.newInstance(EventMode.Program.ordinal());
				fragmentTag = EventHolderFragment.TAG;
				break;

			case Bofs:
				fragment = EventHolderFragment.newInstance(EventMode.Bofs.ordinal());
				fragmentTag = EventHolderFragment.TAG;
				break;

			case Social:
				fragment = EventHolderFragment.newInstance(EventMode.Social.ordinal());
				fragmentTag = EventHolderFragment.TAG;
				break;

			case Speakers:
				fragment = new SpeakersListFragment();
				fragmentTag = SpeakersListFragment.TAG;
				break;

			case Favorites:
				fragment = EventHolderFragment.newInstance(EventMode.Favorites.ordinal());
				fragmentTag = EventHolderFragment.TAG;
				break;

			case Location:
				fragment = new LocationFragment();
				fragmentTag = LocationFragment.TAG;
				break;
			default:
				fragment = EventHolderFragment.newInstance(EventMode.Program.ordinal());
		}
		fragmentManager.beginTransaction().replace(fragmentHolderId, fragment, fragmentTag).commit();
	}

	public void reloadPrograms() {
		Fragment fragment;
		fragment = EventHolderFragment.newInstance(EventMode.Program.ordinal());
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(fragmentHolderId, fragment, EventHolderFragment.TAG);
		ft.commitAllowingStateLoss();
	}
}
