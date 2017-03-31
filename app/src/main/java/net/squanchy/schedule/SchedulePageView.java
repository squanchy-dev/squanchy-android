package net.squanchy.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;

import net.squanchy.R;
import net.squanchy.analytics.Analytics;
import net.squanchy.analytics.ContentType;
import net.squanchy.home.Loadable;
import net.squanchy.navigation.Navigator;
import net.squanchy.schedule.domain.view.Event;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.view.ScheduleViewPagerAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class SchedulePageView extends CoordinatorLayout implements Loadable {

    private ScheduleViewPagerAdapter viewPagerAdapter;
    private View progressBar;
    private Disposable subscription;
    private final ScheduleService service;
    private final Navigator navigate;
    private final Analytics analytics;
    private final Activity activity;

    public SchedulePageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SchedulePageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        activity = unwrapToActivityContext(getContext());
        ScheduleComponent component = ScheduleInjector.obtain(activity);
        service = component.service();
        navigate = component.navigator();
        analytics = component.analytics();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = findViewById(R.id.progressbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabstrip);
        tabLayout.setupWithViewPager(viewPager);
        hackToApplyTypefaces(tabLayout);

        viewPagerAdapter = new ScheduleViewPagerAdapter(activity);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TrackingOnTabSelectedListener(analytics, viewPagerAdapter));

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        analytics.trackItemSelected(ContentType.SCHEDULE_ITEM, event.id());
        navigate.toEventDetails(event.id());
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
            String fontPath = getFontPathFor(context);

            Typeface typeface = TypefaceUtils.load(context.getAssets(), fontPath);
            int tabCount = tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab == null || hasSpan(tab.getText())) {
                    continue;
                }
                tab.setText(CalligraphyUtils.applyTypefaceSpan(tab.getText(), typeface));
            }
        });
    }

    private String getFontPathFor(Context context) {
        TypedArray a = context.obtainStyledAttributes(R.style.TextAppearance_Squanchy_Tab, new int[]{R.attr.fontPath});
        try {
            return a.getString(0);
        } finally {
            a.recycle();
        }
    }

    private boolean hasSpan(CharSequence text) {
        if (!(text instanceof Spanned)) {
            return false;
        }
        Spanned spannable = (Spanned) text;
        CalligraphyTypefaceSpan[] spans = spannable.getSpans(0, text.length(), CalligraphyTypefaceSpan.class);
        return spans.length > 0;
    }

    public void updateWith(Schedule schedule, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        viewPagerAdapter.updateWith(schedule.pages(), listener);
        progressBar.setVisibility(GONE);
    }

    private static class TrackingOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

        private final Analytics analytics;
        private final ScheduleViewPagerAdapter viewPagerAdapter;

        private TrackingOnTabSelectedListener(Analytics analytics, ScheduleViewPagerAdapter viewPagerAdapter) {
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
