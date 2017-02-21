package net.squanchy.schedule.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Schedule;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class ScheduleView extends CoordinatorLayout {

    private ScheduleViewPagerAdapter viewPagerAdapter;
    private View progressBar;

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

        Typeface tabTypeface = getTabTypeface(getContext());
        viewPagerAdapter = new ScheduleViewPagerAdapter(tabLayout.getContext(), tabTypeface);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private Typeface getTabTypeface(Context context) {
        int typefaceAttrId = CalligraphyConfig.get().getAttrId();
        TypedArray a = context.obtainStyledAttributes(R.style.TextAppearance_Squanchy_Tab, new int[]{typefaceAttrId});
        try {
            String fontPath = a.getString(0);
            return TypefaceUtils.load(context.getAssets(), fontPath);
        } finally {
            a.recycle();
        }
    }

    public void updateWith(Schedule schedule, ScheduleViewPagerAdapter.OnEventClickedListener listener) {
        viewPagerAdapter.updateWith(schedule.pages(), listener);
        progressBar.setVisibility(GONE);
    }
}
