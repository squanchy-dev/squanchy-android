package net.squanchy.venue;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.home.Loadable;
import net.squanchy.imageloader.ImageLoader;
import net.squanchy.imageloader.ImageLoaderInjector;
import net.squanchy.navigation.Navigator;
import net.squanchy.venue.domain.view.Venue;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static net.squanchy.support.ContextUnwrapper.unwrapToActivityContext;

public class VenueInfoPageView extends CoordinatorLayout implements Loadable {

    private Disposable subscription;
    private final Navigator navigate;
    private final VenueInfoService service;
    private TextView nameText;
    private TextView addressText;
    private TextView descriptionText;
    private ImageView mapView;
    private ImageLoader imageLoader;

    public VenueInfoPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VenueInfoPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AppCompatActivity activity = unwrapToActivityContext(getContext());
        VenueInfoComponent component = VenueInfoInjector.obtain(activity);
        navigate = component.navigator();
        service = component.service();

        if (!isInEditMode()) {
            imageLoader = ImageLoaderInjector.obtain(activity).imageLoader();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        nameText = (TextView) findViewById(R.id.venue_name);
        addressText = (TextView) findViewById(R.id.venue_address);
        descriptionText = (TextView) findViewById(R.id.venue_description);
        mapView = (ImageView) findViewById(R.id.venue_map);

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.venue_info_label);
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
        subscription = service.venue()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateWith);
    }

    private void updateWith(Venue venue) {
        nameText.setText(venue.getName());
        addressText.setText(venue.getAddress());
        descriptionText.setText(parseHtml(venue.getDescription()));
        loadMap(mapView, venue.getMapUrl(), imageLoader);
        updateMapClickListenerWith(venue);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressWarnings("deprecation")        // The older fromHtml() is only called pre-24
    private Spanned parseHtml(String description) {
        // TODO handle this properly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(description);
        }
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
    public void stopLoading() {
        subscription.dispose();
    }
}
