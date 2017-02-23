package net.squanchy.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.SearchView;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.view.HomeViewPagerAdapter;
import net.squanchy.navigation.view.NoSwipeViewPager;
import net.squanchy.search.OnSearchClickListener;
import net.squanchy.search.AbstractSearchActivity;
import net.squanchy.search.SearchActivity;

public class HomeActivity extends TypefaceStyleableActivity implements OnSearchClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        NoSwipeViewPager viewPager = (NoSwipeViewPager) findViewById(R.id.main_view_pager);
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        BottomNavigationHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_schedule:
                            viewPager.setCurrentItem(HomeViewPagerAdapter.SCHEDULE_POSITION);
                            break;
                        case R.id.action_favourite:
                            break;
                        case R.id.action_tweet:
                            break;
                        case R.id.action_venue:
                            break;
                        default:
                            throw new IndexOutOfBoundsException("Unsupported navigation item ID: " + item.getItemId());
                    }
                    return true;
                }
        );
    }

    @Override
    public void onSearchClick() {
        startActivity(new Intent(this, SearchActivity.class));
    }
}
