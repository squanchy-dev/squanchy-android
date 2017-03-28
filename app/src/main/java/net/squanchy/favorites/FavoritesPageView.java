package net.squanchy.favorites;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class FavoritesPageView extends CoordinatorLayout implements LifecycleView {

    private View progressBar;
    private Disposable subscription;
    private ScheduleService service;
    private Navigator navigate;
    private Analytics analytics;
    private FavoritesListView favoritesListView;
    private TextView emptyView;

    public FavoritesPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FavoritesPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = findViewById(R.id.progressbar);
        favoritesListView = (FavoritesListView) findViewById(R.id.favorites_list);
        emptyView = (TextView) findViewById(R.id.empty_view);

        setupToolbar();

        if (!isInEditMode()) {
            Activity activity = unwrapToActivityContext(getContext());
            FavoritesComponent component = FavoritesInjector.obtain(activity);
            service = component.service();
            navigate = component.navigator();
            analytics = component.analytics();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_favorites);
        toolbar.inflateMenu(R.menu.search_icon);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                navigate.toSearch();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onStart() {
        subscription = Observable.combineLatest(service.schedule(true), service.currentUserIsSignedIn(), toScheduleAndLoggedInStuff())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> updateWith(schedule, this::onEventClicked));
    }

    private BiFunction<Schedule, Boolean, ScheduledAndSignedIn> toScheduleAndLoggedInStuff() {
        return ScheduledAndSignedIn::new;
    }

    private void onEventClicked(Event event) {
        analytics.trackItemSelected(ContentType.FAVORITES_ITEM, event.id());
        navigate.toEventDetails(event.id());
    }

    @Override
    public void onStop() {
        subscription.dispose();
    }

    private void updateWith(ScheduledAndSignedIn scheduledAndSignedIn, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        if (scheduledAndSignedIn.hasPages()) {
            updateWith(scheduledAndSignedIn.schedule, listener);
        } else {
            if (scheduledAndSignedIn.signedIn) {
                promptToSign();
            } else {
                promptToFavorite();
            }
        }
    }

    private void updateWith(Schedule schedule, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        favoritesListView.updateWith(schedule, listener);
        progressBar.setVisibility(GONE);
        emptyView.setVisibility(GONE);
    }

    public void promptToSign() {
        favoritesListView.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        emptyView.setVisibility(VISIBLE);
        emptyView.setText(R.string.no_favorites_label);
        emptyView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.illustration_signed_in, 0, 0);
    }

    private void promptToFavorite() {
        favoritesListView.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        emptyView.setVisibility(VISIBLE);
        emptyView.setText(R.string.signin_to_favorite);
        emptyView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.illustration_prompt_to_favorite, 0, 0);
    }

    private static class ScheduledAndSignedIn {

        final Schedule schedule;
        final boolean signedIn;

        private ScheduledAndSignedIn(Schedule schedule, boolean signedIn) {
            this.schedule = schedule;
            this.signedIn = signedIn;
        }

        boolean hasPages() {
            return schedule.hasPages();
        }
    }
}
