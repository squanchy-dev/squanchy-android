package net.squanchy.home;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.squanchy.R;
import net.squanchy.analytics.Analytics;
import net.squanchy.analytics.ContentType;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.home.deeplink.HomeActivityDeepLinkCreator;
import net.squanchy.home.deeplink.HomeActivityIntentParser;
import net.squanchy.navigation.Navigator;
import net.squanchy.proximity.ProximityEvent;
import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.service.proximity.injection.ProximityService;
import net.squanchy.support.lang.Optional;
import net.squanchy.support.widget.InterceptingBottomNavigationView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class HomeActivity extends TypefaceStyleableActivity {

    private static final String STATE_KEY_SELECTED_PAGE_INDEX = "HomeActivity.selected_page_index";
    private static final String KEY_CONTEST_STAND = "stand";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000;
    private static final boolean PROXIMITY_SERVICE_RADAR_NOT_STARTED = false;

    private final Map<BottomNavigationSection, View> pageViews = new HashMap<>(4);
    private final List<LifecycleView> lifecycleViews = new ArrayList<>(2);

    private int pageFadeDurationMillis;

    private BottomNavigationSection currentSection;
    private InterceptingBottomNavigationView bottomNavigationView;
    private ViewGroup pageContainer;
    private ProximityService proximityService;
    private Analytics analytics;
    private RemoteConfig remoteConfig;
    private Navigator navigator;

    private CompositeDisposable subscriptions;

    private boolean proximityServiceRadarStarted = PROXIMITY_SERVICE_RADAR_NOT_STARTED;

    public static Intent createScheduleIntent(Context context, Optional<String> dayId, Optional<String> eventId) {
        return new HomeActivityDeepLinkCreator(context)
                .deepLinkTo(BottomNavigationSection.SCHEDULE)
                .withDayId(dayId)
                .withEventId(eventId)
                .build();
    }

    public static Intent createFavoritesIntent(Context context) {
        return new HomeActivityDeepLinkCreator(context)
                .deepLinkTo(BottomNavigationSection.FAVORITES)
                .build();
    }

    public static Intent createTweetsIntent(Context context) {
        return new HomeActivityDeepLinkCreator(context)
                .deepLinkTo(BottomNavigationSection.TWEETS)
                .build();
    }

    public static Intent createVenueInfoIntent(Context context) {
        return new HomeActivityDeepLinkCreator(context)
                .deepLinkTo(BottomNavigationSection.VENUE_INFO)
                .build();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pageFadeDurationMillis = getResources().getInteger(android.R.integer.config_shortAnimTime);

        pageContainer = (ViewGroup) findViewById(R.id.page_container);
        collectPageViewsInto(pageViews);
        collectLifecycleViewsViewsInto(lifecycleViews);

        bottomNavigationView = (InterceptingBottomNavigationView) findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        Intent intent = getIntent();
        selectPageFrom(intent, Optional.fromNullable(savedInstanceState));

        HomeComponent homeComponent = HomeInjector.obtain(this);
        analytics = homeComponent.analytics();
        remoteConfig = homeComponent.remoteConfig();
        proximityService = homeComponent.proximityService();

        navigator = homeComponent.navigator();
        subscriptions = new CompositeDisposable();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        selectPageFrom(intent, Optional.absent());
    }

    private void selectPageFrom(Intent intent, Optional<Bundle> savedState) {
        HomeActivityIntentParser intentParser = new HomeActivityIntentParser(savedState, intent);
        BottomNavigationSection selectedPage = intentParser.getInitialSelectedPage();
        selectInitialPage(selectedPage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectInitialPage(currentSection);

        remoteConfig.proximityServicesEnabled()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(enabled -> {
                    if (enabled) {
                        askProximityPermissionToStartRadar();
                    } else {
                        proximityService.stopRadar();
                    }

                    subscriptions.add(
                            proximityService.observeProximityEvents()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(this::handleProximityEvent));
                });

        for (LifecycleView lifecycleView : lifecycleViews) {
            lifecycleView.onStart();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (hasGrantedFineLocationAccess(grantResults)) {
                proximityService.startRadar();
            }
        }
    }

    private void askProximityPermissionToStartRadar() {
        if (hasLocationPermission()) {
            proximityService.startRadar();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            );
        }
    }

    private boolean hasLocationPermission() {
        int granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasGrantedFineLocationAccess(int[] grantResults) {
        return grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    //TODO fix it properly
    @Override
    protected void onResume() {
        super.onResume();

        Resources.Theme theme = getThemeFor(currentSection);
        bottomNavigationView.setBackgroundColor(getColorFromTheme(theme, android.support.design.R.attr.colorPrimary));
    }

    private void handleProximityEvent(ProximityEvent proximityEvent) {
        // TODO highlight speech near the rooms
        if (proximityEvent.action().equals(KEY_CONTEST_STAND)) {
            navigator.toContest(proximityEvent.subject());
        }
    }

    private void collectPageViewsInto(Map<BottomNavigationSection, View> pageViews) {
        pageViews.put(BottomNavigationSection.SCHEDULE, pageContainer.findViewById(R.id.schedule_content_root));
        pageViews.put(BottomNavigationSection.FAVORITES, pageContainer.findViewById(R.id.favorites_content_root));
        pageViews.put(BottomNavigationSection.TWEETS, pageContainer.findViewById(R.id.tweets_content_root));
        pageViews.put(BottomNavigationSection.VENUE_INFO, pageContainer.findViewById(R.id.venue_content_root));
    }

    private void collectLifecycleViewsViewsInto(List<LifecycleView> lifecycleViews) {
        lifecycleViews.add((LifecycleView) pageContainer.findViewById(R.id.schedule_content_root));
        lifecycleViews.add((LifecycleView) pageContainer.findViewById(R.id.favorites_content_root));
        lifecycleViews.add((LifecycleView) pageContainer.findViewById(R.id.venue_content_root));
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
        HomeStatePersister statePersister = new HomeStatePersister();
        statePersister.saveCurrentSection(outState, currentSection);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();

        subscriptions.clear();

        for (LifecycleView lifecycleView : lifecycleViews) {
            lifecycleView.onStop();
        }
    }
}
