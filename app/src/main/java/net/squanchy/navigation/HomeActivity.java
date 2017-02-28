package net.squanchy.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.view.HomeViewPagerAdapter;
import net.squanchy.navigation.view.NoSwipeViewPager;

public class HomeActivity extends TypefaceStyleableActivity {

    private static final int KEEP_ALL_PAGES = 4;       // This is more than what we need but it's to have some leeway

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        NoSwipeViewPager viewPager = (NoSwipeViewPager) findViewById(R.id.main_view_pager);
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(KEEP_ALL_PAGES);

        BottomNavigationHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_schedule:
                            viewPager.setCurrentItem(HomeViewPagerAdapter.SCHEDULE_POSITION);
                            break;
                        case R.id.action_favorite:
                            viewPager.setCurrentItem(HomeViewPagerAdapter.FAVORITES_POSITION);
                            break;
                        case R.id.action_tweet:
                            viewPager.setCurrentItem(HomeViewPagerAdapter.TWEETS_POSITION);
                            break;
                        case R.id.action_venue:
                            viewPager.setCurrentItem(HomeViewPagerAdapter.VENUE_POSITION);
                            break;
                        default:
                            throw new IndexOutOfBoundsException("Unsupported navigation item ID: " + item.getItemId());
                    }
                    return true;
                }
        );
    }
}
