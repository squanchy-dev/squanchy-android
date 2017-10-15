package net.squanchy.venue

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.Spanned
import android.util.AttributeSet
import android.widget.ImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.merge_venue_info_layout.view.venueAddress
import kotlinx.android.synthetic.main.merge_venue_info_layout.view.venueDescription
import kotlinx.android.synthetic.main.merge_venue_info_layout.view.venueMap
import kotlinx.android.synthetic.main.merge_venue_info_layout.view.venueName
import net.squanchy.R
import net.squanchy.home.Loadable
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.ImageLoaderInjector
import net.squanchy.navigation.Navigator
import net.squanchy.support.ContextUnwrapper.unwrapToActivityContext
import net.squanchy.venue.domain.view.Venue

class VenueInfoPageView : CoordinatorLayout, Loadable {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        if (isInEditMode) {
            return
        }

        val activity = unwrapToActivityContext(getContext())
        val component = venueInfoComponent(activity)

        navigator = component.navigator()
        service = component.service()
        imageLoader = ImageLoaderInjector.obtain(activity).imageLoader()
    }

    private lateinit var navigator: Navigator
    private lateinit var service: VenueInfoService
    private lateinit var imageLoader: ImageLoader

    private var subscription: Disposable? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.venue_info_label)
        toolbar.inflateMenu(R.menu.homepage)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    navigator.toSearch()
                    true
                }
                R.id.action_settings -> {
                    navigator.toSettings()
                    true
                }
                else -> false
            }
        }
    }

    override fun startLoading() {
        subscription = service.venue()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { this.updateWith(it) }
    }

    private fun updateWith(venue: Venue) {
        venueName.text = venue.name
        venueAddress.text = venue.address
        venueDescription.text = parseHtml(venue.description)
        loadMap(venueMap, venue.mapUrl, imageLoader)
        updateMapClickListenerWith(venue)
    }

    @TargetApi(Build.VERSION_CODES.N) // The older fromHtml() is only called pre-24
    private fun parseHtml(description: String): Spanned {
        // TODO handle this properly
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(description)
        }
    }

    private fun loadMap(imageView: ImageView, mapUrl: String, imageLoader: ImageLoader) {
        imageLoader.load(mapUrl).into(imageView)
    }

    private fun updateMapClickListenerWith(venue: Venue) {
        venueMap.setOnClickListener { navigator.toMapsFor(venue) }
    }

    override fun stopLoading() {
        subscription?.dispose()
    }
}
