package net.squanchy.home.deeplink

import android.content.Intent
import android.os.Bundle

import net.squanchy.home.BottomNavigationSection
import net.squanchy.home.HomeStatePersister

import net.squanchy.home.deeplink.HomeActivityDeepLinkCreator.KEY_INITIAL_PAGE_INDEX

class HomeActivityIntentParser(private val savedState: Bundle?, private val intent: Intent) {

    private val statePersister: HomeStatePersister = HomeStatePersister()

    val initialSelectedPage: BottomNavigationSection
        get() = getSectionFrom(statePersister, savedState)
                ?: intent.extras.section
                ?: BottomNavigationSection.SCHEDULE

    private fun getSectionFrom(statePersister: HomeStatePersister, savedState: Bundle?): BottomNavigationSection? {
        return statePersister.readSectionFrom(savedState)
    }

    private val Bundle?.section: BottomNavigationSection?
        get() {
            val bundle = this ?: Bundle.EMPTY

            if (bundle.containsKey(KEY_INITIAL_PAGE_INDEX)) {
                val sectionIndex = bundle.getInt(KEY_INITIAL_PAGE_INDEX)
                return BottomNavigationSection.values()[sectionIndex]
            } else {
                return null
            }
        }
}
