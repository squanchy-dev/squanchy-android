package com.ls.ui.activity;

import com.google.android.gms.analytics.GoogleAnalytics;

import com.ls.drupalcon.R;
import com.ls.drupalcon.app.App;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.UpdatesManager;
import com.ls.drupalcon.model.data.Level;
import com.ls.drupalcon.model.data.Track;
import com.ls.drupalcon.model.managers.TracksManager;
import com.ls.ui.adapter.item.EventListItem;
import com.ls.ui.dialog.FilterDialog;
import com.ls.ui.dialog.IrrelevantTimezoneDialog;
import com.ls.ui.drawer.DrawerAdapter;
import com.ls.ui.drawer.DrawerManager;
import com.ls.ui.drawer.DrawerMenu;
import com.ls.ui.drawer.DrawerMenuItem;
import com.ls.utils.AnalyticsManager;
import com.ls.utils.KeyboardUtils;
import com.ls.utils.ScheduleManager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends StateActivity implements FilterDialog.OnFilterApplied {

    private DrawerManager mFrManager;
    private DrawerAdapter mAdapter;
    private String mPresentTitle;
    private int mSelectedItem = 0;
    private int mLastSelectedItem = 0;
    private boolean isIntentHandled = false;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    public FilterDialog mFilterDialog;
    public boolean mIsDrawerItemClicked;

    private UpdatesManager.DataUpdatedListener updateReceiver = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<Integer> requestIds) {
            closeFilterDialog();
            initFilterDialog();
        }
    };

    public static void startThisActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        Model.instance().getUpdatesManager().registerUpdateListener(updateReceiver);

        initToolbar();
        initNavigationDrawer();
        initNavigationDrawerList();
        initFilterDialog();

        initFragmentManager();
        if (getIntent().getExtras() != null) {
            isIntentHandled = true;
        }
        handleIntent(getIntent());
        showIrrelevantTimezoneDialogIfNeeded();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(null);
        if (!isIntentHandled) {
            handleIntent(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    protected void onDestroy() {
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateReceiver);
        AnalyticsManager.sendEvent(this, "Application", R.string.action_close);
        super.onDestroy();
    }

    @Override
    public void onNewFilterApplied() {
        mFrManager.reloadPrograms();
    }

    private void initToolbar() {
        mPresentTitle = DrawerMenu.MENU_STRING_ARRAY[0];
        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mToolbar.setTitle(mPresentTitle);
        setSupportActionBar(mToolbar);
    }

    private void initNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);

        mDrawerLayout.setDrawerListener(drawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.closeDrawers();
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                KeyboardUtils.hideKeyboard(getCurrentFocus());
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mIsDrawerItemClicked) {
                    mIsDrawerItemClicked = false;
                    changeFragment();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        drawerToggle.syncState();
    }

    private void initNavigationDrawerList() {
        List<DrawerMenuItem> menu = getNavigationDrawerItems();
        mAdapter = new DrawerAdapter(this, menu);
        mAdapter.setDrawerItemClickListener(new DrawerAdapter.OnDrawerItemClickListener() {
            @Override
            public void onDrawerItemClicked(int position) {
                onItemClick(position);
            }
        });

        ListView listView = (ListView) findViewById(R.id.leftDrawer);
        listView.addHeaderView(
                getLayoutInflater().inflate(R.layout.nav_drawer_header, null),
                null,
                false);
        listView.setAdapter(mAdapter);
    }

    public void initFilterDialog() {
        new AsyncTask<Void, Void, List<EventListItem>>() {
            @Override
            protected List<EventListItem> doInBackground(Void... params) {
                TracksManager tracksManager = Model.instance().getTracksManager();
                List<Track> trackList = tracksManager.getTracks();
                List<Level> levelList = tracksManager.getLevels();

                Collections.sort(trackList, new Comparator<Track>() {
                    @Override
                    public int compare(Track track1, Track track2) {
                        String name1 = track1.getName();
                        String name2 = track2.getName();
                        return name1.compareToIgnoreCase(name2);
                    }
                });

                String[] tracks = new String[trackList.size()];
                String[] levels = new String[levelList.size()];

                for (int i = 0; i < trackList.size(); i++) {
                    tracks[i] = trackList.get(i).getName();
                }

                for (int i = 0; i < levelList.size(); i++) {
                    levels[i] = levelList.get(i).getName();
                }
                mFilterDialog = FilterDialog.newInstance(tracks, levels);
                mFilterDialog.setData(levelList, trackList);
                return null;
            }

            @Override
            protected void onPostExecute(List<EventListItem> eventListItems) {
            }
        }.execute();
    }

    public void closeFilterDialog() {
        if (mFilterDialog != null) {
            if (mFilterDialog.isAdded()) {
                mFilterDialog.dismissAllowingStateLoss();
            }
            mFilterDialog.clearFilter();
        }
    }

    private void handleIntent(Intent intent) {
        if (intent.getExtras() != null) {
            long eventId = intent.getLongExtra(EventDetailsActivity.EXTRA_EVENT_ID, -1);
            long day = intent.getLongExtra(EventDetailsActivity.EXTRA_DAY, -1);
            redirectToDetails(eventId, day);
            isIntentHandled = false;
            new ScheduleManager(this).cancelAlarm(eventId);
        }
    }

    private void redirectToDetails(long id, long day) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID, id);
        intent.putExtra(EventDetailsActivity.EXTRA_DAY, day);
        startActivity(intent);
    }

    private void onItemClick(int position) {
        mDrawerLayout.closeDrawers();
        if (mSelectedItem == position) {
            return;
        }
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mSelectedItem = position;
        mIsDrawerItemClicked = true;
    }

    private void changeFragment() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        if (mSelectedItem == DrawerMenu.DrawerItem.About.ordinal()) {
            AboutActivity.startThisActivity(this);
            mSelectedItem = mLastSelectedItem;

        } else {
            DrawerMenuItem item = mAdapter.getItem(mSelectedItem);
            if (!item.isGroup() && mFrManager != null) {
                mFrManager.setFragment(DrawerMenu.DrawerItem.values()[mSelectedItem]);
                mPresentTitle = DrawerMenu.MENU_STRING_ARRAY[mSelectedItem];
                mToolbar.setTitle(mPresentTitle);

                mAdapter.setSelectedPos(mSelectedItem);
                mAdapter.notifyDataSetChanged();

                AnalyticsManager.sendEvent(this, mPresentTitle + " screen", R.string.action_open);
            }
        }
        mLastSelectedItem = mSelectedItem;
    }

    private void initFragmentManager() {
        mFrManager = DrawerManager.getInstance(getSupportFragmentManager(), R.id.mainFragment);
        AnalyticsManager.sendEvent(this, App.getContext().getString(R.string.Sessions) + " screen", R.string.action_open);
        mFrManager.setFragment(DrawerMenu.DrawerItem.Program);
    }

    private static List<DrawerMenuItem> getNavigationDrawerItems() {
        List<DrawerMenuItem> result = new ArrayList<DrawerMenuItem>();

        for (int i = 0; i < DrawerMenu.MENU_STRING_ARRAY.length; i++) {
            DrawerMenuItem menuItem = new DrawerMenuItem();
            String name = DrawerMenu.MENU_STRING_ARRAY[i];

            menuItem.setId(i);
            menuItem.setName(name);
            menuItem.setGroup(false);
            menuItem.setIconRes(DrawerMenu.MENU_ICON_RES[i]);
            menuItem.setSelIconRes(DrawerMenu.MENU_ICON_RES_SEL[i]);

            result.add(menuItem);
        }

        return result;
    }

     private void showIrrelevantTimezoneDialogIfNeeded() {
        if (!IrrelevantTimezoneDialog.isCurrentTimezoneRelevant()
                && IrrelevantTimezoneDialog.canPresentMessage(this)
                && !isFinishing()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(new IrrelevantTimezoneDialog(), IrrelevantTimezoneDialog.TAG);
            ft.commitAllowingStateLoss();
        }
    }
}