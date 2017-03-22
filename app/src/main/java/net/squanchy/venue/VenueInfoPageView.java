package net.squanchy.venue;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.navigation.LifecycleView;
import net.squanchy.navigation.Navigator;
import net.squanchy.venue.domain.view.Venue;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class VenueInfoPageView extends LinearLayout implements LifecycleView {

    private Disposable subscription;
    private Navigator navigate;
    private VenueInfoService service;
    private TextView nameText;

    public VenueInfoPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VenueInfoPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VenueInfoPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        super.setOrientation(VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("TweetsPageView doesn't support changing orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Activity activity = unwrapToActivityContext(getContext());
        VenueInfoComponent component = VenueInfoInjector.obtain(activity);
        navigate = component.navigator();
        service = component.service();

        nameText = (TextView) findViewById(R.id.venue_name);

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

    @Override
    public void onStart() {
        subscription = service.venue()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateWith);
    }

    private void updateWith(Venue venue) {
        nameText.setText(venue.name());
    }

    @Override
    public void onStop() {
        subscription.dispose();
    }
}
