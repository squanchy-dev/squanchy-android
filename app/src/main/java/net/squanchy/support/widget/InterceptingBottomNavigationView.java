package net.squanchy.support.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import net.squanchy.support.graphics.CircularRevealDrawable;
import net.squanchy.support.lang.Optional;
import net.squanchy.support.view.Hotspot;

public class InterceptingBottomNavigationView extends BottomNavigationView {

    private Optional<MotionEvent> lastUpEvent = Optional.absent();
    private Optional<OnNavigationItemSelectedListener> listener = Optional.absent();
    private Optional<ColorProvider> colorProvider = Optional.absent();

    private int revealDurationMillis;

    public InterceptingBottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterceptingBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        super.setOnNavigationItemSelectedListener(this::onItemSelected);
        revealDurationMillis = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    private boolean onItemSelected(MenuItem menuItem) {
        boolean itemSelected = true;
        if (listener.isPresent()) {
            itemSelected = listener.get().onNavigationItemSelected(menuItem);
        }

        if (itemSelected && colorProvider.isPresent()) {
            startCircularReveal(colorProvider.get().getSelectedItemColor(), menuItem);
        }

        return itemSelected;
    }

    private void startCircularReveal(@ColorInt int color, MenuItem menuItem) {
        CircularRevealDrawable revealDrawable = (CircularRevealDrawable) getBackground();
        setBackgroundHotspot(revealDrawable, menuItem);
        revealDrawable.animateToColor(color, revealDurationMillis);
    }

    private void setBackgroundHotspot(CircularRevealDrawable revealDrawable, MenuItem menuItem) {
        Hotspot hotspot = lastUpEvent.map(Hotspot::fromMotionEvent)
                .or(getHotspotFor(menuItem));
        revealDrawable.setHotspot(hotspot.getX(), hotspot.getY());
    }

    private Hotspot getHotspotFor(MenuItem menuItem) {
        int selectedPosition = positionFor(menuItem);
        ViewGroup menuView = getBottomNavigationMenuView();
        View selectedItemView = menuView.getChildAt(selectedPosition);

        return Hotspot.fromCenterOf(selectedItemView)
                .offsetToParent(menuView);
    }

    @IntRange(from = 0)
    private int positionFor(MenuItem menuItem) {
        Menu menu = getMenu();
        int menuSize = menu.size();
        for (int i = 0; i < menuSize; i++) {
            int itemId = menu.getItem(i).getItemId();
            if (itemId == menuItem.getItemId()) {
                return i;
            }
        }
        throw new IllegalArgumentException("Menu item " + menuItem + " not found in the bottom bar items");
    }

    public void setRevealDurationMillis(@IntRange(from = 0) int revealDurationMillis) {
        this.revealDurationMillis = revealDurationMillis;
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        Drawable background = getBackground();
        if (background instanceof CircularRevealDrawable) {
            updateBackgroundColor(background, color);
        } else {
            setBackgroundColor(color);
        }
    }

    @Override
    public void setBackground(Drawable background) {
        if (background instanceof ColorDrawable) {
            Drawable currentBackground = getBackground();
            int newColor = ((ColorDrawable) background).getColor();
            updateBackgroundColor(currentBackground, newColor);
        } else {
            throw new IllegalArgumentException("Only ColorDrawables are supported");
        }
    }

    private void updateBackgroundColor(Drawable drawable, int newColor) {
        if (drawable instanceof CircularRevealDrawable) {
            ((CircularRevealDrawable) drawable).setColor(newColor);
        } else {
            super.setBackground(CircularRevealDrawable.Companion.from(newColor));
        }
    }

    public void cancelTransitions() {
        Drawable background = getBackground();
        if (background instanceof CircularRevealDrawable) {
            ((CircularRevealDrawable) background).cancelTransitions();
        }
    }

    @Override
    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {
        this.listener = Optional.fromNullable(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            MotionEvent motionEvent = MotionEvent.obtainNoHistory(ev);
            motionEvent.setLocation(ev.getX(), ev.getY());
            lastUpEvent = Optional.of(motionEvent);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setColorProvider(@Nullable ColorProvider colorProvider) {
        this.colorProvider = Optional.fromNullable(colorProvider);
    }

    @SuppressLint("RestrictedApi")      // This is a hacky solution to BottomNavigationView's lack of APIs :(
    public void selectItemAt(@IntRange(from = 0) int position) {
        getMenu().getItem(position).setChecked(true);
        getBottomNavigationMenuView().updateMenuView();
    }

    private BottomNavigationMenuView getBottomNavigationMenuView() {
        return (BottomNavigationMenuView) getChildAt(0);
    }

    public interface ColorProvider {

        @ColorInt
        int getSelectedItemColor();
    }
}
