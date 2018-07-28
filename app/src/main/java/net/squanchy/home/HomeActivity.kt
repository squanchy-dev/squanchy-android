package net.squanchy.home

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.transition.Fade
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_home.*
import net.squanchy.R
import net.squanchy.analytics.Analytics
import net.squanchy.analytics.ContentType
import net.squanchy.home.deeplink.HomeActivityDeepLinkCreator
import net.squanchy.home.deeplink.HomeActivityIntentParser
import net.squanchy.navigation.Navigator
import net.squanchy.signin.SignInOrigin
import net.squanchy.support.content.res.getColorFromAttribute
import net.squanchy.support.widget.InterceptingBottomNavigationView

class HomeActivity : AppCompatActivity() {

    private val pageViews: Map<BottomNavigationSection, View> by lazy {
        mapOf(
            BottomNavigationSection.SCHEDULE to pageContainer.findViewById<View>(R.id.schedule_content_root),
            BottomNavigationSection.FAVORITES to pageContainer.findViewById<View>(R.id.favorites_content_root),
            BottomNavigationSection.TWEETS to pageContainer.findViewById<View>(R.id.tweetsContentRoot),
            BottomNavigationSection.VENUE_INFO to pageContainer.findViewById<View>(R.id.venueContentRoot)
        )
    }
    private val loadables: List<Loadable> by lazy {
        listOf(
            pageContainer.findViewById<View>(R.id.schedule_content_root) as Loadable,
            pageContainer.findViewById<View>(R.id.favorites_content_root) as Loadable,
            pageContainer.findViewById<View>(R.id.tweetsContentRoot) as Loadable,
            pageContainer.findViewById<View>(R.id.venueContentRoot) as Loadable
        )
    }

    private var pageFadeDurationMillis: Int = 0

    private lateinit var currentSection: BottomNavigationSection

    private lateinit var analytics: Analytics
    private lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        pageFadeDurationMillis = resources.getInteger(android.R.integer.config_shortAnimTime)

        setupBottomNavigation(bottomNavigationView)

        selectPageFrom(intent, savedInstanceState)

        with(homeComponent(this)) {
            analytics = analytics()
            navigator = navigator()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        selectPageFrom(intent, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SIGN_IN_MAY_GOD_HAVE_MERCY_OF_OUR_SOULS) {
            startLoading()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun selectPageFrom(intent: Intent, savedState: Bundle?) {
        val intentParser = HomeActivityIntentParser(savedState, intent)
        val selectedPage = intentParser.initialSelectedPage
        selectInitialPage(selectedPage)
    }

    private fun setupBottomNavigation(bottomNavigationView: InterceptingBottomNavigationView) {
        bottomNavigationView.disableShiftMode()
        bottomNavigationView.revealDurationMillis = pageFadeDurationMillis

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_schedule -> selectPage(BottomNavigationSection.SCHEDULE)
                R.id.action_favorites -> selectPage(BottomNavigationSection.FAVORITES)
                R.id.action_tweets -> selectPage(BottomNavigationSection.TWEETS)
                R.id.action_venue -> selectPage(BottomNavigationSection.VENUE_INFO)
                else -> throw IndexOutOfBoundsException("Unsupported navigation item ID: " + item.itemId)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun selectInitialPage(section: BottomNavigationSection) {
        swapPageTo(section)
        bottomNavigationView.cancelTransitions()
        bottomNavigationView.selectItemAt(section.ordinal)

        val theme = getThemeFor(section)
        bottomNavigationView.setBackgroundColor(theme.getColorFromAttribute(com.google.android.material.R.attr.colorPrimary))
        window.statusBarColor = theme.getColorFromAttribute(android.R.attr.statusBarColor)

        currentSection = section
    }

    private fun selectPage(section: BottomNavigationSection) {
        if (section == currentSection) {
            return
        }

        val transition = Fade().apply { duration = pageFadeDurationMillis.toLong() }
        TransitionManager.beginDelayedTransition(pageContainer, transition)

        swapPageTo(section)

        val theme = getThemeFor(section)
        animateStatusBarColorTo(theme.getColorFromAttribute(android.R.attr.statusBarColor))
        bottomNavigationView.colorProvider = { theme.getColorFromAttribute(com.google.android.material.R.attr.colorPrimary) }

        currentSection = section

        trackPageSelection(section)
    }

    private fun swapPageTo(section: BottomNavigationSection) {
        if (::currentSection.isInitialized) {
            pageViews[currentSection]!!.isInvisible = true
        }
        pageViews[section]!!.isVisible = true
    }

    private fun getThemeFor(section: BottomNavigationSection): Resources.Theme {
        return resources.newTheme().apply {
            setTo(theme)
            applyStyle(section.theme, true)
        }
    }

    private fun animateStatusBarColorTo(@ColorInt color: Int) {
        val currentStatusBarColor = window.statusBarColor

        animateColor(
            currentStatusBarColor,
            color,
            ValueAnimator.AnimatorUpdateListener { animation -> window.statusBarColor = animation.animatedValue as Int })
    }

    private fun animateColor(@ColorInt currentColor: Int, @ColorInt targetColor: Int, listener: ValueAnimator.AnimatorUpdateListener) {
        ValueAnimator.ofArgb(currentColor, targetColor).apply {
            duration = pageFadeDurationMillis.toLong()
            addUpdateListener(listener)
        }.start()
    }

    private fun trackPageSelection(section: BottomNavigationSection) {
        analytics.trackItemSelected(ContentType.NAVIGATION_ITEM, section.name)
        analytics.trackPageView(this, section.name)
    }

    fun requestSignIn() {
        stopLoading()
        navigator.toSignInForResult(REQUEST_SIGN_IN_MAY_GOD_HAVE_MERCY_OF_OUR_SOULS, SignInOrigin.FAVORITES)
    }

    override fun onStart() {
        super.onStart()

        selectInitialPage(currentSection)

        startLoading()
    }

    private fun startLoading() {
        loadables.forEach { it.startLoading() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val statePersister = HomeStatePersister()
        statePersister.saveCurrentSection(outState, currentSection)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()

        stopLoading()
    }

    private fun stopLoading() {
        loadables.forEach { it.stopLoading() }
    }

    companion object {

        private const val REQUEST_SIGN_IN_MAY_GOD_HAVE_MERCY_OF_OUR_SOULS = 666

        fun createScheduleIntent(context: Context, dayId: String?, eventId: String?): Intent {
            return HomeActivityDeepLinkCreator(context)
                .deepLinkTo(BottomNavigationSection.SCHEDULE)
                .withDayId(dayId)
                .withEventId(eventId)
                .build()
        }

        fun createFavoritesIntent(context: Context): Intent {
            return HomeActivityDeepLinkCreator(context)
                .deepLinkTo(BottomNavigationSection.FAVORITES)
                .build()
        }

        fun createTweetsIntent(context: Context): Intent {
            return HomeActivityDeepLinkCreator(context)
                .deepLinkTo(BottomNavigationSection.TWEETS)
                .build()
        }

        fun createVenueInfoIntent(context: Context): Intent {
            return HomeActivityDeepLinkCreator(context)
                .deepLinkTo(BottomNavigationSection.VENUE_INFO)
                .build()
        }
    }
}
