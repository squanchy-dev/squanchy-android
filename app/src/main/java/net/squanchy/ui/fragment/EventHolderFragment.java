package net.squanchy.ui.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import net.squanchy.R;
import net.squanchy.model.Model;
import net.squanchy.model.PreferencesManager;
import net.squanchy.model.UpdatesManager;
import net.squanchy.ui.activity.HomeActivity;
import net.squanchy.ui.adapter.BaseEventDaysPagerAdapter;
import net.squanchy.ui.drawer.DrawerManager;
import net.squanchy.ui.receiver.ReceiverManager;
import net.squanchy.utils.DateUtils;

import java.util.List;

import timber.log.Timber;

public class EventHolderFragment extends Fragment {

    public static final String TAG = "ProjectsFragment";

    private static final String EXTRAS_ARG_MODE = "EXTRAS_ARG_MODE";

    private LoadDataTask loadDataTask;

    private PreferencesManager preferencesManager;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private BaseEventDaysPagerAdapter adapter;

    private DrawerManager.EventMode eventMode;

    private View layoutPlaceholder;
    private ImageView emptyImageView;
    private TextView emptyLabel;

    private boolean isFilterUsed;

    private ReceiverManager favoriteReceiver = new ReceiverManager((eventId, isFavorite) -> updateFavorites());

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
        preferencesManager = PreferencesManager.create(getActivity());
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Model.getInstance().getUpdatesManager().registerUpdateListener(updateReceiver);
        favoriteReceiver.register(getActivity());

        initData();
        initView();
        refreshData();
    }

    @Override
    public void onDestroyView() {
        Model.getInstance().getUpdatesManager().unregisterUpdateListener(updateReceiver);
        favoriteReceiver.unregister(getActivity());
        super.onDestroyView();
    }

    private UpdatesManager.DataUpdatedListener updateReceiver = requestIds -> updateData(requestIds);

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int eventPos = bundle.getInt(EXTRAS_ARG_MODE, DrawerManager.EventMode.PROGRAM.ordinal());
            eventMode = DrawerManager.EventMode.values()[eventPos];
        }
    }

    private void initView() {
        View view = getView();
        if (view == null) {
            return;
        }

        adapter = new BaseEventDaysPagerAdapter(getActivity(), getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        tabStrip = (PagerSlidingTabStrip) getView().findViewById(R.id.pager_tab_strip);
        tabStrip.setTypeface(typeface, 0);
        tabStrip.setViewPager(viewPager);

        layoutPlaceholder = view.findViewById(R.id.layout_placeholder);
        emptyLabel = (TextView) view.findViewById(R.id.text_view_placeholder);
        emptyImageView = (ImageView) view.findViewById(R.id.image_view_placeholder);

        if (eventMode == DrawerManager.EventMode.PROGRAM ||
                eventMode == DrawerManager.EventMode.BOFS ||
                eventMode == DrawerManager.EventMode.SOCIAL) {
            setHasOptionsMenu(true);
        } else {
            setHasOptionsMenu(false);
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

    private void updateFilterState(MenuItem filter) {
        isFilterUsed = false;
        List<Long> levelIds = preferencesManager.getExpLevels();
        List<Long> trackIds = preferencesManager.getTracks();

        if (!levelIds.isEmpty() || !trackIds.isEmpty()) {
            isFilterUsed = true;
        }

        if (isFilterUsed) {
            filter.setIcon(getResources().getDrawable(R.drawable.ic_filter));
        } else {
            filter.setIcon(getResources().getDrawable(R.drawable.ic_filter_empty));
        }
    }

    private void updateData(List<Integer> requestIds) {
        for (int id : requestIds) {
            int eventModePos = UpdatesManager.convertEventIdToEventModePos(id);
            if (eventModePos == eventMode.ordinal() ||
                    (eventMode == DrawerManager.EventMode.FAVORITES && isEventItem(id))) {
                refreshData();
                break;
            }
        }
    }

    private boolean isEventItem(int id) {
        return id == UpdatesManager.PROGRAMS_REQUEST_ID ||
                id == UpdatesManager.BOFS_REQUEST_ID ||
                id == UpdatesManager.SOCIALS_REQUEST_ID;
    }

    private void updateFavorites() {
        if (getView() != null) {
            if (eventMode == DrawerManager.EventMode.FAVORITES) {
                refreshData();
            }
        }
    }

    private void refreshData() {
        if (loadDataTask != null) {
            loadDataTask.cancel(true);
        }
        loadDataTask = new LoadDataTask(eventMode, loadDataCallback);
        loadDataTask.execute();
    }

    private final LoadDataTask.LoadDataTaskCallback loadDataCallback = result -> {
        if (isResumed()) {
            updateViews(result);
        }
    };

    private void updateViews(List<Long> dayList) {
        if (dayList.isEmpty()) {
            tabStrip.setVisibility(View.GONE);
            layoutPlaceholder.setVisibility(View.VISIBLE);

            if (isFilterUsed) {
                emptyImageView.setVisibility(View.GONE);
                emptyLabel.setText(getString(R.string.placeholder_no_matching_events));
            } else {
                emptyImageView.setVisibility(View.VISIBLE);

                int imageResId = 0, textResId = 0;

                switch (eventMode) {
                    case PROGRAM:
                        imageResId = R.drawable.ic_no_session;
                        textResId = R.string.placeholder_sessions;
                        break;
                    case BOFS:
                        imageResId = R.drawable.ic_no_bofs;
                        textResId = R.string.placeholder_bofs;
                        break;
                    case SOCIAL:
                        imageResId = R.drawable.ic_no_social_events;
                        textResId = R.string.placeholder_social_events;
                        break;
                    case FAVORITES:
                        imageResId = R.drawable.ic_no_my_schedule;
                        textResId = R.string.placeholder_schedule;
                        break;
                }

                emptyImageView.setImageResource(imageResId);
                emptyLabel.setText(getString(textResId));
            }
        } else {
            layoutPlaceholder.setVisibility(View.GONE);
            tabStrip.setVisibility(View.VISIBLE);
        }

        adapter.setData(dayList, eventMode);
        switchToCurrentDay(dayList);
    }

    private void switchToCurrentDay(List<Long> days) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Timber.e("Trying to switch day while not attached to an activity");
            return;
        }

        int item = 0;
        for (Long millis : days) {
            if (DateUtils.isToday(activity, millis) || DateUtils.isAfterCurrentDate(millis)) {
                viewPager.setCurrentItem(item);
                return;
            }
            item++;
        }
    }

}
