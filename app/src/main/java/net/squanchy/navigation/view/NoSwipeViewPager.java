package net.squanchy.navigation.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class NoSwipeViewPager extends ViewPager {

    public NoSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean executeKeyEvent(KeyEvent e) {
        return false;
    }

    @Override
    public void setCurrentItem(@Tab int item) {
        // This override is to enforce the @Tab typedef on the parameter
        super.setCurrentItem(item);
    }
}
