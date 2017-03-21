package net.squanchy.venue;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import net.squanchy.R;
import net.squanchy.navigation.Navigator;

public class VenueInfoPageView extends LinearLayout {

    private Navigator navigate;

    public VenueInfoPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VenueInfoPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VenueInfoPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Activity activity = unwrapToActivityContext(getContext());
        VenueInfoComponent component = VenueInfoInjector.obtain(activity);
        navigate = component.navigator();

        setupToolbar();
    }

    private static Activity unwrapToActivityContext(Context context) {
        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            ContextWrapper contextWrapper = (ContextWrapper) context;
            return unwrapToActivityContext(contextWrapper.getBaseContext());
        } else {
            throw new IllegalStateException("Context type not supported: " + context.getClass().getCanonicalName());
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.venue_info_label);
        toolbar.inflateMenu(R.menu.venue_info_menu);
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
}
