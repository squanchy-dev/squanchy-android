package net.squanchy.navigation;

import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Window;

import net.squanchy.R;
import net.squanchy.favorites.FavoritesFragment;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.schedule.ScheduleFragment;
import net.squanchy.tweets.TweetsFragment;
import net.squanchy.venueinfo.VenueInfoFragment;

public class HomeActivity extends TypefaceStyleableActivity {

    private static final String TAG_CURRENT_SECTION = "current_section";
    private static final int FRAGMENT_FADE_DURATION = 220;

    private BottomNavigationSection currentSection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        selectPage(BottomNavigationSection.SCHEDULE);
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        BottomNavigationHelper.disableShiftMode(bottomNavigationView);

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

        currentSection = section;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.findFragmentByTag(TAG_CURRENT_SECTION);
        if (currentFragment != null) {
            transaction.remove(currentFragment);
        }
        transaction.add(R.id.fragment_container, createFragmentFor(section), TAG_CURRENT_SECTION)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

        setStatusBarColorFromTheme(section.theme());
    }

    private Fragment createFragmentFor(BottomNavigationSection section) {
        switch (section) {
            case SCHEDULE:
                return new ScheduleFragment();
            case FAVORITES:
                return new FavoritesFragment();
            case TWEETS:
                return new TweetsFragment();
            case VENUE_INFO:
                return new VenueInfoFragment();
            default:
                throw new IllegalArgumentException("The section " + section + " is not supported");
        }
    }

    private void setStatusBarColorFromTheme(@StyleRes int theme) {
        ContextThemeWrapper wrappedContext = new ContextThemeWrapper(this, theme);
        TypedArray a = wrappedContext.obtainStyledAttributes(new int[]{android.R.attr.statusBarColor});
        try {
            int statusBarColor = a.getColor(0, 0);
            setStatusBarColor(statusBarColor);
        } finally {
            a.recycle();
        }
    }

    private void setStatusBarColor(@ColorInt int color) {
        Window window = getWindow();
        int currentStatusBarColor = window.getStatusBarColor();

        ValueAnimator animator = ValueAnimator.ofArgb(currentStatusBarColor, color)
                .setDuration(FRAGMENT_FADE_DURATION / 2);
        animator.addUpdateListener(animation -> window.setStatusBarColor((Integer) animation.getAnimatedValue()));
        animator.start();
    }
}
