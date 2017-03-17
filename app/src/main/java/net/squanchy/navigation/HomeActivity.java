package net.squanchy.navigation;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.HashMap;
import java.util.Map;

import net.squanchy.R;
import net.squanchy.analytics.Analytics;
import net.squanchy.analytics.ContentType;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.proximity.ProximityEvent;
import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.proximity.injection.ProximityService;
import net.squanchy.support.lang.Optional;
import net.squanchy.support.widget.InterceptingBottomNavigationView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class HomeActivity extends TypefaceStyleableActivity {

    private static final String STATE_KEY_SELECTED_PAGE_INDEX = "HomeActivity.selected_page_index";
    private static final boolean PROXIMITY_SERVICE_RADAR_NOT_STARTED = false;

    private final Map<BottomNavigationSection, View> pageViews = new HashMap<>(4);

    private int pageFadeDurationMillis;

    private BottomNavigationSection currentSection;
    private InterceptingBottomNavigationView bottomNavigationView;
    private ViewGroup pageContainer;
    private ProximityService proximityService;
    private Analytics analytics;
    private RemoteConfig remoteConfig;
    private FirebaseAuthService authService;
    private CompositeDisposable subscriptions;

    private boolean proximityServiceRadarStarted = PROXIMITY_SERVICE_RADAR_NOT_STARTED;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pageFadeDurationMillis = getResources().getInteger(android.R.integer.config_shortAnimTime);

        pageContainer = (ViewGroup) findViewById(R.id.page_container);
        collectPageViewsInto(pageViews);

        bottomNavigationView = (InterceptingBottomNavigationView) findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        BottomNavigationSection selectedPage = getSelectedSectionOrDefault(Optional.fromNullable(savedInstanceState));
        selectInitialPage(selectedPage);

        HomeComponent homeComponent = HomeInjector.obtain(this);
        analytics = homeComponent.analytics();
        remoteConfig = homeComponent.remoteConfig();
        authService = homeComponent.authService();
        proximityService = homeComponent.service();
        subscriptions = new CompositeDisposable();
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectInitialPage(currentSection);

        // TODO do something useful with this once we can
        remoteConfig.proximityServicesEnabled()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(enabled -> {
                    if (enabled) {
                        proximityServiceRadarStarted = true;
                        proximityService.startRadar();
                    }

                    subscriptions.add(
                            proximityService.observeProximityEvents()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::handleProximityEvent));
                });

        subscriptions.add(authService.signInAnonymously().subscribe());
    }

    private void handleProximityEvent(ProximityEvent proximityEvent) {
        // TODO do something with the event, like showing feedback or opening an event detail
    }

    private void collectPageViewsInto(Map<BottomNavigationSection, View> pageViews) {
        pageViews.put(BottomNavigationSection.SCHEDULE, pageContainer.findViewById(R.id.schedule_content_root));
        pageViews.put(BottomNavigationSection.FAVORITES, pageContainer.findViewById(R.id.favorites_content_root));
        pageViews.put(BottomNavigationSection.TWEETS, pageContainer.findViewById(R.id.tweets_content_root));
        pageViews.put(BottomNavigationSection.VENUE_INFO, pageContainer.findViewById(R.id.venue_content_root));
    }

    private void setupBottomNavigation(InterceptingBottomNavigationView bottomNavigationView) {
        BottomNavigationHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setRevealDurationMillis(pageFadeDurationMillis);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_schedule:
                            selectPage(BottomNavigationSection.SCHEDULE);
                            break;
                        case R.id.action_favorites:
                            selectPage(BottomNavigationSection.FAVORITES);
                            break;
                        case R.id.action_tweets:
                            selectPage(BottomNavigationSection.TWEETS);
                            break;
                        case R.id.action_venue:
                            selectPage(BottomNavigationSection.VENUE_INFO);
                            break;
                        default:
                            throw new IndexOutOfBoundsException("Unsupported navigation item ID: " + item.getItemId());
                    }
                    return true;
                }
        );
    }

    private BottomNavigationSection getSelectedSectionOrDefault(Optional<Bundle> savedInstanceState) {
        int selectedPageIndex = savedInstanceState.or(new Bundle())
                .getInt(STATE_KEY_SELECTED_PAGE_INDEX, BottomNavigationSection.SCHEDULE.ordinal());
        return BottomNavigationSection.values()[selectedPageIndex];
    }

    private void selectInitialPage(BottomNavigationSection section) {
        swapPageTo(section);
        bottomNavigationView.cancelTransitions();
        bottomNavigationView.selectItemAt(section.ordinal());

        Resources.Theme theme = getThemeFor(section);
        bottomNavigationView.setBackgroundColor(getColorFromTheme(theme, android.support.design.R.attr.colorPrimary));
        getWindow().setStatusBarColor(getColorFromTheme(theme, android.R.attr.statusBarColor));

        currentSection = section;
    }

    private void selectPage(BottomNavigationSection section) {
        if (section == currentSection) {
            return;
        }

        Fade transition = new Fade();
        transition.setDuration(pageFadeDurationMillis);
        TransitionManager.beginDelayedTransition(pageContainer, transition);

        swapPageTo(section);

        Resources.Theme theme = getThemeFor(section);
        animateStatusBarColorTo(getColorFromTheme(theme, android.R.attr.statusBarColor));
        bottomNavigationView.setColorProvider(() -> getColorFromTheme(theme, android.support.design.R.attr.colorPrimary));

        currentSection = section;

        trackPageSelection(section);
    }

    private void swapPageTo(BottomNavigationSection section) {
        if (currentSection != null) {
            pageViews.get(currentSection).setVisibility(View.INVISIBLE);
        }
        pageViews.get(section).setVisibility(View.VISIBLE);
    }

    private Resources.Theme getThemeFor(BottomNavigationSection section) {
        Resources.Theme theme = getResources().newTheme();
        theme.setTo(getTheme());
        theme.applyStyle(section.theme(), true);
        return theme;
    }

    @ColorInt
    private int getColorFromTheme(Resources.Theme theme, @AttrRes int attributeId) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attributeId, typedValue, true);
        return typedValue.data;
    }

    private void animateStatusBarColorTo(@ColorInt int color) {
        Window window = getWindow();
        int currentStatusBarColor = window.getStatusBarColor();

        animateColor(currentStatusBarColor, color, animation -> window.setStatusBarColor((int) animation.getAnimatedValue()));
    }

    private void animateColor(@ColorInt int currentColor, @ColorInt int targetColor, ValueAnimator.AnimatorUpdateListener listener) {
        ValueAnimator animator = ValueAnimator.ofArgb(currentColor, targetColor).setDuration(pageFadeDurationMillis);
        animator.addUpdateListener(listener);
        animator.start();
    }

    private void trackPageSelection(BottomNavigationSection section) {
        analytics.trackItemSelected(ContentType.NAVIGATION_ITEM, section.name());
        analytics.trackPageView(this, section.name());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_KEY_SELECTED_PAGE_INDEX, currentSection.ordinal());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (proximityServiceRadarStarted) {
            proximityService.stopRadar();
            proximityServiceRadarStarted = PROXIMITY_SERVICE_RADAR_NOT_STARTED;
        }

        subscriptions.clear();
    }
}
