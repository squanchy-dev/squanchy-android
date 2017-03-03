package net.squanchy.navigation;

import android.animation.ValueAnimator;
import android.content.Intent;
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

import com.hadisatrio.optional.Optional;

import java.util.HashMap;
import java.util.Map;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.search.OnSearchClickListener;
import net.squanchy.search.SearchActivity;
import net.squanchy.support.widget.InterceptingBottomNavigationView;

public class HomeActivity extends TypefaceStyleableActivity implements OnSearchClickListener {

    private static final String STATE_KEY_SELECTED_PAGE_INDEX = "HomeActivity.selected_page_index";

    private final Map<BottomNavigationSection, View> pageViews = new HashMap<>(4);

    private int pageFadeDurationMillis;

    private BottomNavigationSection currentSection;
    private InterceptingBottomNavigationView bottomNavigationView;
    private ViewGroup pageContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pageFadeDurationMillis = getResources().getInteger(android.R.integer.config_shortAnimTime);

        pageContainer = (ViewGroup) findViewById(R.id.page_container);
        collectPageViewsInto(pageViews);

        bottomNavigationView = (InterceptingBottomNavigationView) findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        BottomNavigationSection selectedPage = getSelectedSectionOrDefault(Optional.ofNullable(savedInstanceState));
        selectInitialPage(selectedPage);
        selectPage(BottomNavigationSection.SCHEDULE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectInitialPage(currentSection);
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
    }

    private void swapPageTo(BottomNavigationSection section) {
        if (currentSection != null) {
            pageViews.get(currentSection).setVisibility(View.INVISIBLE);
        }
        pageViews.get(section).setVisibility(View.VISIBLE);
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

    private Resources.Theme getThemeFor(BottomNavigationSection section) {
        Resources.Theme theme = getResources().newTheme();
        theme.setTo(getTheme());
        theme.applyStyle(section.theme(), true);
        return theme;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_KEY_SELECTED_PAGE_INDEX, currentSection.ordinal());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSearchClick() {
        startActivity(new Intent(this, SearchActivity.class));
    }
}
