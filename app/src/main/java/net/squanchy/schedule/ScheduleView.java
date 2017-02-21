package net.squanchy.schedule;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Schedule;
import net.squanchy.schedule.view.ScheduleViewPagerAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class ScheduleView extends CoordinatorLayout {

    public static ScheduleView inflate(Context context, ViewGroup parent) {
        return (ScheduleView) LayoutInflater.from(context).inflate(R.layout.schedule_view, parent, false);
    }

    private ScheduleViewPagerAdapter viewPagerAdapter;
    private View progressBar;
    private Disposable subscription;
    private ScheduleService service;

    public ScheduleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = findViewById(R.id.progressbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabstrip);
        tabLayout.setupWithViewPager(viewPager);
        hackToApplyTypefaces(tabLayout);

        viewPagerAdapter = new ScheduleViewPagerAdapter(getContext());
        viewPager.setAdapter(viewPagerAdapter);

        ScheduleComponent component = ScheduleInjector.obtain(getContext());
        service = component.service();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_schedule);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        subscription = service.schedule()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> updateWith(schedule, null));
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        subscription.dispose();
    }

    private void hackToApplyTypefaces(TabLayout tabLayout) {
        // Unfortunately doing this the sensible way (in ScheduleViewPagerAdapter.getPageTitle())
        // results in a bunch of other views on screen to stop drawing their text, for reasons only
        // known to the Gods of Kobol. We can't theme things in the TabLayout either, as the
        // TextAppearance is applied _after_ inflating the tab views, which means Calligraphy can't
        // intercept that either. Sad panda.
        tabLayout.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            Typeface typeface = TypefaceUtils.load(getContext().getAssets(), "fonts/LeagueSpartan-Bold.otf");
            int tabCount = tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab == null) {
                    continue;
                }
                tab.setText(CalligraphyUtils.applyTypefaceSpan(tab.getText(), typeface));
            }
        });
    }

    public void updateWith(Schedule schedule, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        viewPagerAdapter.updateWith(schedule.pages(), listener);
        progressBar.setVisibility(GONE);
    }
}
