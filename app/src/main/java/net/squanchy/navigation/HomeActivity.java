package net.squanchy.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import net.squanchy.R;
import net.squanchy.favorites.FavoritesFragment;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.schedule.ScheduleFragment;
import net.squanchy.tweets.TweetsFragment;
import net.squanchy.venueinfo.VenueInfoFragment;

public class HomeActivity extends TypefaceStyleableActivity {

    private static final String CURRENT_SECTION = "current_section";
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

        Fragment currentFragment = fragmentManager.findFragmentByTag(CURRENT_SECTION);
        if (currentFragment != null) {
            transaction.remove(currentFragment);
        }
        transaction.add(R.id.fragment_container, createFragmentFor(section), CURRENT_SECTION)
                .commit();
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
}
