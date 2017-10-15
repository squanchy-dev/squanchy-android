package net.squanchy.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import net.squanchy.typeface.TypefaceController;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class SchedulePageView extends CoordinatorLayout implements Loadable {

    private final ScheduleViewPagerAdapter viewPagerAdapter;
    private View progressBar;
    private Disposable subscription;
    private final ScheduleService service;
    private final Navigator navigate;
    private final Analytics analytics;
    private final TypefaceController typefaceController;

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
        typefaceController = component.typefaceController();

        viewPagerAdapter = new ScheduleViewPagerAdapter(activity);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = findViewById(R.id.progressbar);

        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabstrip);
        tabLayout.setupWithViewPager(viewPager);
        hackToApplyTypefaces(tabLayout);

        viewPager.setAdapter(viewPagerAdapter);

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
            int chosenFont = getFontPathFor(context);

            Typeface typeface = ResourcesCompat.getFont(context, chosenFont);
            int tabCount = tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab == null || hasSpan(tab.getText())) {
                    continue;
                }

                tab.setText(typefaceController.applyTypeface(tab.getText(), typeface));
            }
        });
    }

    private int getFontPathFor(Context context) {
        TypedArray a = context.obtainStyledAttributes(R.style.TextAppearance_Squanchy_Tab, new int[]{R.attr.fontFamily});
        try {
            return a.getInt(0, -1);
        } finally {
            a.recycle();
        }
    }

    private boolean hasSpan(CharSequence text) {
        if (!(text instanceof Spanned)) {
            return false;
        }
        return typefaceController.hasSpan((Spanned) text);
    }

    public void updateWith(Schedule schedule, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        viewPagerAdapter.updateWith(schedule.getPages(), listener);
        progressBar.setVisibility(GONE);
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
