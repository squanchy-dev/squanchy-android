package net.squanchy.schedule;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import net.squanchy.R;
import net.squanchy.analytics.Analytics;
import net.squanchy.analytics.ContentType;
import net.squanchy.home.Loadable;
import net.squanchy.navigation.Navigator;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.domain.view.SchedulePage;
import net.squanchy.schedule.view.ScheduleViewPagerAdapter;
import net.squanchy.support.font.FontCompat;
import net.squanchy.support.font.TypefaceCompat;

import org.joda.time.LocalDate;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class SchedulePageView extends CoordinatorLayout implements Loadable {

    private static final int NO_PAGE = -1;

    private View progressBar;
    private ViewPager viewPager;
    private Disposable subscription;
    private final ScheduleViewPagerAdapter viewPagerAdapter;
    private final ScheduleService service;
    private final Navigator navigate;
    private final Analytics analytics;

    private int currentPage = NO_PAGE;

    public SchedulePageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SchedulePageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        AppCompatActivity activity = unwrapToActivityContext(getContext());
        ScheduleComponent component = ScheduleComponentKt.obtain(activity);
        service = component.service();
        navigate = component.navigator();
        analytics = component.analytics();

        viewPagerAdapter = new ScheduleViewPagerAdapter(activity);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = findViewById(R.id.progressbar);

        viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabstrip);
        tabLayout.setupWithViewPager(viewPager);
        hackToApplyTypefaces(tabLayout);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //nothing to do
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //nothing to do
            }
        });

        tabLayout.addOnTabSelectedListener(new TrackingOnTabSelectedListener(analytics, viewPagerAdapter));

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_schedule);
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
    public void startLoading() {
        subscription = service.schedule(false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> updateWith(schedule, this::onEventClicked), Timber::e);
    }

    private void onEventClicked(Event event) {
        analytics.trackItemSelected(ContentType.SCHEDULE_ITEM, event.getId());
        navigate.toEventDetails(event.getId());
    }

    @Override
    public void stopLoading() {
        subscription.dispose();
    }

    private void hackToApplyTypefaces(TabLayout tabLayout) {
        // Unfortunately doing this the sensible way (in ScheduleViewPagerAdapter.getPageTitle())
        // results in a bunch of other views on screen to stop drawing their text, for reasons only
        // known to the Gods of Kobol. We can't theme things in the TabLayout either, as the
        // TextAppearance is applied _after_ inflating the tab views, which means Calligraphy can't
        // intercept that either. Sad panda.
        tabLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            Context context = tabLayout.getContext();
            Typeface typeface = FontCompat.getFontFor(context, R.style.TextAppearance_Squanchy_Tab);

            int tabCount = tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab == null || hasTypefaceSpan(tab.getText())) {
                    continue;
                }
                tab.setText(TypefaceCompat.applyTypeface(tab.getText(), typeface));
            }
        });
    }

    private boolean hasTypefaceSpan(CharSequence text) {
        if (!(text instanceof Spanned)) {
            return false;
        }
        return TypefaceCompat.hasTypefaceSpan((Spanned) text);
    }

    public void updateWith(Schedule schedule, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        List<SchedulePage> pages = schedule.getPages();
        viewPagerAdapter.updateWith(pages, listener);
        if (currentPage == NO_PAGE) {
            viewPager.setCurrentItem(findTodayIndexOrDefault(pages));
        }
        progressBar.setVisibility(GONE);
    }

    private int findTodayIndexOrDefault(List<SchedulePage> pages) {
        LocalDate now = LocalDate.now();
        for (int i = 0; i < pages.size(); i++) {
            SchedulePage page = pages.get(i);
            if (page.getDate().toLocalDate().equals(now)) {
                return i;
            }
        }
        return 0;
    }

    private static final class TrackingOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

        private final Analytics analytics;
        private final ScheduleViewPagerAdapter viewPagerAdapter;

        TrackingOnTabSelectedListener(Analytics analytics, ScheduleViewPagerAdapter viewPagerAdapter) {
            this.analytics = analytics;
            this.viewPagerAdapter = viewPagerAdapter;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            analytics.trackItemSelected(ContentType.SCHEDULE_DAY, viewPagerAdapter.getPageDayId(tab.getPosition()));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // No-op
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // No-op
        }
    }
}
