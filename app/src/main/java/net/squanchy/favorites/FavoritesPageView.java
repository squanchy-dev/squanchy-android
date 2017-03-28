package net.squanchy.favorites;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import net.squanchy.R;
import net.squanchy.analytics.Analytics;
import net.squanchy.analytics.ContentType;
import net.squanchy.favorites.view.FavoritesListView;
import net.squanchy.home.LifecycleView;
import net.squanchy.navigation.Navigator;
import net.squanchy.schedule.ScheduleService;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.view.ScheduleViewPagerAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class FavoritesPageView extends CoordinatorLayout implements LifecycleView {

    private View progressBar;
    private Disposable subscription;
    private ScheduleService service;
    private Navigator navigate;
    private Analytics analytics;
    private FavoritesListView favoritesListView;

    public FavoritesPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FavoritesPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Activity activity = unwrapToActivityContext(getContext());
        FavoritesComponent component = FavoritesInjector.obtain(activity);
        service = component.service();
        navigate = component.navigator();
        analytics = component.analytics();

        progressBar = findViewById(R.id.progressbar);

        favoritesListView = (FavoritesListView) findViewById(R.id.favorites_list);

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_favorites);
        toolbar.inflateMenu(R.menu.homepage);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_search:
                    navigate.toSearch();
                    return true;
                case R.id.action_settings:
                    navigate.toSettings();
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public void onStart() {
        subscription = service.schedule(true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> updateWith(schedule, this::onEventClicked));
    }

    private void onEventClicked(Event event) {
        analytics.trackItemSelected(ContentType.FAVORITES_ITEM, event.id());
        navigate.toEventDetails(event.id());
    }

    @Override
    public void onStop() {
        subscription.dispose();
    }

    public void updateWith(Schedule schedule, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        favoritesListView.updateWith(schedule, listener);
        progressBar.setVisibility(GONE);
    }
}
