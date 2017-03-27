package net.squanchy.venue;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.home.LifecycleView;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.navigation.Navigator;
import net.squanchy.venue.domain.view.Venue;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class VenueInfoPageView extends LinearLayout implements LifecycleView {

    private Disposable subscription;
    private Navigator navigate;
    private VenueInfoService service;
    private TextView nameText;
    private TextView addressText;
    private TextView descriptionText;
    private ImageView mapView;
    private ImageLoader imageLoader;

    public VenueInfoPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VenueInfoPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VenueInfoPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (!isInEditMode()) {
            Activity activity = unwrapToActivityContext(context);
            imageLoader = ImageLoaderInjector.obtain(activity).imageLoader();
        }
        
        super.setOrientation(VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException(VenueInfoPageView.class.getSimpleName() + " doesn't support changing orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        Activity activity = unwrapToActivityContext(getContext());
        VenueInfoComponent component = VenueInfoInjector.obtain(activity);
        navigate = component.navigator();
        service = component.service();

        nameText = (TextView) findViewById(R.id.venue_name);
        addressText = (TextView) findViewById(R.id.venue_address);
        descriptionText = (TextView) findViewById(R.id.venue_description);
        mapView = (ImageView) findViewById(R.id.venue_map);

        setupToolbar();
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
        addressText.setText(venue.address());
        descriptionText.setText(venue.description());
        loadMap(mapView, venue.mapUrl(), imageLoader);
        updateMapClickListenerWith(venue);
    }

    private void loadMap(ImageView imageView, String mapUrl, ImageLoader imageLoader) {
        imageLoader.load(mapUrl).into(imageView);
    }

    private void updateMapClickListenerWith(Venue venue) {
        mapView.setOnClickListener(v -> {
            navigate.toMapsFor(venue);
        });
    }

    @Override
    public void onStop() {
        subscription.dispose();
    }
}
