package net.squanchy.venue

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.merge_venue_info_layout.view.*
import net.squanchy.R
import net.squanchy.home.Loadable
import net.squanchy.imageloader.ImageLoader
import net.squanchy.imageloader.imageLoaderComponent
import net.squanchy.navigation.Navigator
import net.squanchy.support.text.parseHtml
import net.squanchy.support.unwrapToActivityContext
import net.squanchy.venue.domain.view.Venue
import timber.log.Timber

class VenueInfoPageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr), Loadable {

    private lateinit var navigator: Navigator
    private lateinit var service: VenueInfoService

    private var imageLoader: ImageLoader? = null
    private var subscription: Disposable? = null

    init {
        if (!isInEditMode) {
            val activity = context.unwrapToActivityContext()
            val component = venueInfoComponent(activity)
            navigator = component.navigator()
            service = component.service()
            imageLoader = imageLoaderComponent(activity).imageLoader()
        }
    }

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
            .subscribe(::updateWith, Timber::e)
    }

    private fun updateWith(venue: Venue) {
        venueName.text = venue.name
        venueAddress.text = venue.address
        venueDescription.text = venue.description.parseHtml()
        loadMap(venueMap, venue.mapUrl, imageLoader)
        updateMapClickListenerWith(venue)
    }

    private fun loadMap(imageView: ImageView, mapUrl: String, imageLoader: ImageLoader?) {
        if (imageLoader == null) {
            throw IllegalStateException("Unable to access the ImageLoader, it hasn't been initialized yet")
        }

        imageLoader.load(mapUrl).into(imageView)
    }

    private fun updateMapClickListenerWith(venue: Venue) {
        venueMap.setOnClickListener { navigator.toMapsFor(venue) }
    }

    override fun stopLoading() {
        subscription?.dispose()
    }
}
