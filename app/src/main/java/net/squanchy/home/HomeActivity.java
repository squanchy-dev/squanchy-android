package net.squanchy.home;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
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
import java.util.Locale;
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
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.service.proximity.injection.ProximityService;
import net.squanchy.support.lang.Optional;
import net.squanchy.support.widget.InterceptingBottomNavigationView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class HomeActivity extends TypefaceStyleableActivity {

    private static final String KEY_CONTEST_STAND = "stand";
    private static final String KEY_ROOM_EVENT = "room";
    private static final String WHEN_DATE_TIME_FORMAT = "HH:mm";

    private static final int REQUEST_SIGN_IN_MAY_GOD_HAVE_MERCY_OF_OUR_SOULS = 666;
    private static final int REQUEST_GRANT_PERMISSIONS = 1000;

    private final Map<BottomNavigationSection, View> pageViews = new HashMap<>(4);
    private final List<Loadable> loadables = new ArrayList<>(4);

    private int pageFadeDurationMillis;

    private BottomNavigationSection currentSection;
    private InterceptingBottomNavigationView bottomNavigationView;
    private ViewGroup pageContainer;
    private ProximityService proximityService;
    private Analytics analytics;
    private RemoteConfig remoteConfig;
    private Navigator navigator;
    private CurrentEventService currentEventService;

    private CompositeDisposable subscriptions;

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
        collectLoadablesInto(loadables);

        bottomNavigationView = (InterceptingBottomNavigationView) findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        Intent intent = getIntent();
        selectPageFrom(intent, Optional.fromNullable(savedInstanceState));

        HomeComponent homeComponent = HomeInjector.obtain(this);
        analytics = homeComponent.analytics();
        remoteConfig = homeComponent.remoteConfig();
        proximityService = homeComponent.proximityService();
        currentEventService = homeComponent.currentEvent();

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

        startAllSubscriptions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_GRANT_PERMISSIONS) {
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
                    REQUEST_GRANT_PERMISSIONS
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

    @Override
    protected void onResume() {
        super.onResume();

        selectInitialPage(currentSection);
    }

    private void handleProximityEvent(ProximityEvent proximityEvent) {
        switch (proximityEvent.action()) {
            case KEY_CONTEST_STAND:
                navigator.toContest(proximityEvent.subject());
                break;
            case KEY_ROOM_EVENT:
                showCurrentEvent(proximityEvent.subject());
                break;
        }
    }

    private void showCurrentEvent(String placeId) {
        currentEventService.eventIn(placeId)
                .map(this::toSnackbar)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Snackbar::show);
    }

    public Snackbar toSnackbar(Event event) {
        Snackbar snackbar = Snackbar.make(pageViews.get(currentSection), buildString(event), BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.event_details, view -> navigator.toEventDetails(event.id()));
        snackbar.setActionTextColor(getResources().getColor(R.color.text_inverse));
        return snackbar;
    }

    private String buildString(Event event) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(WHEN_DATE_TIME_FORMAT)
                .withZone(event.timeZone());
        return String.format(
                Locale.US,
                "Room: %s %s \n%s",
                event.place().get().name(),
                formatter.print(event.startTime().toDateTime()),
                event.title()
        );
    }

    private void collectPageViewsInto(Map<BottomNavigationSection, View> pageViews) {
        pageViews.put(BottomNavigationSection.SCHEDULE, pageContainer.findViewById(R.id.schedule_content_root));
        pageViews.put(BottomNavigationSection.FAVORITES, pageContainer.findViewById(R.id.favorites_content_root));
        pageViews.put(BottomNavigationSection.TWEETS, pageContainer.findViewById(R.id.tweets_content_root));
        pageViews.put(BottomNavigationSection.VENUE_INFO, pageContainer.findViewById(R.id.venue_content_root));
    }

    private void collectLoadablesInto(List<Loadable> loadables) {
        loadables.add((Loadable) pageContainer.findViewById(R.id.schedule_content_root));
        loadables.add((Loadable) pageContainer.findViewById(R.id.favorites_content_root));
        loadables.add((Loadable) pageContainer.findViewById(R.id.tweets_content_root));
        loadables.add((Loadable) pageContainer.findViewById(R.id.venue_content_root));
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

    public void requestSignIn() {
        disposeAllSubscriptions();
        navigator.toSignInForResult(REQUEST_SIGN_IN_MAY_GOD_HAVE_MERCY_OF_OUR_SOULS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGN_IN_MAY_GOD_HAVE_MERCY_OF_OUR_SOULS) {
            startAllSubscriptions();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startAllSubscriptions() {
        subscriptions.add(remoteConfig.proximityServicesEnabled()
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
                }));

        for (Loadable loadable : loadables) {
            loadable.startLoading();
        }
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

        disposeAllSubscriptions();
    }

    private void disposeAllSubscriptions() {
        subscriptions.clear();

        for (Loadable loadable : loadables) {
            loadable.stopLoading();
        }
    }
}
