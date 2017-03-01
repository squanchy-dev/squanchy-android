package net.squanchy.support.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.MotionEvent;

import net.squanchy.support.graphics.CircularRevealDrawable;

public class InterceptingBottomNavigationView extends BottomNavigationView {

    private int revealDurationMillis;

    private MotionEvent lastUpEvent;
    private OnNavigationItemSelectedListener listener;
    private ColorProvider colorProvider;

    public InterceptingBottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterceptingBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        super.setOnNavigationItemSelectedListener(this::onItemSelected);
        revealDurationMillis = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    public void setRevealDurationMillis(@IntRange(from = 0) int revealDurationMillis) {
        this.revealDurationMillis = revealDurationMillis;
    }

    @Override
    public void setBackground(Drawable background) {
        if (background instanceof ColorDrawable) {
            super.setBackground(CircularRevealDrawable.from((ColorDrawable) background));
        } else {
            throw new IllegalArgumentException("Only ColorDrawables allowed");
        }
    }

    private boolean onItemSelected(MenuItem menuItem) {
        boolean itemSelected = true;
        if (listener != null) {
            itemSelected = listener.onNavigationItemSelected(menuItem);
        }

        if (itemSelected && colorProvider != null) {
            startCircularRevealTo(colorProvider.getSelectedItemColor());
        }

        return itemSelected;
    }

    private void startCircularRevealTo(int color) {
        CircularRevealDrawable revealDrawable = (CircularRevealDrawable) getBackground();
        revealDrawable.setHotspot(
                lastUpEvent.getX() - getX(),
                lastUpEvent.getY() - getY()
        );
        revealDrawable.animateToColor(color, revealDurationMillis);
    }

    @Override
    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            lastUpEvent = ev;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setColorProvider(ColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    public interface ColorProvider {

        @ColorInt
        int getSelectedItemColor();
    }
}
