package com.ls.ui.fragment;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.ls.ui.drawer.DrawerManager;
import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.UpdatesManager;
import com.ls.drupalcon.model.managers.BofsManager;
import com.ls.drupalcon.model.managers.FavoriteManager;
import com.ls.drupalcon.model.managers.ProgramManager;
import com.ls.drupalcon.model.managers.SocialManager;
import com.ls.ui.activity.HomeActivity;
import com.ls.ui.adapter.BaseEventDaysPagerAdapter;
import com.ls.ui.dialog.FilterDialog;
import com.ls.ui.receiver.ReceiverManager;
import com.ls.utils.ApplicationConfig;
import com.ls.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EventHolderFragment extends Fragment {

    public static final String TAG = "ProjectsFragment";
    private static final String EXTRAS_ARG_MODE = "EXTRAS_ARG_MODE";

    private ViewPager mViewPager;
    private PagerSlidingTabStrip mPagerTabs;
    private BaseEventDaysPagerAdapter mAdapter;

    private DrawerManager.EventMode mEventMode;
    private View mTxtNoEvents;
    private View mNoFavorites;

    private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<Integer> requestIds) {
            performDataUpdate(requestIds);
        }
    };

    private ReceiverManager receiverManager = new ReceiverManager(
            new ReceiverManager.FavoriteUpdatedListener() {
                @Override
                public void onFavoriteUpdated(long eventId, boolean isFavorite) {
                    performFavoriteUpdate();
                }
            });

    public static EventHolderFragment newInstance(int modePos) {
        EventHolderFragment fragment = new EventHolderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRAS_ARG_MODE, modePos);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_holder_event, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filter, menu);

        MenuItem filter = menu.findItem(R.id.actionFilter);
        if (filter != null) {
            updateFilterState(filter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionFilter:
                showFilter();
                break;
        }
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Model.instance().getUpdatesManager().registerUpdateListener(updateListener);
        receiverManager.register(getActivity());

        initData();
        initView();
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
        receiverManager.unregister(getActivity());
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int eventPos = bundle.getInt(EXTRAS_ARG_MODE, DrawerManager.EventMode.Program.ordinal());
            mEventMode = DrawerManager.EventMode.values()[eventPos];
        }
    }

    private void initView() {
        View view = getView();
        if (view == null) {
            return;
        }

        mAdapter = new BaseEventDaysPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mAdapter);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        mPagerTabs = (PagerSlidingTabStrip) getView().findViewById(R.id.pager_tab_strip);
        mPagerTabs.setTypeface(typeface, 0);
        mPagerTabs.setViewPager(mViewPager);

        mTxtNoEvents = view.findViewById(R.id.txtNoEvents);
        mNoFavorites = view.findViewById(R.id.emptyIcon);

        if (!ApplicationConfig.DISPLAY_FILTER || mEventMode != DrawerManager.EventMode.Program) {
            setHasOptionsMenu(false);
        } else {
            setHasOptionsMenu(true);
        }
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateViewsUI(getDayList());
            }
        }).start();
    }

    private void updateViewsUI(final List<Long> dayList) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateViews(dayList);
                }
            });
        }
    }

    private List<Long> getDayList() {
        List<Long> dayList = new ArrayList<>();
        switch (mEventMode) {
            case Bofs:
                BofsManager bofsManager = Model.instance().getBofsManager();
                dayList.addAll(bofsManager.getBofsDays());
                break;
            case Social:
                SocialManager socialManager = Model.instance().getSocialManager();
                dayList.addAll(socialManager.getSocialsDays());
                break;
            case Favorites:
                FavoriteManager favoriteManager = Model.instance().getFavoriteManager();
                dayList.addAll(favoriteManager.getFavoriteEventDays());
                break;
            default:
                ProgramManager programManager = Model.instance().getProgramManager();
                dayList.addAll(programManager.getProgramDays());
                break;
        }
        return dayList;
    }


    private void updateViews(List<Long> dayList) {
        if (dayList.isEmpty()) {
            mPagerTabs.setVisibility(View.GONE);
            if (mEventMode == DrawerManager.EventMode.Favorites) {
                mNoFavorites.setVisibility(View.VISIBLE);
            } else {
                mTxtNoEvents.setVisibility(View.VISIBLE);
            }

        } else {
            mNoFavorites.setVisibility(View.GONE);
            mTxtNoEvents.setVisibility(View.GONE);
            mPagerTabs.setVisibility(View.VISIBLE);
        }

        mAdapter.setData(dayList, mEventMode);
        switchToCurrentDay(dayList);
    }

    private void switchToCurrentDay(List<Long> days) {
        int item = 0;
        for (Long millis : days) {
            if (DateUtils.getInstance().isToday(millis)) {
                mViewPager.setCurrentItem(item);
                break;
            }
            item++;
        }
    }

    private void showFilter() {
        Activity activity = getActivity();
        if (activity instanceof HomeActivity) {

            if (!((HomeActivity) activity).mFilterDialog.isAdded()) {
                ((HomeActivity) activity).mFilterDialog.show(getActivity().getSupportFragmentManager(), "filter");
            }
        }
    }

    private void updateFilterState(@NotNull MenuItem filter) {
        boolean isFilterUsed = false;
        List<Long> levelIds = PreferencesManager.getInstance().loadExpLevel();
        List<Long> trackIds = PreferencesManager.getInstance().loadTracks();

        if (!levelIds.isEmpty() || !trackIds.isEmpty()) {
            isFilterUsed = true;
        }

        if (isFilterUsed) {
            filter.setIcon(getResources().getDrawable(R.drawable.ic_filter));
        } else {
            filter.setIcon(getResources().getDrawable(R.drawable.ic_filter_empty));
        }
    }

    //TODO refactor
    private void performDataUpdate(List<Integer> requestIds) {
        Activity activity = getActivity();
        if (activity instanceof HomeActivity) {
            ((HomeActivity) activity).initFilterDialog();

            FilterDialog filterDialog = ((HomeActivity) activity).mFilterDialog;
            if (filterDialog != null) {
                filterDialog.clearFilter();

                if (((HomeActivity) activity).mFilterDialog.isAdded()) {
                    ((HomeActivity) activity).mFilterDialog.dismissAllowingStateLoss();
                }
            }
        }

        for (int id : requestIds) {
            int eventModePos = UpdatesManager.convertEventIdToEventModePos(id);
            if (eventModePos == mEventMode.ordinal()) {
                loadData();
                break;
            }
        }
    }

    private void performFavoriteUpdate() {
        if (getView() != null) {
            if (mEventMode == DrawerManager.EventMode.Favorites) {
                loadData();
            }
        }
    }
}
